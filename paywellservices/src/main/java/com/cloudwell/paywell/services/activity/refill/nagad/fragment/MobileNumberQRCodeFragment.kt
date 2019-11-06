package com.cloudwell.paywell.services.activity.refill.nagad.fragment

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.android.synthetic.main.fragment_mobile_number_qr.view.*
import java.util.*




class MobileNumberQRCodeFragment : DialogFragment() {
    private var handleOnClick: HandleOnClick? = null
    val QRCodeWidth = 500



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setHandleOnClick(handleOnClick: HandleOnClick) {
        this.handleOnClick = handleOnClick
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_mobile_number_qr, container, false)
        isCancelable = true
        val nagadMainActivity = activity as BaseActivity
        nagadMainActivity.showProgressDialog()
        val generateQrCode = generateQrCode("01787679661");
        val bitmap = generateQrCode
        v.imageViewQRCode.setImageBitmap(bitmap)
        nagadMainActivity.dismissProgressDialog()
        return v
    }


    fun generateQrCode(myCodeText: String): Bitmap {


        val hintMap = Hashtable<EncodeHintType, ErrorCorrectionLevel>()
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H) // H = 30% damage

        val qrCodeWriter = QRCodeWriter()

        val size = 500

        val bitMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size, size, hintMap)
        val width = bitMatrix.width
        val bmp = Bitmap.createBitmap(width, width, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until width) {
                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = view.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    interface HandleOnClick {

        fun onOkClick(status: Int);

    }

}
