package com.cloudwell.paywell.services.activity.payments.statement;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.payments.PaymentsMainActivity;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

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
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.cloudwell.paywell.services.R.id.type;

public class MyTransactionsActivity extends AppCompatActivity {

    private AppHandler mAppHandler;
    private ConnectionDetector cd;
    private RelativeLayout mLinearLayout;
    private static String mPin;
    public static String[] mAmount = null;
    ListView listView;
    String date = "";
    public static int size = 0;
    CustomAdapter mAdapter;
    private ArrayList<String> myArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_payment_transactions);
        }
        mAdapter = new CustomAdapter(this);

        initialize();
    }

    private void initialize() {
        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
        mLinearLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        askForPin();
    }

    private void askForPin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pin_no_title_msg);

        final EditText pinNoET = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        pinNoET.setLayoutParams(lp);
        pinNoET.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pinNoET.setGravity(Gravity.CENTER_HORIZONTAL);
        pinNoET.setTransformationMethod(PasswordTransformationMethod.getInstance());
        builder.setView(pinNoET);

        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                InputMethodManager inMethMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inMethMan.hideSoftInputFromWindow(pinNoET.getWindowToken(), 0);

                if (pinNoET.getText().toString().length() != 0) {
                    mPin = pinNoET.getText().toString();
                    if (cd.isConnectingToInternet()) {
                        new DataAsync().execute(getResources().getString(R.string.bkash_rocket_combo_trx),
                                "imei=" + mAppHandler.getImeiNo(),
                                "&pin=" + mPin);
                    } else {
                        Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    @SuppressWarnings("deprecation")
    private class DataAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MyTransactionsActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... data) {
            String responseTxt = null;

            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data[0]);

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("pin", mPin));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseTxt;
        }

        @Override
        protected void onPostExecute(String response) {
            progressDialog.cancel();
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("trx_details");

                            listView = (ListView) findViewById(R.id.listView);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String newOne = object.getString("date");
                                if (!date.equals(newOne)) {
                                    String username, name;
                                    date = newOne;
                                    mAdapter.addSectionHeaderItem(newOne);
                                    myArray.add(newOne);
                                    mAdapter.notifyDataSetChanged();

                                    Bitmap photo = retrieveContactPhoto(MyTransactionsActivity.this, object.getString("sender"));
                                    String photo_str = BitMapToString(photo);

                                    name = getContactName(MyTransactionsActivity.this, object.getString("sender"));
                                    if (name != null && name.equals("")) {
                                        username = " ";
                                    } else {
                                        username = name;
                                    }
                                    String result = photo_str + "@" + object.getString("sender") + "@" + object.getString("amount") + "@" + object.getString("type") + "@" + username;
                                    myArray.add(result);
                                    mAdapter.addItem(result);
                                    mAdapter.notifyDataSetChanged();

                                } else {
                                    Bitmap photo = retrieveContactPhoto(MyTransactionsActivity.this, object.getString("sender"));
                                    String photo_str = BitMapToString(photo);

                                    String username, name;
                                    name = getContactName(MyTransactionsActivity.this, object.getString("sender"));
                                    if (name != null && name.equals("")) {
                                        username = " ";
                                    } else {
                                        username = name;
                                    }
                                    String result = photo_str + "@" + object.getString("sender") + "@" + object.getString("amount") + "@" + object.getString("type") + "@" + username;
                                    mAdapter.addItem(result);
                                    myArray.add(result);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            if (listView.getAdapter() == null)
                                listView.setAdapter(mAdapter);
                            else {
                                mAdapter.updateArray(); //update your adapter's data
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        Snackbar snackbar = Snackbar.make(mLinearLayout, msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
                onBackPressed();
            }
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        //String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return Base64.encodeToString(b, Base64.DEFAULT);
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
        Intent intent = new Intent(MyTransactionsActivity.this, PaymentsMainActivity.class);
        startActivity(intent);
        finish();
    }

    public class CustomAdapter extends BaseAdapter {

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;

        private ArrayList<String> mData = new ArrayList<>();
        private ArrayList<String> array = new ArrayList<>();

        private LayoutInflater mInflater;

        ImageConverter imageConverter = new ImageConverter();

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


        private void updateArray() {
            mData.clear();
            mData = myArray;
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
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
                        holder.textView = (TextView) convertView.findViewById(R.id.header);
                        convertView.setTag(holder);
                        String splitArray[] = mData.get(position).split("@");
                        holder.textView.clearComposingText();
                        holder.textView.setText(splitArray[0]);
                        break;
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.dialog_combo_rocket_bkash, parent, false);
                        holder.imageView = (ImageView) convertView.findViewById(R.id.profile);
                        holder.phnNo = (TextView) convertView.findViewById(R.id.phnNo);
                        holder.type = (TextView) convertView.findViewById(type);
                        holder.amount = (TextView) convertView.findViewById(R.id.typeName);
                        holder.arrow_left = (ImageView) convertView.findViewById(R.id.img1);
                        holder.arrow_right = (ImageView) convertView.findViewById(R.id.img2);
                        holder.username = (TextView) convertView.findViewById(R.id.username);

                        convertView.setTag(holder);

                        splitArray_row_first = mData.get(position).split("@");

                        Bitmap photo_bitmap = StringToBitMap(splitArray_row_first[0]);
                        Bitmap circularBitmap = imageConverter.getRoundedCornerBitmap(photo_bitmap, 100);
                        holder.imageView.setImageBitmap(circularBitmap);

                        holder.phnNo.setText(splitArray_row_first[1]);
                        Double value_pre = Double.parseDouble(splitArray_row_first[2]);
                        //String type_data = "Tk. " + String.format("%.2f", value_pre);
                        String type_data = "Tk. " + decimalFormat.format(value_pre);

                        if (splitArray_row_first[3].equals("bKash")) {
                            holder.type.setText(type_data);
                            holder.type.setBackgroundResource(R.drawable.square_left);
                            holder.amount.setText(getString(R.string.trx_title_bkash));
                            holder.amount.setTextColor(Color.parseColor("#004d00"));
                            holder.arrow_left.setVisibility(View.VISIBLE);
                        } else if (splitArray_row_first[3].equals("DBBL")) {
                            holder.type.setText(type_data);
                            holder.type.setBackgroundResource(R.drawable.square_right);
                            holder.amount.setText(getString(R.string.trx_title_rocket));
                            holder.amount.setTextColor(Color.parseColor("#cc3300"));
                            holder.arrow_right.setVisibility(View.VISIBLE);
                            holder.type.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String[] splitArray_trx = mData.get(position).split("@");
                                    Intent intent = new Intent(MyTransactionsActivity.this, CashInActivity.class);
                                    intent.putExtra("phn", splitArray_trx[1]);
                                    startActivity(intent);
                                }
                            });
                        }
                        if (splitArray_row_first[4].equals(" ")) {
                            holder.username.setVisibility(View.GONE);
                        } else {
                            holder.username.setVisibility(View.VISIBLE);
                            holder.username.setText(splitArray_row_first[4]);
                        }

                        holder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String[] splitArray_trx = mData.get(position).split("@");
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + splitArray_trx[1]));
                                try {
                                    startActivity(intent);
                                } catch (android.content.ActivityNotFoundException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        break;
                }
            } else {
                holder = (ViewHolder) convertView.getTag();
                switch (rowType) {
                    case TYPE_SEPARATOR:
                        convertView = mInflater.inflate(R.layout.dialog_both_header, parent, false);
                        holder.textView = (TextView) convertView.findViewById(R.id.header);
                        convertView.setTag(holder);
                        String splitArray[] = mData.get(position).split("@");
                        holder.textView.clearComposingText();
                        holder.textView.setText(splitArray[0]);
                        break;
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.dialog_combo_rocket_bkash, parent, false);
                        holder.imageView = (ImageView) convertView.findViewById(R.id.profile);
                        holder.phnNo = (TextView) convertView.findViewById(R.id.phnNo);
                        holder.type = (TextView) convertView.findViewById(type);
                        holder.amount = (TextView) convertView.findViewById(R.id.typeName);
                        holder.arrow_left = (ImageView) convertView.findViewById(R.id.img1);
                        holder.arrow_right = (ImageView) convertView.findViewById(R.id.img2);
                        holder.username = (TextView) convertView.findViewById(R.id.username);

                        convertView.setTag(holder);

                        splitArray_row_second = mData.get(position).split("@");

                        Bitmap photo_bitmap = StringToBitMap(splitArray_row_second[0]);
                        Bitmap circularBitmap = imageConverter.getRoundedCornerBitmap(photo_bitmap, 100);
                        holder.imageView.setImageBitmap(circularBitmap);

                        holder.phnNo.setText(splitArray_row_second[1]);
                        Double value_pre = Double.parseDouble(splitArray_row_second[2]);
                        //String type_data = "Tk. " + String.format("%.2f", value_pre);
                        String type_data = "Tk. " + decimalFormat.format(value_pre);

                        if (splitArray_row_second[3].equals("bKash")) {
                            holder.type.setText(type_data);
                            holder.type.setBackgroundResource(R.drawable.square_left);
                            holder.amount.setText(getString(R.string.trx_title_bkash));
                            holder.amount.setTextColor(Color.parseColor("#004d00"));
                            holder.arrow_left.setVisibility(View.VISIBLE);
                        }else if (splitArray_row_second[3].equals("DBBL")) {
                            holder.type.setText(type_data);
                            holder.type.setBackgroundResource(R.drawable.square_right);
                            holder.amount.setText(getString(R.string.trx_title_rocket));
                            holder.amount.setTextColor(Color.parseColor("#cc3300"));
                            holder.arrow_right.setVisibility(View.VISIBLE);
                            holder.type.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String[] splitArray_trx = mData.get(position).split("@");
                                    Intent intent = new Intent(MyTransactionsActivity.this, CashInActivity.class);
                                    intent.putExtra("phn", splitArray_trx[1]);
                                    startActivity(intent);
                                }
                            });
                        }
                        if (splitArray_row_second[4].equals(" ")) {
                            holder.username.setVisibility(View.GONE);
                        } else {
                            holder.username.setVisibility(View.VISIBLE);
                            holder.username.setText(splitArray_row_second[4]);
                        }

                        holder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String[] splitArray_trx = mData.get(position).split("@");
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + splitArray_trx[1]));
                                try {
                                    startActivity(intent);
                                } catch (android.content.ActivityNotFoundException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        break;
                }
            }
            return convertView;
        }

        private class ViewHolder {
            private ImageView imageView, arrow_right, arrow_left;
            private TextView textView, phnNo, amount, type, username;
        }


        private Bitmap StringToBitMap(String encodedString) {
            try {
                byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
                //Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            } catch (Exception e) {
                e.getMessage();
                return null;
            }
        }
    }

    public Bitmap retrieveContactPhoto(Context context, String number) {

        Bitmap photo;

        ContentResolver contentResolver = context.getContentResolver();
        String contactId = "";
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            cursor.close();
        }

        if (!contactId.equals("") && !contactId.isEmpty()) {
            photo = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.default_user_image);

            try {
                InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(contactId)));

                if (inputStream != null) {
                    photo = BitmapFactory.decodeStream(inputStream);
                }
                //assert inputStream != null;
                if (inputStream != null) {
                    inputStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            photo = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.default_user_image);
        }
        return photo;
    }

    public static String getContactName(Context context, String phoneNumber) {
        String contactName = "";
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }


    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    private class ImageConverter {

        private Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, pixels, pixels, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        }
    }
}