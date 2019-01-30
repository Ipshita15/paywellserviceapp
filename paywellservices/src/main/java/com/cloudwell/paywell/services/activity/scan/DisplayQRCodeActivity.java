package com.cloudwell.paywell.services.activity.scan;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppHandler;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class DisplayQRCodeActivity extends AppCompatActivity {

    private AppHandler mAppHandler;
    private Context mContext;
    public final static int QRCodeWidth = 700;
    private ImageView imageViewQRCode;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_scan_qr_code);
        }
        mContext = this;
        mAppHandler = new AppHandler(this);
        mRelativeLayout = findViewById(R.id.relativeLayout);

        imageViewQRCode = findViewById(R.id.imageViewQRCode);

        if (mAppHandler.getQrCodeImagePath().equalsIgnoreCase("unknown")) {
            generateQRCode();
        } else {
            setImage(mAppHandler.getQrCodeImagePath());
        }

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_QRCODE_ICON_PAGE);


    }


    public void generateQRCode() {
        try {
            if(mAppHandler.getRID() != "unknown") {
                Bitmap bitmap = TextToImageEncode(mAppHandler.getRID());
                imageViewQRCode.setImageBitmap(bitmap);
                String path = saveToInternalStorage(bitmap);
                mAppHandler.setQrCodeImagePath(path);
            } else {
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.data_error_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                }, 2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.data_error_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                }
            }, 2000);
        }
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRCodeWidth, QRCodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        ContextCompat.getColor(mContext, android.R.color.black) : ContextCompat.getColor(mContext, android.R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
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
        finish();
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File myPath = new File(directory, "qr_code.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar.make(mRelativeLayout, "Unable to store image", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mRelativeLayout, "Unable to store image", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
        return directory.getAbsolutePath();
    }

    private void setImage(String imgPath) {
        try {
            File file = new File(imgPath, "qr_code.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            imageViewQRCode.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            generateQRCode();
        }

    }
}
