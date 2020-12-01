package com.cloudwell.paywell.services.activity.reg;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.reg.nidOCR.NidInputActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.cloudwell.paywell.services.activity.reg.EntryMainActivity.regModel;

public class EntryThirdActivity extends BaseActivity {
    private EditText et_salesCode, et_collectionCode;
    private String str_which_btn_selected;
    private static final int PERMISSION_FOR_GALLERY = 321;
    private AppHandler mAppHandler;
    private Button btPicOutlet, btNID, btSmart, btPicOwner, btPicTrade, btPicPassport, btPicBirth, btPicDrive, bTPicVisit;

    boolean isNID;
    private String currentPhotoPath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_three);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.step_3);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        ScrollView mScrollView = findViewById(R.id.scrollView_third);
        et_salesCode = findViewById(R.id.editText_salesCode);
        et_collectionCode = findViewById(R.id.editText_collectionCode);


        btPicOutlet = findViewById(R.id.btn_picOutlet);
        btNID = findViewById(R.id.btNid);
        btSmart = findViewById(R.id.btSmart);
        btPicOwner = findViewById(R.id.btn_picOwner);
        btPicTrade = findViewById(R.id.btn_picTrade);
        btPicPassport = findViewById(R.id.btn_picPassport);
        btPicBirth = findViewById(R.id.btn_picBirth);
        btPicDrive = findViewById(R.id.btn_picDrive);
        bTPicVisit = findViewById(R.id.btn_picVisit);


//        if (mAppHandler.REG_FLAG_THREE) {
            et_salesCode.setText(regModel.getSalesCode());
            et_collectionCode.setText(regModel.getCollectionCode());


            Drawable img = getResources().getDrawable(R.drawable.icon_seleted);


            if (!regModel.getOutletImage().equals(""))

                btPicOutlet.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btPicOutlet.setCompoundDrawablePadding(100);

            if (!regModel.getNidFront().equals("")) {

                btNID.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btNID.setCompoundDrawablePadding(100);
            }

            if (!regModel.getSmartCardFront().equals("")) {

                btSmart.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btSmart.setCompoundDrawablePadding(100);

            }

            if (!regModel.getOwnerImage().equals("")) {
                btPicOwner.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btPicOwner.setCompoundDrawablePadding(100);

            }
        if (!regModel.getTradeLicense().equals("")) {
            btPicTrade.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
            btPicTrade.setCompoundDrawablePadding(100);
        }

            if (!regModel.getPassport().equals("")) {
                btPicPassport.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btPicPassport.setCompoundDrawablePadding(100);
            }

            if (!regModel.getBirthCertificate().equals("")) {
                btPicBirth.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btPicBirth.setCompoundDrawablePadding(100);
            }

            if (!regModel.getDrivingLicense().equals("")) {
                btPicDrive.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btPicDrive.setCompoundDrawablePadding(100);
            }

            if (!regModel.getVisitingCard().equals("")) {
                bTPicVisit.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                bTPicVisit.setCompoundDrawablePadding(100);

            }

        //}

        ((TextView) mScrollView.findViewById(R.id.textView_salesCode)).setTypeface(AppController.getInstance().getAponaLohitFont());
        et_salesCode.setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) mScrollView.findViewById(R.id.textView_collectionCode)).setTypeface(AppController.getInstance().getAponaLohitFont());
        et_collectionCode.setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_picOutlet)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btNid)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btSmart)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_picOwner)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_picTrade)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_picPassport)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_picBirth)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_picDrive)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_picVisit)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_preStep)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_nextStep)).setTypeface(AppController.getInstance().getAponaLohitFont());

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_REGISTRATION_THIRD_PAGE);



    }

    public void previousOnClick(View view) {
        AnalyticsManager.sendEvent(AnalyticsParameters.KEY_REGISTRATION_MENU, AnalyticsParameters.KEY_REGISTRATION_THIRD_PORTION_PREVIOUS_REQUEST);
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EntryThirdActivity.this, EntrySecondActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void nextOnClick(View view) {
        regModel.setSalesCode(et_salesCode.getText().toString().trim());
        regModel.setCollectionCode(et_collectionCode.getText().toString().trim());

        AppHandler.REG_FLAG_THREE = true;

        AnalyticsManager.sendEvent(AnalyticsParameters.KEY_REGISTRATION_MENU, AnalyticsParameters.KEY_REGISTRATION_THIRD_PORTION_SUBMIT_REQUEST);
        Intent intent = new Intent(EntryThirdActivity.this, EntryForthActivity.class);
        startActivity(intent);
        finish();
    }

    public void outletImgOnClick(View v) {

        asked("দোকানের ছবি", "1");

    }


    public void nidImgOnClick(View v) {
        isNID = true;
        Intent intent = new Intent(getApplicationContext(), NidInputActivity.class);
        intent.putExtra("isNID", isNID);
        intent.putExtra("isMissingPage", false);
        startActivity(intent);
    }


    public void smartImgOnClick(View v) {
        isNID = false;
        Intent intent = new Intent(getApplicationContext(), NidInputActivity.class);
        intent.putExtra("isNID", isNID);
        intent.putExtra("isMissingPage", false);
        startActivity(intent);

    }

    public void nidBackImgOnClick(View v) {
        asked("ন্যাশনাল আইডি পেছনের পৃষ্ঠার ছবি", "3");
    }

    public void ownerImgOnClick(View v) {
        asked("মালিকের ছবি", "4");
    }


    public void tradeLicenseImgOnClick(View v) {
        asked("ট্রেড লাইসেন্সের ছবি", "5");
    }

    public void passportImgOnClick(View v) {
        asked("বার্থ সার্টিফিকেটের ছবি", "6");
    }

    public void birthImgOnClick(View v) {
        asked("পাসপোর্টের ছবি", "7");
    }

    public void drivingImgOnClick(View v) {
        asked("ড্রাইভিং লাইসেন্সের ছবি", "8");
    }

    public void visitingImgOnClick(View v) {
        asked("ভিজিটিং কার্ডের ছবি", "9");

    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                InputStream imageStream;
                try {
                    imageStream = this.getContentResolver().openInputStream(data.getData());
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    encodeTobase64(selectedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EntryThirdActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                }
            } else {

                Bitmap selectedImage = BitmapFactory.decodeFile(currentPhotoPath);
                encodeTobase64(selectedImage);

            }
        }
    }

    public void encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap myBm = getResizedBitmap(image, 1000, 700);
        myBm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT).replaceAll("[\n\r]", "");

        String strBuild = ("xxCloud" + imageEncoded + "xxCloud");

        Drawable img = getResources().getDrawable(R.drawable.icon_seleted);


        switch (str_which_btn_selected) {
            case "1":
                regModel.setOutletImage(strBuild);
                btPicOutlet.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btPicOutlet.setCompoundDrawablePadding(100);

                break;
            case "2":


                if (isNID){


                }else {


                }

                break;
            case "3":
                regModel.setNidBack(strBuild);

                break;
            case "4":
                regModel.setOwnerImage(strBuild);
                btPicOwner.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btPicOwner.setCompoundDrawablePadding(100);

                break;
            case "5":
                regModel.setTradeLicense(strBuild);

                btPicTrade.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btPicTrade.setCompoundDrawablePadding(100);

                break;
            case "6":
                regModel.setPassport(strBuild);

                btPicPassport.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btPicPassport.setCompoundDrawablePadding(100);

                break;
            case "7":
                regModel.setBirthCertificate(strBuild);

                btPicBirth.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btPicBirth.setCompoundDrawablePadding(100);


                break;
            case "8":
                regModel.setDrivingLicense(strBuild);

                btPicDrive.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btPicDrive.setCompoundDrawablePadding(100);

                break;
            case "9":
                regModel.setVisitingCard(strBuild);


                bTPicVisit.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                bTPicVisit.setCompoundDrawablePadding(100);

                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_FOR_GALLERY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    galleryIntent();
                } else {
                    // permission denied
                    Toast.makeText(EntryThirdActivity.this, R.string.access_denied_msg, Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(
                            EntryThirdActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                }
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }


    public void asked(String title, String number) {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                askedDialog(title, number);


            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();


    }

    private void askedDialog(String title, String number) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(title)
                .setCancelable(true)
                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        str_which_btn_selected = number;

                        getCamaraIntent();
                        // filePickUtils.requestImageCamera(CAMERA_PERMISSION, false, true); // pass false if you dont want to allow image crope


                    }
                })
                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        str_which_btn_selected = number;

                        int permissionCheck = ContextCompat.checkSelfPermission(EntryThirdActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                    EntryThirdActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                        } else {
                            galleryIntent();
                        }


                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void getCamaraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "" + getApplication().getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 2);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


}
