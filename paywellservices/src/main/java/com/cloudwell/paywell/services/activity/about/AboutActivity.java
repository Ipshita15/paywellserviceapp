package com.cloudwell.paywell.services.activity.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.AppLoadingActivity;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.nav_about);
        }
        initView();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_ABOUT_PAGE);
    }

    private void initView() {
        ListView mListView = findViewById(R.id.listView);
        SampleTextListAdapter adapter = new SampleTextListAdapter(this);
        mListView.setAdapter(adapter);
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
        Intent intent = new Intent(AboutActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class SampleTextListAdapter extends BaseAdapter {

        private final Context mContext;
        private final String[] sampleStrings;
        private AppHandler mAppHandler;

        private SampleTextListAdapter(Context context) {
            mContext = context;
            mAppHandler = AppHandler.getmInstance(getApplicationContext());
            sampleStrings = mContext.getResources().getStringArray(R.array.sampleStrings);
        }

        @Override
        public int getCount() {
            return sampleStrings.length;
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_about_text_view, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.titleView = convertView.findViewById(R.id.title);
                viewHolder.textView = convertView.findViewById(R.id.about_textView);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (mAppHandler.getAppLanguage().equalsIgnoreCase("bn")) {
                String title = "পেওয়েল সার্ভিস" + " v" + AppLoadingActivity.versionName;
                viewHolder.titleView.setText(title);
                viewHolder.titleView.setTypeface(AppController.getInstance().getAponaLohitFont());
                viewHolder.textView.setTypeface(AppController.getInstance().getAponaLohitFont());
            } else {
                String title = "PayWell Services" + " v" + AppLoadingActivity.versionName;
                viewHolder.titleView.setText(title);
                viewHolder.titleView.setTypeface(AppController.getInstance().getOxygenLightFont());
                viewHolder.textView.setTypeface(AppController.getInstance().getOxygenLightFont());
            }
            viewHolder.textView.setText(sampleStrings[position]);

            return convertView;
        }

        private class ViewHolder {
            TextView titleView, textView;
        }
    }
}
