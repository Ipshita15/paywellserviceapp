package com.cloudwell.paywell.services.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.cloudwell.paywell.services.BuildConfig;
import com.cloudwell.paywell.services.activity.utility.AllUrl;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;

import java.io.File;

import androidx.core.content.FileProvider;

public class DownloadManager2 {
    public static final String FOLDER_NAEME = "/PayWellService";
    public static final String FILE_NAME = "paywell_services_latest_version.apk";

    private final String fileName;
    private final String dirPath;
    private ProgressDialog progressDialog;
    private String TAG = "UpdateDownloadManager";
    private Context mContext;
    private String downloadPath;


    public DownloadManager2(Context context, boolean installAfterDownload) {
        mContext = context;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Downloading ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Setting timeout globally for the download network requests:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(mContext, config);

        String url = AllUrl.URL_update_check;

        File file = new File(Environment.getExternalStorageDirectory().getPath() + FOLDER_NAEME);
        if (!file.exists()) {
            file.mkdirs();
        }
        dirPath = file.getPath();
        fileName = FILE_NAME;
        downloadPath = dirPath + "/" + fileName;

        int downloadId = PRDownloader.download(url, dirPath, fileName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                        progressDialog.setProgress((int) progressPercent);

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        progressDialog.dismiss();
                        Log.e(TAG, "onDownloadComplete: ");
                        installAPKNew(downloadPath);
                    }

                    @Override
                    public void onError(Error error) {
                        progressDialog.dismiss();
                        Log.e(TAG, "");
                    }

                });

    }

    public void installAPKNew(String downloadPath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            File file = new File(downloadPath);
            Uri uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mContext.startActivity(intent);
        } else {
            File file = new File(downloadPath);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }
}