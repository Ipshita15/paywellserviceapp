package com.cloudwell.paywell.services.activity.topup.offer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.topup.OperatorMenuActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

public class ConfirmOfferActivity extends AppCompatActivity implements View.OnClickListener {

    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    static String amount;
    static String retCom;
    static String details;
    static String operatorName;
    String mPhn, mPin;
    EditText editTextPhn, editTextPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_offer);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(operatorName);
        }
        mAppHandler = new AppHandler(this);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        TextView textView = (TextView) findViewById(R.id.tvDetails);
        textView.setText(getString(R.string.amount_des) + " " + amount + getString(R.string.tk)
                + "\n" + getString(R.string.ret_com_des) + " " + retCom + getString(R.string.tk)
                + "\n" + getString(R.string.offer_details_des) + " " + details);

        editTextPhn = (EditText) findViewById(R.id.etPhn);
        editTextPin = (EditText) findViewById(R.id.etPin);
        Button submitBtn = (Button) findViewById(R.id.confirmBtn);
        submitBtn.setOnClickListener(this);
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
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirmBtn) {
            mPhn = editTextPhn.getText().toString().trim();
            mPin = editTextPin.getText().toString().trim();
            if(mPhn.length() != 11) {
                editTextPhn.setError(getString(R.string.phone_no_error_msg));
            } else if(mPin.isEmpty()) {
                editTextPin.setError(getString(R.string.pin_no_error_msg));
            } else {
                new TopupAsync().execute(getResources().getString(R.string.single_top)
                        + "iemi_no=" + mAppHandler.getImeiNo()
                        + "&msisdn=" + mPhn
                        + "&amount=" + amount
                        + "&con_type=" + "prepaid"
                        + "&pin_code=" + mPin);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private class TopupAsync extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ConfirmOfferActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();

        }

        @Override
        protected String doInBackground(String... data) {
            String responseTxt = null;
            HttpGet request = new HttpGet(data[0]);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            try {
                responseTxt = client.execute(request, responseHandler);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (result != null) {
                String[] splitSingleAt = result.split("@");
                if (result.startsWith("200")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmOfferActivity.this);
                    StringBuilder receiptBuilder = new StringBuilder();

                    receiptBuilder.append(getString(R.string.phone_no_des) + " " + mPhn
                            + "\n" + getString(R.string.amount_des) + " " + amount + getString(R.string.tk)
                            + "\n" + getString(R.string.trx_id_des) + " " + splitSingleAt[2]
                            + "\n\n" + getString(R.string.using_paywell_des)
                            + "\n" + getString(R.string.hotline_des) + " " + splitSingleAt[5]);
                    if (splitSingleAt[0].equals("200")) {
                        builder.setTitle(Html.fromHtml("<font color='#008000'>Result Successful</font>"));
                    } else {
                        builder.setTitle(Html.fromHtml("<font color='#ff0000'>Result Failed</font>"));
                    }

                    builder.setMessage(receiptBuilder.toString());
                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            dialogInterface.dismiss();
                            startActivity(new Intent(ConfirmOfferActivity.this, OperatorMenuActivity.class));
                            finish();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, splitSingleAt[1], Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }

        }
    }
}
