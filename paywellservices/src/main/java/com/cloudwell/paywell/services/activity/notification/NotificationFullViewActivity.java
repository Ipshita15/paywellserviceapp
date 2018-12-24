package com.cloudwell.paywell.services.activity.notification;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationFullViewActivity extends BaseActivity implements View.OnClickListener {

    public static int TAG_NOTIFICATION_SOURCE;
    public static int TAG_NOTIFICATION_POSITION;
    private static final String TAG_RESPONSE_STATUS = "status";

    private LinearLayout mLinearLayout;
    private TextView mTextViewTitle, mTextViewMsg;
    private ImageView mImageView;
    private EditText mEditText;
    private Button mBtn;

    private AppHandler mAppHandler;

    boolean isNotificationFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_full_view);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.home_notification_details);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mAppHandler = new AppHandler(this);
        initializer();

        isNotificationFlow = getIntent().getBooleanExtra("isNotificationFlow", false);

        if (isNotificationFlow) {
            handleNotificationClickFlow(getIntent().getStringExtra("Notification_Details"));
        } else {
            handleNormalFlow();
        }
    }

    private void handleNotificationClickFlow(String notifcation_details) {

        try {
            JSONObject jsonObject = new JSONObject(notifcation_details);
            int message_id = jsonObject.getInt("message_id");
            String title = jsonObject.getString("title");
            String message = jsonObject.getString("message");
            final String image = jsonObject.getString("image");


            mTextViewTitle.setText(title);
            mTextViewMsg.setText(message);

            if (!image.equals("")) {
                showProgressDialog();
                mImageView.setVisibility(View.VISIBLE);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                Picasso.get()
                        .load(image)
                        .resize(width, 0)
                        .into(mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Exception e) {
                        dismissProgressDialog();
                    }
                });

                mImageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        ImageViewActivity.TAG_IMAGE_URL = image;
                        startActivity(new Intent(NotificationFullViewActivity.this, ImageViewActivity.class));
                        return false;
                    }
                });
            }
            callNotificationReadAPI("" + message_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void handleNormalFlow() {
        if (TAG_NOTIFICATION_SOURCE == 1) {
            final SpannableString spannableString = new SpannableString(NotificationAllActivity.mMsg[TAG_NOTIFICATION_POSITION]);
            Linkify.addLinks(spannableString, Linkify.WEB_URLS);

            mTextViewTitle.setText(NotificationAllActivity.mTitle[TAG_NOTIFICATION_POSITION]);
            mTextViewMsg.setText(spannableString);
            if (!NotificationAllActivity.mImage[TAG_NOTIFICATION_POSITION].equalsIgnoreCase("empty")) {
                showProgressDialog();
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                Picasso.get()
                        .load(NotificationAllActivity.mImage[TAG_NOTIFICATION_POSITION])
                        .resize(width, 0)
                        .into(mImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                dismissProgressDialog();
                            }

                            @Override
                            public void onError(Exception e) {
                                dismissProgressDialog();
                            }
                        });
                mImageView.setVisibility(View.VISIBLE);
                mImageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        ImageViewActivity.TAG_IMAGE_URL = NotificationAllActivity.mImage[TAG_NOTIFICATION_POSITION];
                        startActivity(new Intent(NotificationFullViewActivity.this, ImageViewActivity.class));
                        return false;
                    }
                });
            }

//            Display display = getWindowManager().getDefaultDisplay();
//            Point size = new Point(); display.getSize(size); int width = size.x; //.resize(width, 0)

            if (NotificationAllActivity.mType[TAG_NOTIFICATION_POSITION].equalsIgnoreCase("BalanceReturnPwl")) {
                mEditText.setVisibility(View.VISIBLE);
                mBtn.setVisibility(View.VISIBLE);
            }
        }
    }

    public void initializer() {
        mLinearLayout = findViewById(R.id.linearLayoutNotiFullView);
        mTextViewTitle = findViewById(R.id.notiTitle);
        mTextViewMsg = findViewById(R.id.notiMessage);
        mImageView = findViewById(R.id.notiImg);
        mEditText = findViewById(R.id.notiEditText);
        mBtn = findViewById(R.id.notiButton);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            mTextViewTitle.setTypeface(AppController.getInstance().getOxygenLightFont());
            mTextViewMsg.setTypeface(AppController.getInstance().getOxygenLightFont());
            mEditText.setTypeface(AppController.getInstance().getOxygenLightFont());
            mBtn.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            mTextViewTitle.setTypeface(AppController.getInstance().getAponaLohitFont());
            mTextViewMsg.setTypeface(AppController.getInstance().getAponaLohitFont());
            mEditText.setTypeface(AppController.getInstance().getAponaLohitFont());
            mBtn.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
        mBtn.setOnClickListener(NotificationFullViewActivity.this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.notiButton) {
            if (!mEditText.getText().toString().isEmpty()) {
                if (TAG_NOTIFICATION_SOURCE == 1) {
                    String param = NotificationAllActivity.mData[TAG_NOTIFICATION_POSITION];
                    new PinRequestAsync().execute(
                            getResources().getString(R.string.merchant_balance_return_url)
                                    + "?messageId=" + NotificationAllActivity.mId[TAG_NOTIFICATION_POSITION]
                                    + "&merchentPassword=" + mEditText.getText().toString()
                                    + "&" + param.replace("@", "&"));
                } else {
                    String param = NotificationAllActivity.mData[TAG_NOTIFICATION_POSITION];
                    new PinRequestAsync().execute(
                            getResources().getString(R.string.merchant_balance_return_url)
                                    + "?messageId=" + NotificationAllActivity.mId[TAG_NOTIFICATION_POSITION]
                                    + "&merchentPassword=" + mEditText.getText().toString()
                                    + "&" + param.replace("@", "&"));
                }
            } else {
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    private class PinRequestAsync extends AsyncTask<String, String, String> {


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
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#8cc63f"));
                snackbar.show();
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("Status");
                    String message = jsonObject.getString("StatusName");
                    showTransferMessage(status, message);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#8cc63f"));
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#8cc63f"));
                snackbar.show();
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void showTransferMessage(String status_code, String message) {
        String mStatus = status_code;
        String mMessage = message;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (mStatus.equalsIgnoreCase("200")) {
            builder.setTitle(Html.fromHtml("<font color='#66cc00'>" + getString(R.string.success_msg) + "</font>"));
            builder.setMessage(getString(R.string.amount_transfer_msg));
        } else {
            builder.setTitle(Html.fromHtml("<font color='#e62e00'>" + getString(R.string.request_failed_msg) + "</font>"));
            builder.setMessage(mMessage);
        }

        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isNotificationFlow) {
            Intent intent = new Intent(getApplicationContext(), NotificationAllActivity.class);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

    public void callNotificationReadAPI(String messageId) {
        showProgressDialog();

        AndroidNetworking.post(getResources().getString(R.string.notif_url))
                .addBodyParameter("username", mAppHandler.getImeiNo())
                .addBodyParameter("message_id", messageId)
                .addBodyParameter("format", "json")
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String status = jsonObject.getString(TAG_RESPONSE_STATUS);
                    if (status.equalsIgnoreCase("200")) {

                    } else {
                        Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.no_notification_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#8cc63f"));
                    snackbar.show();
                }
            }

            @Override
            public void onError(ANError anError) {
                dismissProgressDialog();
            }
        });
    }
}
