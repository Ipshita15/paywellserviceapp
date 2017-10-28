package com.cloudwell.paywell.services.activity.notification;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2/11/2016.
 */
public class NotificationActivity extends AppCompatActivity {

    public static final String IS_NOTIFICATION_SHOWN = "isNotificationShown";
    private ListView listView;
    private AppHandler mAppHandler;

    public static int length;
    public static String[] mId = null;
    public static String[] mMsg = null;
    public static String[] mDate = null;
    public static String[] mTitle = null;
    public static String[] mImage = null;
    public static String[] mStatus = null;
    int flag = 0;
    int position_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);
        getSupportActionBar().setTitle(R.string.home_notification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.ListView);
        mAppHandler = new AppHandler(this);

        final MsgAdapter adapter = new MsgAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                position_status = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
                builder.setTitle(mTitle[position]);
                builder.setMessage(mMsg[position]);

                if(!mImage[position].equals("empty")) {
                    final ImageView imageView = new ImageView(NotificationActivity.this);
                    Picasso.with(NotificationActivity.this).load(mImage[position]).into(imageView);
                    builder.setView(imageView);
                }

                builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.cancel();
                        mStatus[position] = "Read";
                        adapter.notifyDataSetChanged();
                        flag = 1;
                    }
                });
                AlertDialog alert = builder.create();
                if (!isFinishing()) {
                    new NotificationAsync().execute(getResources().getString(R.string.notif_url), mId[position]);
                    alert.show();
                }
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
        if(flag == 1) {
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
        protected String doInBackground(String... params) {
            String notifications = null;

            try {
                HttpClient httpClients = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(params[0]);

                List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("message_id", params[1]));
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

            if (result != null) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                } catch (Exception ex) {
                }
            }
        }
    }

    private class MsgAdapter extends BaseAdapter {

        private final Context mContext;

        private MsgAdapter(Context context) {
            mAppHandler = new AppHandler(context);
            mContext = context;
        }

        @Override
        public int getCount() { return NotificationActivity.length; }

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

            // Set the color here
            if (NotificationActivity.mStatus[position].equals("Unread")) {
                viewHolder.title.setTextColor(Color.parseColor("#ff0000"));
            } else {
                viewHolder.title.setTextColor(Color.parseColor("#0d0d0d"));
            }
            viewHolder.title.setText(NotificationActivity.mTitle[position]);
            viewHolder.date.setText(NotificationActivity.mDate[position]);

            return convertView;
        }


        private class ViewHolder {
            TextView title;
            TextView date;
        }
    }
}
