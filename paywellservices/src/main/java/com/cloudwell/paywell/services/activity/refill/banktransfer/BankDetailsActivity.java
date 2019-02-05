package com.cloudwell.paywell.services.activity.refill.banktransfer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.refill.model.BranchData;
import com.cloudwell.paywell.services.activity.refill.model.DistrictData;
import com.cloudwell.paywell.services.activity.refill.model.RefillRequestData;
import com.cloudwell.paywell.services.activity.refill.model.RequestBranch;
import com.cloudwell.paywell.services.activity.refill.model.RequestRefillBalance;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankDetailsActivity extends BaseActivity {

    private static final int PERMISSION_FOR_GALLERY = 321;
    public static DistrictData responseDistrictData;
    private static String KEY_TAG = BankDetailsActivity.class.getName();
    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private ConstraintLayout mConstraintLayout;
    private Spinner mSpinnerDistrict, mSpinnerBranch;
    private Button mBtnUpload;
    private ArrayList<String> district_array, branch_array;
    private boolean districtChangeStatus;
    private ArrayAdapter<String> arrayAdapterBranchSpinner;
    private RequestRefillBalance mRequestRefillBalance;
    private BranchData responseBranchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_refill_bank);
        }

        mAppHandler = new AppHandler(this);
        mCd = new ConnectionDetector(AppController.getContext());

        mRequestRefillBalance = new RequestRefillBalance();
        mRequestRefillBalance.setmUsername("" + mAppHandler.getImeiNo());
        districtChangeStatus = true;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mRequestRefillBalance.setmBankId("" + bundle.getString("bankId"));
        }

        initializeData();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_BALANCE_REFILL_BANK);

    }

    public void initializeData() {

        mConstraintLayout = findViewById(R.id.constraintLayout);
        mSpinnerDistrict = findViewById(R.id.spinner_district);
        mSpinnerBranch = findViewById(R.id.spinner_branch);
        mBtnUpload = findViewById(R.id.btn_upload);
//        mBtnOk = findViewById(R.id.btn_ok);

        TextView textViewAccountNum = findViewById(R.id.textViewAccountNo);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            ((TextView) mConstraintLayout.findViewById(R.id.textViewAccountNo)).setTypeface(AppController.getInstance().getOxygenLightFont());
            ((TextView) mConstraintLayout.findViewById(R.id.textViewDistrict)).setTypeface(AppController.getInstance().getOxygenLightFont());
            ((TextView) mConstraintLayout.findViewById(R.id.textViewBranch)).setTypeface(AppController.getInstance().getOxygenLightFont());
            ((Button) mConstraintLayout.findViewById(R.id.btn_upload)).setTypeface(AppController.getInstance().getOxygenLightFont());
//            ((Button) mConstraintLayout.findViewById(R.id.btn_ok)).setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            ((TextView) mConstraintLayout.findViewById(R.id.textViewAccountNo)).setTypeface(AppController.getInstance().getAponaLohitFont());
            ((TextView) mConstraintLayout.findViewById(R.id.textViewDistrict)).setTypeface(AppController.getInstance().getAponaLohitFont());
            ((TextView) mConstraintLayout.findViewById(R.id.textViewBranch)).setTypeface(AppController.getInstance().getAponaLohitFont());
            ((Button) mConstraintLayout.findViewById(R.id.btn_upload)).setTypeface(AppController.getInstance().getAponaLohitFont());
//            ((Button) mConstraintLayout.findViewById(R.id.btn_ok)).setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        try {
            String text = "Account No: " + responseDistrictData.getBankInfo().getAccountNumber();
            textViewAccountNum.setText(text);
            district_array = new ArrayList<>();
            branch_array = new ArrayList<>();
            for (int i = 0; i < responseDistrictData.getDistrictData().size(); i++) {
                district_array.add(responseDistrictData.getDistrictData().get(i).getDistrictName());
            }

            final ArrayAdapter<String> arrayAdapterDistrictSpinner =
                    new ArrayAdapter<>(BankDetailsActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, district_array);

            mSpinnerDistrict.setAdapter(arrayAdapterDistrictSpinner);
            final int currentDistrictPosition = getDistrictPosition(responseDistrictData.getCurrentDistrict().getName());
            mSpinnerDistrict.setSelection(currentDistrictPosition);

            mSpinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    if (currentDistrictPosition != position || !districtChangeStatus) {
                        districtChangeStatus = false;
                        if (mCd.isConnectingToInternet()) {
                            getBranchListOnSelect(mRequestRefillBalance.getmBankId(), responseDistrictData.getDistrictData().get(position).getId());
                        } else {
                            Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.internet_connection_error_msg, Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                            snackbar.show();
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapter) {
                }
            });

            if (responseDistrictData.getBranch() != null) {
                for (int j = 0; j < responseDistrictData.getBranch().size(); j++) {
                    branch_array.add(responseDistrictData.getBranch().get(j).getName());
                }
                mSpinnerBranch.setEnabled(true);
                mBtnUpload.setEnabled(true);
                mBtnUpload.setBackground(getResources().getDrawable(R.drawable.button_green));
            } else {
                branch_array.add("No branch Available");
                mSpinnerBranch.setEnabled(false);
                mBtnUpload.setEnabled(false);
                mBtnUpload.setBackground(getResources().getDrawable(R.drawable.button_disable));
            }

            arrayAdapterBranchSpinner =
                    new ArrayAdapter<>(BankDetailsActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, branch_array);
            arrayAdapterBranchSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mSpinnerBranch.setAdapter(arrayAdapterBranchSpinner);
            mSpinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapter) {
                }
            });

//            mBtnOk.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onSubmitRequest();
//                }
//            });

            mBtnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int permissionCheck = ContextCompat.checkSelfPermission(BankDetailsActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                BankDetailsActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY
                        );
                    } else {
                        galleryIntent();
                    }
                }
            });

        } catch (Exception ex) {
            Log.d(KEY_TAG, "onFailure:");
            Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }

    private int getDistrictPosition(String value) {
        int position = 0;
        for (int i = 0; i < district_array.size(); i++) {
            if (district_array.get(i).contains(value))
                position = i;
        }
        return position;
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
        Intent intent = new Intent(BankDetailsActivity.this, BankTransferMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getBranchListOnSelect(String bankId, String districtId) {
        showProgressDialog();

        final RequestBranch requestBranch = new RequestBranch();
        requestBranch.setmUsername("" + mAppHandler.getImeiNo());
        requestBranch.setmBankId("" + bankId);
        requestBranch.setmDistrictId("" + districtId);

        Call<BranchData> responseBodyCall = ApiUtils.getAPIService().callBranchDataAPI(requestBranch.getmUsername(),
                requestBranch.getmBankId(), requestBranch.getmDistrictId());

        responseBodyCall.enqueue(new Callback<BranchData>() {
            @Override
            public void onResponse(Call<BranchData> call, Response<BranchData> response) {
                dismissProgressDialog();
                responseBranchData = response.body();

                branch_array.clear();
                if (responseBranchData.getStatus() == 200) {
                    for (int j = 0; j < responseBranchData.getBranch().size(); j++) {
                        branch_array.add(responseBranchData.getBranch().get(j).getName());
                    }
                    mSpinnerBranch.setEnabled(true);
                    mBtnUpload.setEnabled(true);
                    mBtnUpload.setBackground(getResources().getDrawable(R.drawable.button_green));
                } else {
                    branch_array.add("No branch Available");
                    mSpinnerBranch.setEnabled(false);
                    mBtnUpload.setEnabled(false);
                    mBtnUpload.setBackground(getResources().getDrawable(R.drawable.button_disable));
                }
                arrayAdapterBranchSpinner.setNotifyOnChange(true);
                arrayAdapterBranchSpinner.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BranchData> call, Throwable t) {
                dismissProgressDialog();
                Log.d(KEY_TAG, "onFailure:");
                Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        });
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 123);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == 123) {
                InputStream imageStream;
                try {
                    imageStream = this.getContentResolver().openInputStream(data.getData());
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    encodeTobase64(selectedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        } else {
            Snackbar snackbar = Snackbar.make(mConstraintLayout, "error", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    public void encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap myBm = getResizedBitmap(image, 600, 400);
        myBm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT).replaceAll("[\n\r]", "");

        mRequestRefillBalance.setmImagePath(imageEncoded);

//        mBtnUpload.setText("Uploaded");
        onSubmitRequest();
    }


    private void onSubmitRequest() {
//        if (!districtChangeStatus && responseBranchData.getStatus() != 200 || mRequestRefillBalance.getmImagePath() == null) {
//            onBackPressed();
//        } else {
        if (districtChangeStatus) {
            mRequestRefillBalance.setmDistrictId(responseDistrictData.getDistrictData().get(mSpinnerDistrict.getSelectedItemPosition()).getId());
            mRequestRefillBalance.setmBranchId(responseDistrictData.getBranch().get(mSpinnerBranch.getSelectedItemPosition()).getId());
        } else {
            mRequestRefillBalance.setmDistrictId(responseDistrictData.getDistrictData().get(mSpinnerDistrict.getSelectedItemPosition()).getId());
            mRequestRefillBalance.setmBranchId(responseBranchData.getBranch().get(mSpinnerBranch.getSelectedItemPosition()).getId());
        }
        if (mCd.isConnectingToInternet()) {
            submitBalanceRequest();
        } else {
            Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.internet_connection_error_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
//        }
    }

    private void submitBalanceRequest() {
        showProgressDialog();

        Call<RefillRequestData> responseBodyCall = ApiUtils.getAPIService().callBalanceRefillAPI(mRequestRefillBalance.getmUsername(),
                mRequestRefillBalance.getmBankId(), mRequestRefillBalance.getmDistrictId(),
                mRequestRefillBalance.getmBranchId(),
                mRequestRefillBalance.getmImagePath());

        responseBodyCall.enqueue(new Callback<RefillRequestData>() {
            @Override
            public void onResponse(Call<RefillRequestData> call, Response<RefillRequestData> response) {
                dismissProgressDialog();
                showReposeUI(response.body());
            }

            @Override
            public void onFailure(Call<RefillRequestData> call, Throwable t) {
                dismissProgressDialog();
                Log.d(KEY_TAG, "onFailure:");
                Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        });
    }

    private void showReposeUI(final RefillRequestData response) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BankDetailsActivity.this);
        builder.setTitle("Result");
        builder.setMessage(response.getMessage());
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                if (response.getStatus() == 200) {
                    onBackPressed();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

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
                    Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.access_denied_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        }
    }
}
