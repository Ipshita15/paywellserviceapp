package com.cloudwell.paywell.services.activity.notification;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.app.AppHandler;

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

public class NotificationAllActivity extends AppCompatActivity {

    public static final String IS_NOTIFICATION_SHOWN = "isNotificationShown";
    private ListView listView;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;

    private static final String TAG_RESPONSE_STATUS = "status";
    private static final String TAG_RESPONSE_MSG_SUBJECT = "message_sub";
    private static final String TAG_RESPONSE_MSG_ID = "message_id";
    private static final String TAG_RESPONSE_MSG_ARRAY = "detail_message";
    private static final String TAG_RESPONSE_MESSAGE = "message";
    private static final String TAG_RESPONSE_IMAGE = "image_url";
    private static final String TAG_RESPONSE_DATE = "added_datetime";
    private static final String TAG_RESPONSE_TYPE = "type";
    private static final String TAG_RESPONSE_NOTIFICATION_BALANCE_RETURN_DATA = "balance_return_data";

    public static int length;
    public static String[] mId = null;
    public static String[] mMsg = null;
    public static String[] mDate = null;
    public static String[] mTitle = null;
    public static String[] mImage = null;
    public static String[] mStatus = null;
    public static String[] mType = null;
    public static String[] mData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.home_notification);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        listView = findViewById(R.id.listViewNotification);
        mLinearLayout = findViewById(R.id.linearLayout);
        mAppHandler = new AppHandler(this);
        initializer();
    }

    public void initializer() {
        new NotificationAllActivity.NotificationAllListAsync().execute(getResources().getString(R.string.notif_url));
    }

    private class NotificationAllListAsync extends AsyncTask<String, Intent, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(NotificationAllActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String notifications = null;

            try {
                HttpClient httpClients = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(params[0]);

                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("mes_type", "all_message"));
                nameValuePairs.add(new BasicNameValuePair("message_status", "all"));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                notifications = httpClients.execute(httpPost, responseHandler);
            } catch (Exception ex) {
                ex.printStackTrace();
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#8cc63f"));
                snackbar.show();
            }
            return notifications;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString(TAG_RESPONSE_STATUS);

                    if (status.equalsIgnoreCase("200")) {
                        mId = new String[result.length()];
                        mTitle = new String[result.length()];
                        mMsg = new String[result.length()];
                        mDate = new String[result.length()];
                        mImage = new String[result.length()];
                        mStatus = new String[result.length()];
                        mType = new String[result.length()];
                        mData = new String[result.length()];
                        length = 0;

                        JSONArray jsonArray = jsonObject.getJSONArray(TAG_RESPONSE_MSG_ARRAY);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String id = object.getString(TAG_RESPONSE_MSG_ID);
                            String msg_title = object.getString(TAG_RESPONSE_MSG_SUBJECT);
                            String msg = object.getString(TAG_RESPONSE_MESSAGE);
                            String date = object.getString(TAG_RESPONSE_DATE);
                            String msg_status = object.getString(TAG_RESPONSE_STATUS);
                            String type = object.getString(TAG_RESPONSE_TYPE);
                            String data = object.getString(TAG_RESPONSE_NOTIFICATION_BALANCE_RETURN_DATA);

                            String image;
                            if (!object.getString(TAG_RESPONSE_IMAGE).isEmpty()) {
                                image = object.getString(TAG_RESPONSE_IMAGE);
                            } else {
                                image = "empty";
                            }
                            length++;

                            mId[i] = id;
                            mTitle[i] = msg_title;
                            mMsg[i] = msg;
                            mDate[i] = date;
                            mImage[i] = image;
                            mStatus[i] = msg_status;
                            mType[i] = type;
                            mData[i] = data;
                        }
                        createList();
                    } else {
                        Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.no_notification_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } catch (Exception ex) {
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

    public void createList() {
        MsgAdapter adapter = new MsgAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                NotificationFullViewActivity.TAG_NOTIFICATION_SOURCE = 2;
                NotificationFullViewActivity.TAG_NOTIFICATION_POSITION = position;
                startActivity(new Intent(NotificationAllActivity.this, NotificationFullViewActivity.class));
            }
        });
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
        Intent intent = new Intent(NotificationAllActivity.this, MainActivity.class);
        intent.putExtra(IS_NOTIFICATION_SHOWN, true);
        startActivity(intent);
        finish();
    }

    public class MsgAdapter extends BaseAdapter {

        private final Context mContext;

        private MsgAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return NotificationAllActivity.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_notification, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.title = convertView.findViewById(R.id.title);
                viewHolder.date = convertView.findViewById(R.id.date);
                viewHolder.msg = convertView.findViewById(R.id.message);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(NotificationAllActivity.mTitle[position]);
            viewHolder.date.setText(NotificationAllActivity.mDate[position]);
            viewHolder.msg.setText(NotificationAllActivity.mMsg[position]);

            return convertView;
        }


        private class ViewHolder {
            TextView title, date, msg;
        }
    }
}
