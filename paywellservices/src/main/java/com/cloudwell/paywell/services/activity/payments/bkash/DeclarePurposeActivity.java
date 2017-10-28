package com.cloudwell.paywell.services.activity.payments.bkash;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DeclarePurposeActivity extends AppCompatActivity {

    //JSON Node Names
    private static final String TAG_STATUS = "Status";
    private static final String TAG_TRX_DETAILS = "Trx Details";
    private static final String TAG_ID = "id";
    private static final String TAG_AMOUNT = "amount";
    private static final String TAG_DATETIME = "datetime";
    private static final String TAG_PURPOSE_TYPE = "purpose_type";
    private static final String TAG_TITLE = "title";
    private static final String TAG_MESSAGE = "Message";
    public static final String JSON_RESPONSE = "jsonResponse";

    private AppHandler mAppHandler;
    private ConnectionDetector cd;
    private static RelativeLayout mRelativeLayout;
    ListView listView;
    CustomAdapter adapter;
    private static List<String> mPurposeTypeList;
    int trx_length;
    public static String[] mAmount = null;
    public static String[] mId = null;
    public static String[] mDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declare_purpose);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_bkash_declare_purpose_title);

        initialize();
    }

    private void initialize() {
        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
        adapter = new CustomAdapter(this);

        String jsonResponse = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jsonResponse = bundle.getString(JSON_RESPONSE);
        }
        listView = (ListView) findViewById(R.id.listView);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        mPurposeTypeList = new ArrayList<>();

        trx_length = 0;
        mAmount = new String[jsonResponse.length()];
        mId = new String[jsonResponse.length()];
        mDate = new String[jsonResponse.length()];

        try {
            JSONObject jsonObj = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_TRX_DETAILS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                String id = jo.getString(TAG_ID);
                String amount = jo.getString(TAG_AMOUNT);
                String datetime = jo.getString(TAG_DATETIME);

                mId[i] = id;
                mAmount[i] = amount;
                mDate[i] = datetime;
                trx_length++;
            }
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ShowPurposeDialog(mId[position], mAmount[position]);
                }
            });
            JSONArray ja = jsonObj.getJSONArray(TAG_PURPOSE_TYPE);
            for (int j = 0; j < ja.length(); j++) {
                JSONObject jo = ja.getJSONObject(j);
                String title = jo.getString(TAG_TITLE);
                mPurposeTypeList.add(title);
            }
            mPurposeTypeList.add("Other");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class CustomAdapter extends BaseAdapter {

        private final Context mContext;

        public CustomAdapter(Context context) {
            mAppHandler = new AppHandler(context);
            mContext = context;
        }

        @Override
        public int getCount() {
            return trx_length;
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.purpose_declare_list_row, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
                viewHolder.datetime = (TextView) convertView.findViewById(R.id.dateTime);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.amount.setText(getString(R.string.tk_des) + " " + mAmount[position]);
            viewHolder.datetime.setText(getString(R.string.date_and_time_des) + " " + mDate[position]);

            return convertView;
        }


        private class ViewHolder {
            TextView amount;
            TextView datetime;
        }
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
        Intent intent = new Intent(DeclarePurposeActivity.this, BKashMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void ShowPurposeDialog(final String itemId, final String amount) {
        final int pListSize = mPurposeTypeList.size();
        final String[] purposeTypes = new String[pListSize];
        for (int i = 0; i < pListSize; i++) {
            purposeTypes[i] = mPurposeTypeList.get(i).toString();
        }

        // creating the EditText widget programmatically
        final EditText editText = new EditText(DeclarePurposeActivity.this);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setTypeface(AppController.getInstance().getRobotoRegularFont());
        editText.setVisibility(View.GONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.purpose_type_msg)
                .setSingleChoiceItems(purposeTypes, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String selectItem = purposeTypes[arg1].toString();
                        if (selectItem.equalsIgnoreCase("Other")) {
                            editText.setVisibility(View.VISIBLE);
                        } else {
                            editText.setVisibility(View.GONE);
                        }
                    }

                });
        builder.setView(editText);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                String selectedItem = purposeTypes[selectedPosition];
                if (selectedItem.equalsIgnoreCase("Other")) {
                    selectedItem = editText.getText().toString().trim();
                    if (selectedItem == null) selectedItem = "";
                }
                if (!cd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    try {
                        new PDAsyncTask().execute(getResources().getString(R.string.bkash_pd_url),
                                "imei=" + mAppHandler.getImeiNo(),
                                "&pin=" + URLEncoder.encode(mAppHandler.getPin(), "UTF-8"),
                                "&id=" + itemId,
                                "&purpose_name=" + URLEncoder.encode(selectedItem, "UTF-8"),
                                "&amount=" + URLEncoder.encode(amount, "UTF-8"),
                                "&format=json");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        // set the focus change listener of the EditText
        // this part will make the soft keyboard automatically visible
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        dialog.show();
    }

    private class PDAsyncTask extends AsyncTask<String, String, JSONObject> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(DeclarePurposeActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            String url = params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6];
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressDialog.cancel();
            try {
                String status = jsonObject.getString(TAG_STATUS);
                String message = jsonObject.getString(TAG_MESSAGE);
                if (status.equalsIgnoreCase("502")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DeclarePurposeActivity.this);
                    builder.setTitle("Result");
                    builder.setMessage(message);
                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            Intent intent = new Intent(DeclarePurposeActivity.this, BKashMenuActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.setCancelable(true);
                    AlertDialog alert = builder.create();
                    alert.setCanceledOnTouchOutside(true);
                    alert.show();
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, message, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
