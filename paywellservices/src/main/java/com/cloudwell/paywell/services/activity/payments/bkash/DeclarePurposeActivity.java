package com.cloudwell.paywell.services.activity.payments.bkash;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.payments.PaymentsMainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeclarePurposeActivity extends AppCompatActivity {

    private String TAG_STATUS = "Status";
    private String TAG_TRX_DETAILS = "Trx Details";
    private String TAG_ID = "id";
    private String TAG_ACTUAL_AMOUNT = "actual_amount";
    private String TAG_AMOUNT = "amount";
    private String TAG_DATETIME = "datetime";
    private String TAG_PURPOSE_TYPE = "purpose_type";
    private String TAG_TITLE = "title";
    private String TAG_MESSAGE = "Message";
    public static String JSON_RESPONSE = "jsonResponse";

    private AppHandler mAppHandler;
    private ConnectionDetector cd;
    private RelativeLayout mRelativeLayout;
    ListView listView;
    CustomAdapter adapter;
    Cursor mCursor;
    private static List<String> mPurposeTypeList;
    int trx_length;
    String date = "";
    String encodedImage = "", mId, mAmount, mPurposeName, mSenderName, mSenderPhn, mSenderNid;
    Button btNidImage;

    private static final int PERMISSION_FOR_GALLERY = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declare_purpose);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_bkash_declare_purpose_title);

        initialize();
    }

    private void initialize() {
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = new AppHandler(this);
        adapter = new CustomAdapter(this);

        String jsonResponse = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jsonResponse = bundle.getString(JSON_RESPONSE);
        }
        listView = findViewById(R.id.listView);
        mRelativeLayout = findViewById(R.id.relativeLayout);
        mPurposeTypeList = new ArrayList<>();

        trx_length = 0;
        try {
            JSONObject jsonObj = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_TRX_DETAILS);
            for (int i = 0; i < jsonArray.length(); i++) {
                String result;
                JSONObject jo = jsonArray.getJSONObject(i);
                String id = jo.getString(TAG_ID);
                String actualAmount = jo.getString(TAG_ACTUAL_AMOUNT);
                String amount = jo.getString(TAG_AMOUNT);
                String datetime = jo.getString(TAG_DATETIME);

                String sub_date_comp = datetime.substring(0, 10);
                String sub_time = datetime.substring(10, datetime.length());

                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
                Date dateFormatedFirst = inputFormat.parse(sub_date_comp);
                String outputDateStr = outputFormat.format(dateFormatedFirst);

                trx_length++;

                if (!date.equals(outputDateStr)) {
                    date = outputDateStr;
                    adapter.addSectionHeaderItem(date);
                }

                result = actualAmount + "@" + id + "@" + amount
                        + "@" + sub_time;
                adapter.addItem(result);
            }
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (adapter.mData.get(position).contains("@")) {
                        ShowPurposeDialog(position);
                    }
                }
            });
            JSONArray ja = jsonObj.getJSONArray(TAG_PURPOSE_TYPE);
            for (int j = 0; j < ja.length(); j++) {
                JSONObject jo = ja.getJSONObject(j);
                String title = jo.getString(TAG_TITLE);
                mPurposeTypeList.add(title);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar.make(mRelativeLayout, getString(R.string.try_again_msg), Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }

    public class CustomAdapter extends BaseAdapter {

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;

        public ArrayList<String> mData = new ArrayList<>();
        private ArrayList<String> array = new ArrayList<>();
        private LayoutInflater mInflater;
        String splitArray_row_first[];
        String splitArray_row_second[];

        public CustomAdapter(Context context) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        private void addItem(final String item) {
            mData.add(item);
            array.add("data");
            notifyDataSetChanged();
        }

        private void addSectionHeaderItem(final String item) {
            mData.add(item);
            array.add("header");
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if (array.get(position).equals("header")) {
                return TYPE_SEPARATOR;
            } else {
                return TYPE_ITEM;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            int rowType;
            if (mData.get(position).contains("@")) {
                rowType = 0;
            } else {
                rowType = 1;
            }

            if (convertView == null) {
                holder = new ViewHolder();
                switch (rowType) {
                    case TYPE_SEPARATOR:
                        convertView = mInflater.inflate(R.layout.dialog_both_header, parent, false);
                        holder.textView = convertView.findViewById(R.id.header);
                        convertView.setTag(holder);
                        holder.textView.clearComposingText();
                        holder.textView.setText(mData.get(position));
                        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
                            holder.textView.setTypeface(AppController.getInstance().getOxygenLightFont());
                        } else {
                            holder.textView.setTypeface(AppController.getInstance().getAponaLohitFont());
                        }
                        break;
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.purpose_declare_list_row, parent, false);
                        holder.amount = convertView.findViewById(R.id.amount);
                        holder.time = convertView.findViewById(R.id.dateTime);

                        convertView.setTag(holder);

                        splitArray_row_first = mData.get(position).split("@");

                        String textAmount = getString(R.string.tk_des) + " " + splitArray_row_first[0];
                        String textTime = getString(R.string.time_des) + " " + splitArray_row_first[3];
                        holder.amount.setText(textAmount);
                        holder.time.setText(textTime);
                        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
                            holder.amount.setTypeface(AppController.getInstance().getOxygenLightFont());
                            holder.time.setTypeface(AppController.getInstance().getOxygenLightFont());
                        } else {
                            holder.amount.setTypeface(AppController.getInstance().getAponaLohitFont());
                            holder.time.setTypeface(AppController.getInstance().getAponaLohitFont());
                        }
                        break;
                }
            } else {
                holder = (ViewHolder) convertView.getTag();
                switch (rowType) {
                    case TYPE_SEPARATOR:
                        convertView = mInflater.inflate(R.layout.dialog_both_header, parent, false);
                        holder.textView = convertView.findViewById(R.id.header);
                        convertView.setTag(holder);
                        holder.textView.clearComposingText();
                        holder.textView.setText(mData.get(position));
                        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
                            holder.textView.setTypeface(AppController.getInstance().getOxygenLightFont());
                        } else {
                            holder.textView.setTypeface(AppController.getInstance().getAponaLohitFont());
                        }
                        break;
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.purpose_declare_list_row, parent, false);
                        holder.amount = convertView.findViewById(R.id.amount);
                        holder.time = convertView.findViewById(R.id.dateTime);

                        convertView.setTag(holder);

                        splitArray_row_second = mData.get(position).split("@");

                        String textAmount = getString(R.string.tk_des) + " " + splitArray_row_second[0];
                        String textTime = getString(R.string.time_des) + " " + splitArray_row_second[3];
                        holder.amount.setText(textAmount);
                        holder.time.setText(textTime);
                        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
                            holder.amount.setTypeface(AppController.getInstance().getOxygenLightFont());
                            holder.time.setTypeface(AppController.getInstance().getOxygenLightFont());
                        } else {
                            holder.amount.setTypeface(AppController.getInstance().getAponaLohitFont());
                            holder.time.setTypeface(AppController.getInstance().getAponaLohitFont());
                        }
                        break;
                }
            }
            return convertView;
        }

        public class ViewHolder {
            TextView textView, amount, time;
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
        if (trx_length > 0) {
            Intent intent = new Intent(DeclarePurposeActivity.this, PaymentsMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(DeclarePurposeActivity.this, BKashMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void ShowPurposeDialog(int position) {

        final int mPosition = position;
        final String array[] = adapter.mData.get(mPosition).split("@");
        final int pListSize = mPurposeTypeList.size();
        final String[] purposeTypes = new String[pListSize];
        for (int i = 0; i < pListSize; i++) {
            purposeTypes[i] = mPurposeTypeList.get(i);
        }

        mPurposeName = purposeTypes[0];

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 20, 20, 5);

        final EditText etSenderName = new EditText(this);
        etSenderName.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        etSenderName.setHint("Sender Name");
        etSenderName.setRawInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(etSenderName);

        final EditText etSenderPhn = new EditText(this);
        etSenderPhn.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        etSenderPhn.setHint("Sender Phone Number");
        etSenderPhn.setRawInputType(InputType.TYPE_CLASS_NUMBER); //for decimal numbers
        InputFilter[] FilterArrayPhn = new InputFilter[1];
        FilterArrayPhn[0] = new InputFilter.LengthFilter(11);
        etSenderPhn.setFilters(FilterArrayPhn);
        layout.addView(etSenderPhn);

        final EditText etSenderNid = new EditText(this);
        etSenderNid.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etSenderNid.setHint("Sender NID Number");
        etSenderNid.setRawInputType(InputType.TYPE_CLASS_NUMBER); //for decimal numbers
        layout.addView(etSenderNid);

        btNidImage = new Button(this);
        btNidImage.setText("NID Image");
        btNidImage.setTextColor(Color.parseColor("#ffffff"));
        btNidImage.setBackgroundColor(Color.parseColor("#5aac40"));
        btNidImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheckGallery = ContextCompat.checkSelfPermission(DeclarePurposeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheckGallery != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            DeclarePurposeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                } else {
                    galleryIntent();
                }
            }
        });
        layout.addView(btNidImage);

        final AlertDialog alertDialog = new AlertDialog.Builder(DeclarePurposeActivity.this)
                .setView(layout)
                .setPositiveButton(R.string.okay_btn, null)
                .setNegativeButton(R.string.cancel_btn, null)
                .setTitle(R.string.purpose_type_msg)
                .setSingleChoiceItems(purposeTypes, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        mPurposeName = purposeTypes[arg1];
                    }

                })
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {

                Button buttonPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (!cd.isConnectingToInternet()) {
                            AppHandler.showDialog(getSupportFragmentManager());
                        } else {
                            try {
                                if (!etSenderName.getText().toString().isEmpty()
                                        && etSenderPhn.getText().toString().length() == 11) {
                                    mId = array[1];
                                    mAmount = array[2];
                                    mSenderName = etSenderName.getText().toString().trim();

                                    try {
                                        mSenderPhn = etSenderPhn.getText().toString().trim();
                                    } catch (Exception ex) {
                                        etSenderPhn.setError(getString(R.string.phone_no_error_msg));
                                        return;
                                    }
                                    if (etSenderNid.getText().toString().isEmpty()) {
                                        mSenderNid = "";
                                    } else {
                                        if (etSenderNid.getText().toString().length() == 10
                                                || etSenderNid.getText().toString().length() == 13
                                                || etSenderNid.getText().toString().length() == 17) {
                                            mSenderNid = etSenderNid.getText().toString().trim();
                                        } else {
                                            etSenderNid.setError(getString(R.string.nid_input_error_msg));
                                            return;
                                        }
                                    }
                                    dialog.dismiss();
                                    new PDAsyncTask().execute(getResources().getString(R.string.bkash_pd_url));
                                } else {
                                    if (etSenderName.getText().toString().isEmpty()) {
                                        etSenderName.setError(getString(R.string.ticket_name_error_msg));
                                    } else {
                                        etSenderPhn.setError(getString(R.string.phone_no_error_msg));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Snackbar snackbar = Snackbar.make(mRelativeLayout, getString(R.string.try_again_msg), Snackbar.LENGTH_LONG);
                                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                                snackbar.show();
                            }
                        }

                    }
                });

                Button buttonNegative = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonNegative.setOnClickListener(new View.OnClickListener()

                {

                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_FOR_GALLERY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    galleryIntent();
                } else {
                    // permission denied
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, getString(R.string.access_denied_msg), Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    ActivityCompat.requestPermissions(
                            DeclarePurposeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                }
            }
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 111);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mCursor = null;
            if (requestCode == 111) {
                InputStream imageStream;
                try {
                    imageStream = this.getContentResolver().openInputStream(data.getData());
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    encodeTobase64(yourSelectedImage);
                    btNidImage.setText("Selected");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    btNidImage.setText("Not Selected");
                }
            }
        }
    }

    public void encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        encodedImage = imageEncoded;
    }

    private class PDAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(DeclarePurposeActivity.this, "", getString(R.string.loading_msg), true);
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
                List<NameValuePair> nameValuePairs = new ArrayList<>(11);
                nameValuePairs.add(new BasicNameValuePair("imei", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("pin", mAppHandler.getPin()));
                nameValuePairs.add(new BasicNameValuePair("id", mId));
                nameValuePairs.add(new BasicNameValuePair("purpose_name", URLEncoder.encode(mPurposeName, "UTF-8")));
                nameValuePairs.add(new BasicNameValuePair("amount", mAmount));
                nameValuePairs.add(new BasicNameValuePair("senderName", URLEncoder.encode(mSenderName, "UTF-8")));
                nameValuePairs.add(new BasicNameValuePair("senderPhn", URLEncoder.encode(mSenderPhn, "UTF-8")));
                nameValuePairs.add(new BasicNameValuePair("senderNid", URLEncoder.encode(mSenderNid, "UTF-8")));
                nameValuePairs.add(new BasicNameValuePair("senderNidImage", URLEncoder.encode(encodedImage, "UTF-8")));
                nameValuePairs.add(new BasicNameValuePair("gateway_id", mAppHandler.getGatewayId()));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
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
                                checkPurpose();
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
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mRelativeLayout, getString(R.string.try_again_msg), Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    public void checkPurpose() {
        new CheckPDAsyncTask().execute(getResources().getString(R.string.bkash_pd_url),
                "imei=" + mAppHandler.getImeiNo(),
                "&pin=" + mAppHandler.getPin(),
                "&gateway_id=" + mAppHandler.getGatewayId(),
                "&format=json");
    }

    private class CheckPDAsyncTask extends AsyncTask<String, String, JSONObject> {
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
            String url = params[0] + params[1] + params[2] + params[3] + params[4];
            return jParser.getJSONFromUrl(url);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressDialog.cancel();
            try {
                String status = jsonObject.getString(TAG_STATUS);
                if (status.equalsIgnoreCase("200")) {
                    Intent intent = new Intent(DeclarePurposeActivity.this, DeclarePurposeActivity.class);
                    intent.putExtra(DeclarePurposeActivity.JSON_RESPONSE, jsonObject.toString());
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(DeclarePurposeActivity.this, BKashMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }
}
