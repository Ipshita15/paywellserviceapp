package com.cloudwell.paywell.services.activity.reg;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static com.cloudwell.paywell.services.activity.reg.EntryMainActivity.regModel;

public class EntryThirdActivity extends AppCompatActivity {
    private EditText et_salesCode, et_collectionCode;
    private String str_which_btn_selected;
    private ImageView img_one, img_two, img_three, img_four, img_five, img_six, img_seven, img_eight, img_nine;
    private static final int PERMISSION_FOR_GALLERY = 321;
    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_three);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("৩য় ধাপ");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mAppHandler = new AppHandler(this);

        ScrollView mScrollView = findViewById(R.id.scrollView_third);
        et_salesCode = findViewById(R.id.editText_salesCode);
        et_collectionCode = findViewById(R.id.editText_collectionCode);

        img_one = findViewById(R.id.img1);
        img_two = findViewById(R.id.img2);
        img_three = findViewById(R.id.img3);
        img_four = findViewById(R.id.img4);
        img_five = findViewById(R.id.img5);
        img_six = findViewById(R.id.img6);
        img_seven = findViewById(R.id.img7);
        img_eight = findViewById(R.id.img8);
        img_nine = findViewById(R.id.img9);

        if (mAppHandler.REG_FLAG_THREE) {
            et_salesCode.setText(regModel.getSalesCode());
            et_collectionCode.setText(regModel.getCollectionCode());
            if (regModel.getOutletImage() != null)
                img_one.setVisibility(View.VISIBLE);
            if (regModel.getNidFront() != null)
                img_two.setVisibility(View.VISIBLE);
            if (regModel.getNidBack() != null)
                img_three.setVisibility(View.VISIBLE);
            if (regModel.getOwnerImage() != null)
                img_four.setVisibility(View.VISIBLE);
            if (regModel.getTradeLicense() != null)
                img_five.setVisibility(View.VISIBLE);
            if (regModel.getPassport() != null)
                img_six.setVisibility(View.VISIBLE);
            if (regModel.getBirthCertificate() != null)
                img_seven.setVisibility(View.VISIBLE);
            if (regModel.getDrivingLicense() != null)
                img_eight.setVisibility(View.VISIBLE);
            if (regModel.getVisitingCard() != null)
                img_nine.setVisibility(View.VISIBLE);
        }

        ((TextView) mScrollView.findViewById(R.id.textView_salesCode)).setTypeface(AppController.getInstance().getAponaLohitFont());
        et_salesCode.setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) mScrollView.findViewById(R.id.textView_collectionCode)).setTypeface(AppController.getInstance().getAponaLohitFont());
        et_collectionCode.setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_picOutlet)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_picNidFront)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_picNidBack)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_picOwner)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_picTrade)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_picPassport)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_picBirth)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_picDrive)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_picVisit)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_preStep)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_nextStep)).setTypeface(AppController.getInstance().getAponaLohitFont());
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

        mAppHandler.REG_FLAG_THREE = true;

        AnalyticsManager.sendEvent(AnalyticsParameters.KEY_REGISTRATION_MENU, AnalyticsParameters.KEY_REGISTRATION_THIRD_PORTION_SUBMIT_REQUEST);
        Intent intent = new Intent(EntryThirdActivity.this, EntryForthActivity.class);
        startActivity(intent);
        finish();
    }

    public void outletImgOnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EntryThirdActivity.this);
        builder.setMessage("দোকানের ছবি");
        builder.setNegativeButton("গ্যালারী থেকে", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                str_which_btn_selected = "1";
                int permissionCheckGallery = ContextCompat.checkSelfPermission(EntryThirdActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheckGallery != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            EntryThirdActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                } else {
                    galleryIntent();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void nidImgOnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EntryThirdActivity.this);
        builder.setMessage("ন্যাশনাল আইডি সামনের পৃষ্ঠার ছবি");
        builder.setNegativeButton("গ্যালারী থেকে", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                str_which_btn_selected = "2";
                int permissionCheck = ContextCompat.checkSelfPermission(EntryThirdActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            EntryThirdActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                } else {
                    galleryIntent();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void nidBackImgOnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EntryThirdActivity.this);
        builder.setMessage("ন্যাশনাল আইডি পেছনের পৃষ্ঠার ছবি");
        builder.setNegativeButton("গ্যালারী থেকে", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                str_which_btn_selected = "3";
                int permissionCheck = ContextCompat.checkSelfPermission(EntryThirdActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            EntryThirdActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                } else {
                    galleryIntent();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void ownerImgOnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EntryThirdActivity.this);
        builder.setMessage("মালিকের ছবি");
        builder.setNegativeButton("গ্যালারী থেকে", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                str_which_btn_selected = "4";
                int permissionCheck = ContextCompat.checkSelfPermission(EntryThirdActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            EntryThirdActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                } else {
                    galleryIntent();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void tradeLicenseImgOnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EntryThirdActivity.this);
        builder.setMessage("ট্রেড লাইসেন্সের ছবি");
        builder.setNegativeButton("গ্যালারী থেকে", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                str_which_btn_selected = "5";
                int permissionCheck = ContextCompat.checkSelfPermission(EntryThirdActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            EntryThirdActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                } else {
                    galleryIntent();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void passportImgOnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EntryThirdActivity.this);
        builder.setMessage("পাসপোর্টের ছবি");
        builder.setNegativeButton("গ্যালারী থেকে", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                str_which_btn_selected = "6";
                int permissionCheck = ContextCompat.checkSelfPermission(EntryThirdActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            EntryThirdActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                } else {
                    galleryIntent();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void birthImgOnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EntryThirdActivity.this);
        builder.setMessage("বার্থ সার্টিফিকেটের ছবি");
        builder.setNegativeButton("গ্যালারী থেকে", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                str_which_btn_selected = "7";
                int permissionCheck = ContextCompat.checkSelfPermission(EntryThirdActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            EntryThirdActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                } else {
                    galleryIntent();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void drivingImgOnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EntryThirdActivity.this);
        builder.setMessage("ড্রাইভিং লাইসেন্সের ছবি");
        builder.setNegativeButton("গ্যালারী থেকে", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                str_which_btn_selected = "8";
                int permissionCheck = ContextCompat.checkSelfPermission(EntryThirdActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            EntryThirdActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                } else {
                    galleryIntent();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void visitingImgOnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EntryThirdActivity.this);
        builder.setMessage("ভিজিটিং কার্ডের ছবি");
        builder.setNegativeButton("গ্যালারী থেকে", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                str_which_btn_selected = "9";
                int permissionCheck = ContextCompat.checkSelfPermission(EntryThirdActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            EntryThirdActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                } else {
                    galleryIntent();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
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

        switch (str_which_btn_selected) {
            case "1":
                regModel.setOutletImage(strBuild);
                img_one.setVisibility(View.VISIBLE);
                break;
            case "2":
                regModel.setNidFront(strBuild);
                img_two.setVisibility(View.VISIBLE);
                break;
            case "3":
                regModel.setNidBack(strBuild);
                img_three.setVisibility(View.VISIBLE);
                break;
            case "4":
                regModel.setOwnerImage(strBuild);
                img_four.setVisibility(View.VISIBLE);
                break;
            case "5":
                regModel.setTradeLicense(strBuild);
                img_five.setVisibility(View.VISIBLE);
                break;
            case "6":
                regModel.setPassport(strBuild);
                img_six.setVisibility(View.VISIBLE);
                break;
            case "7":
                regModel.setBirthCertificate(strBuild);
                img_seven.setVisibility(View.VISIBLE);
                break;
            case "8":
                regModel.setDrivingLicense(strBuild);
                img_eight.setVisibility(View.VISIBLE);
                break;
            case "9":
                regModel.setVisitingCard(strBuild);
                img_nine.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
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
}
