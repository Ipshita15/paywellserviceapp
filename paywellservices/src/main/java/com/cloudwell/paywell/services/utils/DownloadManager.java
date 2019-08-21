package com.cloudwell.paywell.services.utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.cloudwell.paywell.services.BuildConfig;
import com.cloudwell.paywell.services.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import androidx.core.content.FileProvider;

public class DownloadManager extends AsyncTask<String, Integer, String> {
    private ProgressDialog progressDialog;
    private String TAG = "UpdateDownloadManager";
    private boolean installAfterDownload = true;
    private boolean downloaded = false;
    private Context mContext;
    private String downloadPath;
    private OutputStream output;

    /**
     * Constructor for the Download Manager. DO NOT USE ON YOUR OWN. All calls
     * are through UpdateChecker
     *
     * @param context Activity context
     * @since API 1
     */
    public DownloadManager(Context context) {
        this(context, true);
    }

    /**
     * Constructor for the download manager. DO NOT USE ON YOUR OWN. All calls
     * are handled through UpdateChecker.
     *
     * @param context              Activity context
     * @param installAfterDownload true if you want to install immediately after download, false
     *                             otherwise
     * @since API 2
     */

    @SuppressLint("WorldReadableFiles")
    @SuppressWarnings("deprecation")
    public DownloadManager(Context context, boolean installAfterDownload) {
        mContext = context;
        this.installAfterDownload = installAfterDownload;
        progressDialog = new ProgressDialog(mContext);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            // As MODE_WORLD_READABLE is deprecated from API 17
            try {
                output = mContext.openFileOutput("app_release_" + mContext.getString(R.string.app_next_version_no) + ".apk", Context.MODE_WORLD_READABLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
                downloadPath = Environment.getExternalStorageDirectory().getPath() + "/app_release_" + mContext.getString(R.string.app_next_version_no) + ".apk";
                try {
                    output = new FileOutputStream(downloadPath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Checks to see if we have an active internet connection
     *
     * @return true if online, false otherwise
     * @since API 1
     */
    private boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Downloads the update file in a background task
     *
     * @since API 1
     */
    @Override
    protected String doInBackground(String... sUrl) {

        if (isOnline()) {
            try {
                URL url = new URL(sUrl[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // this will be useful so that you can show a typical 0-100%
                // progress bar
                int fileLength = connection.getContentLength();
                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                byte data[] = new byte[12288];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {
                Log.e(TAG,
                        "There was an IOException when downloading the update file");
            } catch (Exception ex) {

            }
        }
        return null;
    }

    /**
     * Updates our progress bar with the download information
     *
     * @since API 1
     */
    @Override
    protected void onProgressUpdate(Integer... changed) {
        progressDialog.setProgress(changed[0]);
    }

    /**
     * Sets up the progress dialog to notify user of download progress
     *
     * @since API 1
     */
    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Downloading ...");
        progressDialog.setCancelable(false);
        //if (progressDialog == null) {
        progressDialog.show();
        //}
    }

    /**
     * Dismissed progress dialog, calls install() if installAfterDownload is
     * true
     *
     * @since API 1
     */
    @Override
    protected void onPostExecute(String result) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        downloaded = true;
        if (installAfterDownload) {
            install();
        }
    }

    /**
     * Launches an Intent to install the apk update.
     *
     * @since API 2
     */
    public void install() {
        if (downloaded) {
            String filepath = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                // As MODE_WORLD_READABLE is deprecated from API 17
                //filepath = "/data/data/" + mContext.getPackageName() + "/files/app_release_" + mContext.getString(R.string.app_next_version_no) + ".apk";
                filepath = "/data/data/" + mContext.getPackageName() + "/files";
            } else {
                if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
                    //filepath = Environment.getExternalStorageDirectory().getPath() + "/app_release_" + mContext.getString(R.string.app_next_version_no) + ".apk";
                    filepath = Environment.getExternalStorageDirectory().getPath();
                }
            }
            File toInstall = new File(filepath, "/app_release_" + mContext.getString(R.string.app_next_version_no) + ".apk");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                File file = new File(mContext.getFilesDir(), "/app_release_" + mContext.getString(R.string.app_next_version_no) + ".apk");
                Uri uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mContext.startActivity(intent);
            } else {
                Uri apkUri = Uri.fromFile(toInstall);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mContext.startActivity(intent);
            }

        }
    }

    public void installNew() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                File file = new File(mContext.getFilesDir(), "app_release_" + mContext.getString(R.string.app_next_version_no) + ".apk");
                Uri uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", file);

                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mContext.startActivity(intent);
                //finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                File file = new File(Environment.getExternalStorageDirectory() + "/app_release_" + mContext.getString(R.string.app_next_version_no) + ".apk");

                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mContext.startActivity(intent);
                //finish();
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }
}