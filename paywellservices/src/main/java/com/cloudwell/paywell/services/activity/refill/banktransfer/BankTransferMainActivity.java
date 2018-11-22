package com.cloudwell.paywell.services.activity.refill.banktransfer;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.refill.RefillBalanceMainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class BankTransferMainActivity extends BaseActivity {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private CoordinatorLayout mCoordinateLayout;
    private String bank_name = null;
    private static final String TAG_RESPONSE_STATUS = "status";
    private static final String TAG_BANK_NAME = "Bank_Name";
    private static final String TAG_ACCOUNT_NAME = "Account_Name";
    private static final String TAG_ACCOUNT_NO = "accountno";
    private static final String TAG_BRANCH = "branch";
    private static final String TAG_MESSAGE = "message";
    private Cursor mCursor;
    private String strImage = "";
    private String bankName;
    private String bankAccount;
    private String bankNo;
    private static final int PERMISSION_FOR_GALLERY = 321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(R.string.home_refill_bank);
        mAppHandler = new AppHandler(this);
        mCoordinateLayout = findViewById(R.id.coordinateLayout);
        mCd = new ConnectionDetector(AppController.getContext());

        TextView textView = findViewById(R.id.detailsText);
        Button btnBrac = findViewById(R.id.homeBtnBrac);
        Button btnDbbl = findViewById(R.id.homeBtnDbbl);
        Button btnIbbl = findViewById(R.id.homeBtnIbbl);
        Button btnPbl = findViewById(R.id.homeBtnPbl);
        Button btnScb = findViewById(R.id.homeBtnScb);
        Button btnCity = findViewById(R.id.homeBtnCity);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            textView.setTypeface(AppController.getInstance().getOxygenLightFont());

            btnBrac.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnDbbl.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnIbbl.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnPbl.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnScb.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnCity.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            textView.setTypeface(AppController.getInstance().getAponaLohitFont());

            btnBrac.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnDbbl.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnIbbl.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnPbl.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnScb.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnCity.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
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
            case R.id.homeBtnCity:
                bank_name = "City";
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


        @Override
        protected void onPreExecute() {

            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                nameValuePairs.add(new BasicNameValuePair("Bank_Name", params[1]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
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
                    TextView messageText = alert.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);
                } else {
                    String message = jsonObject.getString(TAG_MESSAGE);
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, message, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    public void uploadImg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BankTransferMainActivity.this);
        builder.setMessage(R.string.choose_image_for_upload_msg);
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
            processImage(false, mCursor);
            mCursor.close();
        } else {
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
            new DepositInformationAsync().execute(
                    getResources().getString(R.string.refill_bank_deposit), mAppHandler.getImeiNo(), bankName, bankAccountNo, strImage);
        }
    }

    private class DepositInformationAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("imei", params[1]));
                nameValuePairs.add(new BasicNameValuePair("bank", params[2]));
                nameValuePairs.add(new BasicNameValuePair("account", params[3]));
                nameValuePairs.add(new BasicNameValuePair("depositslip", params[4]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
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
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
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
            }
        }
    }
}
