package com.cloudwell.paywell.services.activity.reg;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppHandler;

import java.io.ByteArrayOutputStream;

/**
 * Created by Naima Gani on 11/2/2016.
 */
public class EntryThirdActivity extends AppCompatActivity {
    EditText et_salesCode, et_collectionCode;
    private String str_which_btn_selected;
    String strBuild;
    Cursor mCursor;
    ImageView img_one, img_two, img_three, img_four, img_five;
    private static final int PERMISSION_FOR_CAMERA = 123;
    private static final int PERMISSION_FOR_GALLERY = 321;
    private AppHandler mAppHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_three);
        getSupportActionBar().setTitle("৩য় ধাপ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAppHandler = new AppHandler(this);

        et_salesCode = (EditText) findViewById(R.id.editText_salesCode);
        et_collectionCode = (EditText) findViewById(R.id.editText_collectionCode);

        img_one = (ImageView) findViewById(R.id.img1);
        img_two = (ImageView) findViewById(R.id.img2);
        img_three = (ImageView) findViewById(R.id.img3);
        img_four = (ImageView) findViewById(R.id.img4);
        img_five = (ImageView) findViewById(R.id.img5);

        if (mAppHandler.REG_FLAG_THREE) {
            if (!mAppHandler.getSalesCode().equals("unknown"))
                et_salesCode.setText(mAppHandler.getSalesCode());
            if (!mAppHandler.getCollectionCode().equals("unknown"))
                et_collectionCode.setText(mAppHandler.getCollectionCode());
            if (!mAppHandler.getOutletImg().equals("unknown"))
                img_one.setVisibility(View.VISIBLE);
            if (!mAppHandler.getNIDImg().equals("unknown"))
                img_two.setVisibility(View.VISIBLE);
            if (!mAppHandler.getNIDBackImg().equals("unknown"))
                img_three.setVisibility(View.VISIBLE);
            if (!mAppHandler.getOwnerImg().equals("unknown"))
                img_four.setVisibility(View.VISIBLE);
            if (!mAppHandler.getTradeLicenseImg().equals("unknown"))
                img_five.setVisibility(View.VISIBLE);
        }
    }

    public void previousOnClick(View view) {
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
        Log.e("urlForReg1", mAppHandler.getOutletImg());
        Log.e("urlForReg2", mAppHandler.getNIDImg());
        Log.e("urlForReg3", mAppHandler.getNIDBackImg());
        Log.e("urlForReg4", mAppHandler.getOwnerImg());
        Log.e("urlForReg5", mAppHandler.getTradeLicenseImg());
        mAppHandler.setSalesCode(et_salesCode.getText().toString().trim());
        mAppHandler.setCollectionCode(et_collectionCode.getText().toString().trim());
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
                int permissionCheckGallery = ContextCompat.checkSelfPermission(EntryThirdActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int permissionCheckCamera = ContextCompat.checkSelfPermission(EntryThirdActivity.this, Manifest.permission.CAMERA);
                if (permissionCheckGallery != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            EntryThirdActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                } else {
                    startActivityForResult(
                            new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                            00001);
                }
                str_which_btn_selected = "1";
                dialogInterface.cancel();
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
                int permissionCheck = ContextCompat.checkSelfPermission(EntryThirdActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            EntryThirdActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                } else {
                    startActivityForResult(
                            new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                            00001);
                }
                str_which_btn_selected = "2";
                dialogInterface.cancel();
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
                int permissionCheck = ContextCompat.checkSelfPermission(EntryThirdActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            EntryThirdActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                } else {
                    startActivityForResult(
                            new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                            00001);
                }
                str_which_btn_selected = "3";
                dialogInterface.cancel();
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
                int permissionCheck = ContextCompat.checkSelfPermission(EntryThirdActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            EntryThirdActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                } else {
                    startActivityForResult(
                            new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                            00001);
                }
                str_which_btn_selected = "4";
                dialogInterface.cancel();
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
                int permissionCheck = ContextCompat.checkSelfPermission(EntryThirdActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            EntryThirdActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                } else {
                    startActivityForResult(
                            new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                            00001);
                }
                str_which_btn_selected = "5";
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            mCursor = null;
            if (requestCode == 00001) {
                mCursor = getContentResolver()
                        .query(data.getData(),
                                new String[]{MediaStore.Images.ImageColumns._ID},
                                null, null, null);
            }
            processImage(false, mCursor);
            mCursor.close();
        }
    }


    private void processImage(boolean fromGallery, Cursor cursor) {
        Uri newuri = null;
        if (cursor.moveToFirst()) {
            String uristringpic = "content://media/external/images/media/" + cursor.getInt(0);
            newuri = Uri.parse(uristringpic);
        }
        cursor.close();

        long selectedImageUri = ContentUris.parseId(newuri);

        Bitmap bm1 = MediaStore.Images.Thumbnails.getThumbnail(
                this.getContentResolver(), selectedImageUri,
                MediaStore.Images.Thumbnails.MINI_KIND, null);

        Bitmap bm = Bitmap.createScaledBitmap(bm1, (int) (bm1.getWidth() * 0.8), (int) (bm1.getHeight() * 0.8), true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();

        String encodedImage = Base64.encodeToString(bitmapdata, Base64.DEFAULT).replaceAll("[\n\r]", "");

        strBuild = ("xxCloud" + encodedImage + "xxCloud");

        if (str_which_btn_selected.equals("1")) {
            mAppHandler.setOutletImg(strBuild);
            img_one.setVisibility(View.VISIBLE);
        } else if (str_which_btn_selected.equals("2")) {
            mAppHandler.setNIDImg(strBuild);
            img_two.setVisibility(View.VISIBLE);
        } else if (str_which_btn_selected.equals("3")) {
            mAppHandler.setNIDBackImg(strBuild);
            img_three.setVisibility(View.VISIBLE);
        } else if (str_which_btn_selected.equals("4")) {
            mAppHandler.setOwnerImg(strBuild);
            img_four.setVisibility(View.VISIBLE);
        } else if (str_which_btn_selected.equals("5")) {
            mAppHandler.setTradeLicenseImg(strBuild);
            img_five.setVisibility(View.VISIBLE);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_FOR_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                EntryThirdActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                    } else {
                        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 00002);
                    }
                } else {
                    // permission denied
                    Toast.makeText(EntryThirdActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case PERMISSION_FOR_GALLERY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    startActivityForResult(
                            new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                            00001);
                } else {
                    // permission denied
                    Toast.makeText(EntryThirdActivity.this, R.string.access_denied_msg, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
