package com.cloudwell.paywell.services.activity.utility.qubee;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

/**
 * Created by Android on 12/8/2015.
 */
@SuppressWarnings("ALL")
public class ComplainAmountActivity extends AppCompatActivity implements View.OnClickListener {

    private ConnectionDetector cd;
    private AppHandler mAppHandler;
    private EditText mPin;
    private EditText mAccount;
    private EditText mIncorrectAmount;
    private EditText mCorrectAmount;
    private Button mConfirmAmount;
    private static String _returnMessage;
    private static String _trxId;
    private LinearLayout mLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qubee_complain_amount);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_utility_qubee_wrong_amount_title);

        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        TextView _pin = (TextView) findViewById(R.id.tvQbeePin4);
        _pin.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mPin = (EditText) findViewById(R.id.etQubeePin4);
        mPin.setTypeface(AppController.getInstance().getRobotoRegularFont());

        TextView _account = (TextView) findViewById(R.id.tvAccount);
        _account.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mAccount = (EditText) findViewById(R.id.etAccount);
        mAccount.setTypeface(AppController.getInstance().getRobotoRegularFont());

        TextView _incorrectAmount = (TextView) findViewById(R.id.tvIncorrectAmount);
        _incorrectAmount.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mIncorrectAmount = (EditText) findViewById(R.id.etIncorrectAmount);
        mIncorrectAmount.setTypeface(AppController.getInstance().getRobotoRegularFont());

        TextView _correctAmount = (TextView) findViewById(R.id.tvCorrectAmount);
        _correctAmount.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mCorrectAmount = (EditText) findViewById(R.id.etCorrectAmount);
        mCorrectAmount.setTypeface(AppController.getInstance().getRobotoRegularFont());

        mConfirmAmount = (Button) findViewById(R.id.btnComplainConfirm2);
        mConfirmAmount.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mConfirmAmount.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == mConfirmAmount) {
            if (!cd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                String _pin = mPin.getText().toString().trim();
                if (_pin.length() == 0) {
                    mPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
                    return;
                }
                String _account = mAccount.getText().toString().trim();
                if (_account.length() < 4) {
                    mAccount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.qubee_acc_error_msg) + "</font>"));
                    return;
                }
                String _incorrectAmount = mIncorrectAmount.getText().toString().trim();
                if (_incorrectAmount.length() < 1) {
                    mIncorrectAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
                    return;
                }
                String _correctAmount = mCorrectAmount.getText().toString().trim();
                if (_correctAmount.length() < 1) {
                    mCorrectAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
                    return;
                }

                new SubmitAsync().execute(getResources().getString(R.string.qb_com),
                        "iemi_no=" + mAppHandler.getImeiNo(),
                        "&pin_code=" + _pin,
                        "&wimax=" + AppHandler.KEY_WIMAX,
                        "&type=" + AppHandler.KEY_TYPE2,
                        "&account_no=" + _account,
                        "&correct_amount=" + _correctAmount,
                        "&wrong_amount=" + _incorrectAmount);
            }
        }
    }

    private class SubmitAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ComplainAmountActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6] + params[7]);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseTxt = null;
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            try {
                responseTxt = client.execute(request, responseHandler);
            } catch (ClientProtocolException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            try {
                if (result != null && result.contains("@")) {
                    String splitedArray[] = result.split("@");
                    if (splitedArray.length > 0) {
                        if (splitedArray[0].equalsIgnoreCase("200")) {
                            _returnMessage = splitedArray[1];
                            _trxId = splitedArray[2];
                            showStatusDialog();
                        } else {
                            Snackbar snackbar = Snackbar.make(mLinearLayout, splitedArray[1], Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                            snackbar.show();
                        }
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

    }


    private void showStatusDialog() {
        StringBuilder reqStrBuilder = new StringBuilder();
        reqStrBuilder.append(getString(R.string.message_des) + " " + _returnMessage
                + "\n" + getString(R.string.trx_id_des) + " " + _trxId);
        AlertDialog.Builder builder = new AlertDialog.Builder(ComplainAmountActivity.this);
        builder.setTitle("Result");
        builder.setMessage(reqStrBuilder.toString());
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                onBackPressed();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        TextView messageText = (TextView) alert.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (this != null) {
                this.onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ComplainAmountActivity.this, QubeeMainActivity.class);
        startActivity(intent);
        finish();
    }
}
