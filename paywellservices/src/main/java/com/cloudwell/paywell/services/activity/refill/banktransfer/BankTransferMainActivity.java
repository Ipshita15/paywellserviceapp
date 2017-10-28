package com.cloudwell.paywell.services.activity.refill.banktransfer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.refill.RefillBalanceMainActivity;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BankTransferMainActivity extends AppCompatActivity {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private CoordinatorLayout mCoordinateLayout;
    String bank_name = null;
    private static final String TAG_RESPONSE_STATUS = "status";
    private static final String TAG_BANK_NAME = "Bank_Name";
    private static final String TAG_ACCOUNT_NAME = "Account_Name";
    private static final String TAG_ACCOUNT_NO = "accountno";
    private static final String TAG_BRANCH = "branch";
    private static final String TAG_MESSAGE = "message";
    Cursor mCursor;
    String strImage = "";
    String bankName;
    String bankAccount;
    String bankNo;
    private static final int PERMISSION_FOR_GALLERY = 321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(R.string.home_refill_bank);
        mAppHandler = new AppHandler(this);
        mCoordinateLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);
        mCd = new ConnectionDetector(BankTransferMainActivity.this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BankTransferMainActivity.this, RefillBalanceMainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Button click handler on Main activity
     *
     * @param v
     */
    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.homeBtnBrac:
                bank_name = "BRAC";
                showInformation(bank_name);
                break;
            case R.id.homeBtnDbbl:
                bank_name = "DBBL";
                showInformation(bank_name);
                break;
            case R.id.homeBtnIbbl:
                bank_name = "IBBL";
                showInformation(bank_name);
                break;
            case R.id.homeBtnPbl:
                bank_name = "PBL";
                showInformation(bank_name);
                break;
            case R.id.homeBtnScb:
                bank_name = "SCB";
                showInformation(bank_name);
                break;
            default:
                break;
        }
    }

    public void showInformation(String bank_name) {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(getSupportFragmentManager());
        } else {
            new BankTransferMainActivity.InformationAsync().execute(
                    getResources().getString(R.string.refill_bank_info), bank_name);
        }
    }

    private class InformationAsync extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BankTransferMainActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("Bank_Name", params[1]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(TAG_RESPONSE_STATUS);

                if (status.equalsIgnoreCase("200")) {
                    bankName = jsonObject.getString(TAG_BANK_NAME);
                    bankAccount = jsonObject.getString(TAG_ACCOUNT_NAME);
                    bankNo = jsonObject.getString(TAG_ACCOUNT_NO);
                    String accountBranch = jsonObject.getString(TAG_BRANCH);

                    AlertDialog.Builder builder = new AlertDialog.Builder(BankTransferMainActivity.this);
                    builder.setTitle(bank_name + " Bank Details");
                    builder.setMessage("Bank Name: " + bankName + "\nAcc. Name: " + bankAccount + "\nAcc. No: " + bankNo + "\nBranch: " + accountBranch);

                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton("Upload", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            dialogInterface.dismiss();
                            uploadImg();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    TextView messageText = (TextView) alert.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);

                } else {
                    String message = jsonObject.getString(TAG_MESSAGE);
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, message, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public void uploadImg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BankTransferMainActivity.this);
        builder.setMessage(R.string.choose_image_for_upload_msg);
//        builder.setPositiveButton("ক্যামেরা থেকে", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int id) {
//
//                int PERMISSION_ALL = 1;
//                String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.UPDATE_DEVICE_STATS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.PACKAGE_USAGE_STATS};
//
//                if(!hasPermissions(BankTransferMainActivity.this, PERMISSIONS)){
//                    ActivityCompat.requestPermissions(BankTransferMainActivity.this, PERMISSIONS, PERMISSION_FOR_CAMERA);
//                } else{
//                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 00002);
//                }
//                dialogInterface.cancel();
//                /////////////
////                int permissionCheck = ContextCompat.checkSelfPermission(BankTransferMainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
////                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
////                    ActivityCompat.requestPermissions(
////                            BankTransferMainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
////                } else {
////                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 00002);
////
////                }
////                dialogInterface.cancel();
//            }
//        });
        builder.setNegativeButton(R.string.form_galary_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                int permissionCheck = ContextCompat.checkSelfPermission(BankTransferMainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            BankTransferMainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY
                    );
                } else {
                    startActivityForResult(
                            new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                            00001);
                }
//                String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.UPDATE_DEVICE_STATS, android.Manifest.permission.READ_EXTERNAL_STORAGE};
//
//                if(!hasPermissions(BankTransferMainActivity.this, PERMISSIONS)){
//                    ActivityCompat.requestPermissions(BankTransferMainActivity.this, PERMISSIONS, PERMISSION_FOR_GALLERY);
//                } else{
//                    startActivityForResult(
//                            new Intent(
//                                    Intent.ACTION_PICK,
//                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
//                            00001);
//                }
//                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            mCursor = null;
            if (requestCode == 00001) {
                mCursor = getContentResolver()
                        .query(data.getData(),
                                new String[]{android.provider.MediaStore.Images.ImageColumns._ID},
                                null, null, null);
            }
//            if (requestCode == 00002) {
//                mCursor = getContentResolver().query(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        new String[]{MediaStore.Images.ImageColumns._ID,
//                                MediaStore.Images.ImageColumns.DATE_TAKEN},
//                        null, null,
//                        MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
//
//            }
            processImage(false, mCursor);
            mCursor.close();
        }
        else {
            Snackbar snackbar = Snackbar.make(mCoordinateLayout, "error", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }


    private void processImage(boolean fromGallery, Cursor cursor) {
        Uri newuri = null;
        if (cursor.moveToFirst()) {
            String uristringpic = "content://media/external/images/media/" + cursor.getInt(0);
            newuri = Uri.parse(uristringpic);
        }
        cursor.close();

        long selectedImageUri = ContentUris.parseId(newuri);

        Bitmap bm1 = MediaStore.Images.Thumbnails.getThumbnail(
                this.getContentResolver(), selectedImageUri,
                MediaStore.Images.Thumbnails.MINI_KIND, null);

        Bitmap bm = Bitmap.createScaledBitmap(bm1, 600, 600, true);
        //Bitmap bm = Bitmap.createScaledBitmap(bm1, (int) (bm1.getWidth() * 0.8), (int) (bm1.getHeight() * 0.8), true);


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();

        String encodedImage = Base64.encodeToString(bitmapdata, Base64.DEFAULT).replaceAll("[\n\r]", "");

        strImage = encodedImage;
        uploadDepositInformation(bankName, bankNo);
    }

    public void uploadDepositInformation(String bankName, String bankAccountNo) {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(getSupportFragmentManager());
        } else {
            new BankTransferMainActivity.DepositInformationAsync().execute(
                    getResources().getString(R.string.refill_bank_deposit), mAppHandler.getImeiNo(), bankName, bankAccountNo, strImage);
        }
    }

    private class DepositInformationAsync extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BankTransferMainActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("imei", params[1]));
                nameValuePairs.add(new BasicNameValuePair("bank", params[2]));
                nameValuePairs.add(new BasicNameValuePair("account", params[3]));
                nameValuePairs.add(new BasicNameValuePair("depositslip", params[4]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            try {
                JSONObject jsonObject = new JSONObject(result);

                String message = jsonObject.getString(TAG_MESSAGE);

                AlertDialog.Builder builder = new AlertDialog.Builder(BankTransferMainActivity.this);
                builder.setTitle("Status");
                builder.setMessage(message);
                builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.dismiss();
                        startActivity(new Intent(BankTransferMainActivity.this, RefillBalanceMainActivity.class));
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                TextView messageText = (TextView) alert.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_FOR_GALLERY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    startActivityForResult(
                            new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                            00001);
                } else {
                    // permission denied
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.access_denied_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                return;
            }
//            case PERMISSION_FOR_CAMERA: {
//
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted
//                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 00002);
//                } else {
//                    // permission denied
//                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, "Permission denied", Snackbar.LENGTH_LONG);
//                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
//                    View snackBarView = snackbar.getView();
//                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
//                    snackbar.show();
//                }
//                return;
//            }
        }
    }

}
