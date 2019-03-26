package com.cloudwell.paywell.services.activity.notification;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends BaseActivity {

    public static final String IS_NOTIFICATION_SHOWN = "isNotificationShown";
    private LinearLayout mLinearLayout;
    private ListView listView;
    private AppHandler mAppHandler;
    private int position;

    public static int length;
    public static String[] mId = null;
    public static String[] mMsg = null;
    public static String[] mDate = null;
    public static String[] mTitle = null;
    public static String[] mImage = null;
    public static String[] mStatus = null;
    public static String[] mType = null;
    public static String[] mData = null;
    private int flag = 0;
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.home_notification);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mLinearLayout = findViewById(R.id.linearLayout);
        listView = findViewById(R.id.listViewNotification);
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        adapter = new MsgAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int msgPosition, long id) {
                position = msgPosition;
                flag = 1;
                new NotificationAsync().execute(getResources().getString(R.string.notif_url), mId[msgPosition]);
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
        if (flag == 1) {
            Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
            intent.putExtra(IS_NOTIFICATION_SHOWN, true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
            intent.putExtra(IS_NOTIFICATION_SHOWN, true);
            startActivity(intent);
            finish();
        }
    }

    @SuppressWarnings("deprecation")
    private class NotificationAsync extends AsyncTask<String, Intent, String> {


        @Override
        protected void onPreExecute() {

            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String notifications = null;

            try {
                HttpClient httpClients = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(params[0]);

                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("message_id", params[1]));
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
            dismissProgressDialog();
            if (result != null) {
                try {
                    mStatus[position] = "Read";
                    adapter.notifyDataSetChanged();
                    JSONObject jsonObject = new JSONObject(result);
                    NotificationFullViewActivity.TAG_NOTIFICATION_SOURCE = 1;
                    NotificationFullViewActivity.TAG_NOTIFICATION_POSITION = position;
                    startActivity(new Intent(NotificationActivity.this, NotificationFullViewActivity.class));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#8cc63f"));
                    snackbar.show();
                }
            }
        }
    }

    private class MsgAdapter extends BaseAdapter {

        private final Context mContext;

        private MsgAdapter(Context context) {
            mAppHandler = AppHandler.getmInstance(getApplicationContext());
            mContext = context;
        }

        @Override
        public int getCount() {
            return NotificationActivity.length;
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

            // Set the color here
            if (NotificationActivity.mStatus[position].equals("Unread")) {
                viewHolder.title.setTextColor(Color.parseColor("#ff0000"));
            } else {
                viewHolder.title.setTextColor(Color.parseColor("#355689"));
            }
            viewHolder.title.setText(NotificationActivity.mTitle[position]);
            viewHolder.date.setText(NotificationActivity.mDate[position]);
            viewHolder.msg.setText(NotificationActivity.mMsg[position]);
            if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
                viewHolder.title.setTypeface(AppController.getInstance().getOxygenLightFont());
                viewHolder.date.setTypeface(AppController.getInstance().getOxygenLightFont());
                viewHolder.msg.setTypeface(AppController.getInstance().getOxygenLightFont());
            } else {
                viewHolder.title.setTypeface(AppController.getInstance().getAponaLohitFont());
                viewHolder.date.setTypeface(AppController.getInstance().getAponaLohitFont());
                viewHolder.msg.setTypeface(AppController.getInstance().getAponaLohitFont());
            }
            return convertView;
        }

        private class ViewHolder {
            TextView title, date, msg;
        }
    }
}
