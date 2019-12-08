package com.cloudwell.paywell.services.ocr

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.LanguagesBaseActivity
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import kotlinx.android.synthetic.main.activity_ocr.*
import java.io.IOException
import java.util.regex.Pattern


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-11-04.
 */
class OCRActivity : LanguagesBaseActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val requestPermissionID = 101
        const val REQUEST_CODE_OCR = "REQUEST_CODE_OCR"
        const val REQUEST_FROM = "REQUEST_FROM"
        const val KEY_DESCO = "DESCO"
        const val KEY_DPDC = "DPDC"
        const val KEY_WASA = "WASA"
    }

    var requestForm = ""


    var mCameraView: SurfaceView? = null
    var mTextView: TextView? = null
    var tvFond: TextView? = null
    var mCameraSource: CameraSource? = null
    val strBuilderForStoreLine = StringBuilder()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocr)
        setToolbar(getString(R.string.scanner))

        requestForm =  intent.extras.getString(REQUEST_FROM)


        mCameraView = findViewById(R.id.surfaceView)
        mTextView = findViewById(R.id.text_view)
        startCameraSource()
        val tv = TextView(this)
        tv.text = "New textview"

        ivConfirm.setOnClickListener {
            if (!text_view.text.toString().equals("")) {
                val data = Intent()
                data.putExtra("data", ""+text_view.text)
                setResult(Activity.RESULT_OK, data)
                finish()
            }else{
               Toast.makeText(applicationContext, getString(R.string.no_bill_number_found_message), Toast.LENGTH_LONG).show();
            }
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != requestPermissionID) {
            Log.d(TAG, "Got unexpected permission result: $requestCode")
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                mCameraSource!!.start(mCameraView!!.holder)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun startCameraSource() { //Create the TextRecognizer
        val textRecognizer = TextRecognizer.Builder(applicationContext).build()
        if (!textRecognizer.isOperational) {
            Log.w(TAG, "Detector dependencies not loaded yet")
        } else { //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = CameraSource.Builder(applicationContext, textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build()
            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            mCameraView!!.holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(applicationContext,
                                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this@OCRActivity, arrayOf(Manifest.permission.CAMERA),
                                    requestPermissionID)
                            return
                        }
                        mCameraSource?.start(mCameraView!!.holder)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    mCameraSource?.stop()
                }
            })
            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(object : Detector.Processor<TextBlock?> {
                override fun release() {}
                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 */
                override fun receiveDetections(detections: Detections<TextBlock?>) {
                    val items: SparseArray<*> = detections.detectedItems
                    val strBuilder = StringBuilder()
                    for (i in 0 until items.size()) {
                        val item = items.valueAt(i) as TextBlock
                        strBuilder.append(item.value)
                        strBuilder.append("/")
                        // The following Process is used to show how to use lines & elements as well
                        for (j in 0 until items.size()) {
                            val textBlock = items.valueAt(j) as TextBlock
                            val value = textBlock.value
                            Log.v("textBlock", value)
                            strBuilder.append(textBlock.value)
                            strBuilder.append("/")
                            for (line in textBlock.components) { //extract scanned text lines here
                                Log.v("lines", line.value)
                                strBuilderForStoreLine.append(line.value + "\n")
                                //                                Pattern pattern = Pattern.compile("^(Bill No. ).[0-9]*$");
//                                Pattern pattern1 = Pattern.compile("^(Bill No ).[0-9]*$");
                                val pattern = Pattern.compile("^[0-9]*$")
                                //                                Pattern pattern = Pattern.compile("^(.).[0-9]*$");
// Search above pattern in "geeksforgeeks.org"
                                val m = pattern.matcher(line.value)
                                while (m.find()) {
                                    mTextView!!.post {
                                        if (requestForm.equals(KEY_DESCO)){
                                            if (line.value.length == 12) {
                                                mTextView!!.text = line.value
                                                mTextView!!.visibility = View.VISIBLE
                                                ivConfirm.visibility = View.VISIBLE
                                                tvResultLevel.visibility = View.VISIBLE
                                            }
                                        }else if (requestForm.equals(KEY_DPDC)){
                                            if (line.value.length == 8) {
                                                mTextView!!.text = line.value
                                                mTextView!!.visibility = View.VISIBLE
                                                ivConfirm.visibility = View.VISIBLE
                                                tvResultLevel.visibility = View.VISIBLE
                                            }
                                        }else if (requestForm.equals(KEY_WASA)){
                                            if (line.value.length == 12) {
                                                mTextView!!.text = line.value
                                                mTextView!!.visibility = View.VISIBLE
                                                ivConfirm.visibility = View.VISIBLE
                                                tvResultLevel.visibility = View.VISIBLE
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                    Log.v("strBuilder.toString()", strBuilder.toString())
                }
            })
        }
    }


}