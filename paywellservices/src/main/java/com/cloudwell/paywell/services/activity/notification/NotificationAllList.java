package com.cloudwell.paywell.services.activity.notification;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.app.AppHandler;
import com.squareup.picasso.Picasso;

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

/**
 * Created by Naima Gani on 12/5/2016.
 */

public class NotificationAllList extends AppCompatActivity {

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

    public static int length;
    public static String[] mMsg = null;
    public static String[] mDate = null;
    public static String[] mTitle = null;
    public static String[] mImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);
        getSupportActionBar().setTitle(R.string.home_notification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.ListView);
        mLinearLayout = (LinearLayout) findViewById(R.id.mLinearLayout);
        mAppHandler = new AppHandler(this);

        initializer();
    }

    public void initializer() {
        new NotificationAllList.NotificationAllListAsync().execute(getResources().getString(R.string.notif_url));
    }

    private class NotificationAllListAsync extends AsyncTask<String, Intent, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(NotificationAllList.this, "", getString(R.string.loading_msg), true);
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
                        mTitle = new String[result.length()];
                        mMsg = new String[result.length()];
                        mDate = new String[result.length()];
                        mImage = new String[result.length()];
                        length = 0;

                        JSONArray jsonArray = jsonObject.getJSONArray(TAG_RESPONSE_MSG_ARRAY);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String msg_title = object.getString(TAG_RESPONSE_MSG_SUBJECT);
                            String msg = object.getString(TAG_RESPONSE_MESSAGE);
                            String date = object.getString(TAG_RESPONSE_DATE);
                            String image;
                            if(!object.getString(TAG_RESPONSE_IMAGE).isEmpty()) {
                                image = object.getString(TAG_RESPONSE_IMAGE);
                            } else {
                                image = "empty";
                            }
                            length++;
                            mTitle[i] = msg_title;
                            mMsg[i] = msg;
                            mDate[i] = date;
                            mImage[i] = image;
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
                }
            } else {
            }
        }
    }


    public void createList() {

        MsgAdapter adapter = new MsgAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationAllList.this);
                builder.setTitle(mTitle[position]);
                builder.setMessage(mMsg[position]);
                String image = mImage[position].toString();
                if(!image.equalsIgnoreCase("empty")) {
                    final ImageView imageView = new ImageView(NotificationAllList.this);
                    Picasso.with(NotificationAllList.this).load(mImage[position]).into(imageView);
                    builder.setView(imageView);
                }
                builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                TextView messageText = (TextView) alert.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);
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
        Intent intent = new Intent(NotificationAllList.this, MainActivity.class);
        intent.putExtra(IS_NOTIFICATION_SHOWN, true);
        startActivity(intent);
        finish();
    }

    public class MsgAdapter extends BaseAdapter {

        private final Context mContext;

        public MsgAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() { return NotificationAllList.length; }

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
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.date = (TextView) convertView.findViewById(R.id.date);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(NotificationAllList.mTitle[position]);
            viewHolder.date.setText(NotificationAllList.mDate[position]);

            return convertView;
        }


        private class ViewHolder {
            TextView title;
            TextView date;
        }
    }

}
