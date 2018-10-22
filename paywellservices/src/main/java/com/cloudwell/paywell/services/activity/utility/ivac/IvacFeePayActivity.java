package com.cloudwell.paywell.services.activity.utility.ivac;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IvacFeePayActivity extends AppCompatActivity {

    public static String TAG_CENTER_DETAILS = "centerDetails";
    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private ConstraintLayout mConstraintLayout;
    private Spinner spnr_center;
    private ArrayAdapter<String> arrayAdapter_center;
    private ArrayList<String> center_id_array, center_name_array, center_amount_array;
    private String str_centerId = "", str_amount = "", password = "", webFile = "", passportNo = "", phnNum = "";
    private TextView textViewAmount;
    private EditText editTextPassword, editTextWebFile, editTextPassport, editTextPhnNum;

    private static String TAG_RESPONSE_IVAC_CENTER_ID = "center_id";
    private static String TAG_RESPONSE_IVAC_CENTER_NAME = "center_name";
    private static String TAG_RESPONSE_IVAC_CENTER_AMOUNT = "amount";

    private static final int PERMISSION_OPEN_CAMERA = 100;
    private int state = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivac_fee_pay);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_ivac_fee_pay);
        }

        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = new AppHandler(this);

        initializeData();
    }

    private void initializeData() {
        mConstraintLayout = findViewById(R.id.constrainLayout);
        spnr_center = findViewById(R.id.spinner_center);
        textViewAmount = findViewById(R.id.tvFeeAmount);
        editTextPassword = findViewById(R.id.etPassword);
        editTextWebFile = findViewById(R.id.etWebFile);
        editTextPassport = findViewById(R.id.etPassport);
        editTextPhnNum = findViewById(R.id.etPhnNum);

        editTextWebFile.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(editTextWebFile) {
            @Override
            public boolean onDrawableClick() {
                state = 1;
                if (ActivityCompat.checkSelfPermission(IvacFeePayActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // permission has not been granted.
                    ActivityCompat.requestPermissions(IvacFeePayActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_OPEN_CAMERA);
                } else {
                    //initiate scan with our custom scan activity
                    new IntentIntegrator(IvacFeePayActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
                }
                return true;
            }
        });

        editTextPassport.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(editTextPassport) {
            @Override
            public boolean onDrawableClick() {
                state = 2;
                if (ActivityCompat.checkSelfPermission(IvacFeePayActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // permission has not been granted.
                    ActivityCompat.requestPermissions(IvacFeePayActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_OPEN_CAMERA);
                } else {
                    new IntentIntegrator(IvacFeePayActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
                }
                return true;
            }
        });

        try {
            center_id_array = new ArrayList<>();
            center_name_array = new ArrayList<>();
            center_amount_array = new ArrayList<>();

            center_id_array.add("select one");
            center_name_array.add("Select One");
            center_amount_array.add("select one");
            JSONArray jsonArray = new JSONArray(TAG_CENTER_DETAILS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String center_id = object.getString(TAG_RESPONSE_IVAC_CENTER_ID);
                String center_name = object.getString(TAG_RESPONSE_IVAC_CENTER_NAME);
                String amount = object.getString(TAG_RESPONSE_IVAC_CENTER_AMOUNT);

                center_id_array.add(center_id);
                center_name_array.add(center_name);
                center_amount_array.add(amount);
            }

            arrayAdapter_center = new ArrayAdapter<>(IvacFeePayActivity.this, android.R.layout.simple_spinner_dropdown_item, center_name_array);
            spnr_center.setAdapter(arrayAdapter_center);
            spnr_center.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    try {
                        str_centerId = "";
                        str_centerId = center_id_array.get(position);
                        str_amount = center_amount_array.get(position);
                        String amountText = getString(R.string.tk_des) + " " + str_amount;
                        textViewAmount.setText(amountText);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_OPEN_CAMERA: {
                // Check if the only required permission has been granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Phone permission has been granted
                    new IntentIntegrator(IvacFeePayActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
                } else {
                    Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.access_denied_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                if (state == 1) {
                    editTextWebFile.setText("");
                    editTextWebFile.clearFocus();
                    editTextWebFile.setText(result.getContents());
                } else if (state == 2) {
                    editTextPassport.setText("");
                    editTextPassport.clearFocus();
                    editTextPassport.setText(result.getContents());
                }
            }
        }
    }

    public void confirmIvacOnClick(View view) {
        if (view.getId() == R.id.btnIvacSubmit) {
            String pass = editTextPassword.getText().toString();
            String spnr = spnr_center.getSelectedItem().toString();
            String web = editTextWebFile.getText().toString();
            String passport = editTextPassport.getText().toString();
            String phn = editTextPhnNum.getText().toString();
            if (pass.isEmpty()) {
                editTextPassword.setError(getString(R.string.pin_no_error_msg));
            } else if (spnr.equals("Select One")) {
                Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.center_error_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            } else if ((web.length() <= 3) || (!web.startsWith("BGD"))) {
                editTextWebFile.setError(getString(R.string.input_error_msg));
            } else if (passport.isEmpty()) {
                editTextPassport.setError(getString(R.string.input_error_msg));
            } else if (phn.length() != 11) {
                editTextPhnNum.setError(getString(R.string.phone_no_error_msg));
            } else {
                if (!mCd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    password = editTextPassword.getText().toString().trim();
                    webFile = editTextWebFile.getText().toString().trim();
                    passportNo = editTextPassport.getText().toString().trim();
                    phnNum = editTextPhnNum.getText().toString().trim();
                    new ConfirmFeePayAsync().execute(
                            getResources().getString(R.string.utility_ivac_fee_pay));
                }
            }
        }
    }

    private class ConfirmFeePayAsync extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(IvacFeePayActivity.this, "", getString(R.string.loading_msg), true);
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
                List<NameValuePair> nameValuePairs = new ArrayList<>(8);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("password", password));
                nameValuePairs.add(new BasicNameValuePair("amount", str_amount));
                nameValuePairs.add(new BasicNameValuePair("web_file_no", webFile));
                nameValuePairs.add(new BasicNameValuePair("passport_no", passportNo));
                nameValuePairs.add(new BasicNameValuePair("center_no", str_centerId));
                nameValuePairs.add(new BasicNameValuePair("cust_mobile", phnNum));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    final String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    String trx = jsonObject.getString("trx_id");

                    AlertDialog.Builder builder = new AlertDialog.Builder(IvacFeePayActivity.this);//ERROR ShowDialog cannot be resolved to a type
                    builder.setTitle("Result");
                    builder.setMessage(msg + "\n\n" + trx);

                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            onBackPressed();

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
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
        startActivity(new Intent(IvacFeePayActivity.this, IvacMainActivity.class));
        finish();
    }
}