package com.cloudwell.paywell.services.activity.reg.nidOCR.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudwell.paywell.consumer.ui.nidRegistion.model.User
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseViewModel
import com.cloudwell.paywell.services.activity.reg.nidOCR.view.IInputNidListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import java.io.File
import java.io.IOException
import java.util.*

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-09-01.
 */
class NidInputViewModel() : BaseViewModel() {

    lateinit var firstPageUri: Uri
    lateinit var secoundPageUri: Uri

    val photoURI1: Uri? = null
    var password: String = ""

    var userName: String = ""
    var userEnglishName: String = ""
    private var userFather: String = ""
    private var userMotherName: String = ""
    private var userDateOfBirthday: String = ""
    private var userNIDNumber: String = ""
    private var userAddress: String = ""


    private var isCanStaredParsing = false
    private var isDoneUserName = false
    private var isDoneUserFatherName = false
    private var isDoneUserMother = false
    private var isDoneUserBirthday = false

    var isFirstPage = false
    private var isNID = false;


    var iView: IInputNidListener? = null


    private fun findNIDNumber(lineText: String): Boolean {
        if (lineText.matches("(.*)ID NO(.*)".toRegex())) {
            val name =
                lineText.split("ID NO:".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            userNIDNumber = name[1]
            Logger.v("")
            return true
        } else {
            return false
        }
    }

    private fun findDateOfBrithday(lineText: String): Boolean {
        if (lineText.matches("(.*)Date(.*)".toRegex())) {
            val name = lineText.split("Date of Birth:".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            userDateOfBirthday = name[1]
            Logger.v("")
            return true
        } else {
            return false
        }
    }

    private fun findMotherName(lineText: String): Boolean {
        if (lineText.matches("(.*)মাতা:(.*)".toRegex())) {
            val name =
                lineText.split("মাতা:".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            userMotherName = name[1]
            Logger.v("")
            return true
        } else {
            return false
        }
    }

    private fun findFatherName(lineText: String): Boolean {
        if (lineText.matches("(.*)পিতা:(.*)".toRegex())) {
            val name =
                lineText.split("পিতা:".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            userFather = name[1]
            Logger.v("")
            return true
        } else {
            return false
        }
    }

    private fun findName(lineText: String): Boolean {
        if (lineText.matches("(.*)নাম:(.*)".toRegex())) {
            val name =
                lineText.split("নাম:".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            userName = name[1]
            Logger.v("")
            return true
        } else {
            return false
        }
    }


    fun onNextClick(context: Context, newNID: Boolean) {
        isNID = newNID
        if (::firstPageUri.isInitialized && ::secoundPageUri.isInitialized) {
            doOCFirstPage(context, firstPageUri)
        } else {
            iView?.onFailure(context.getString(R.string.m_please_input_both_nid_page))
        }
    }


    private fun findHusbandName(lineText: String): Boolean {
        if (lineText.matches("(.*)স্বামী:(.*)".toRegex())) {
            val name =
                lineText.split("স্বামী:".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            userFather = name[1]
            Logger.v("")
            return true
        } else {
            return false
        }

    }

    public fun doOCFirstPage(context: Context, resultUri: Uri) {

        iView?.showProgress()

        try {
            val image = FirebaseVisionImage.fromFilePath(
                context,
                Uri.fromFile(File(resultUri.path!!))
            )
            val options = FirebaseVisionCloudTextRecognizerOptions.Builder()
                .setModelType(FirebaseVisionCloudTextRecognizerOptions.DENSE_MODEL)
                .setLanguageHints(Arrays.asList("hi", "bn"))
                .build()

            val textRecognizer = FirebaseVision.getInstance()
                .getCloudTextRecognizer(options)

            textRecognizer.processImage(image)
                .addOnSuccessListener(OnSuccessListener<FirebaseVisionText> { result ->
                    try {
                        parseNIDOldFirstPage(context, result)
                    } catch (e: Exception) {
                        Logger.e(""+e.message)
                        iView?.hiddenProgress()
                        iView?.onFailure(context.getString(R.string.m_first_ni_error))

                        iView?.setDefaultNIDImagInFirstNIDView()
                    }
                })
                .addOnFailureListener { e ->
                    Logger.e(""+e.message)
                    iView?.hiddenProgress()
                    iView?.setDefaultNIDImagInFirstNIDView()
                    iView?.onFailure(context.getString(R.string.m_first_ni_error))
                    Log.e("", "")
                }
        } catch (e: IOException) {
            e.printStackTrace()
            iView?.hiddenProgress()
        }

    }


    private fun checkIsCanStartParsing(lineText: String): Boolean {
        if (lineText.equals("Name")) {
            return true
        } else {
            return false
        }

    }



    private fun parseNIDOldFirstPage(context: Context, result: FirebaseVisionText) {

        val blocks = result.textBlocks
        if (blocks.size == 0) {
            iView?.hiddenProgress()
            iView?.onFailure(context.getString(R.string.m_first_ni_error))
            iView?.setDefaultNIDImagInFirstNIDView()
            return
        }
        Logger.v(Gson().toJson(result))
        Logger.v("" + result.text)

        if (isNID == false) {

            val resultText = result.text
            resultText.split("\\r?\\n")


            for (block in result.textBlocks) {
                for (line in block.lines) {
                    val lineText = line.text
                    Logger.v(lineText)

                    if (lineText.equals("পিতা") || lineText.equals("পিতা।") ||lineText.equals("মাতা")||lineText.equals("মাতা।")) {
                        continue
                    } else if (lineText.length == 1) {
                        continue
                    } else if (checkIsCanStartParsing(lineText)) {
                        isCanStaredParsing = true
                    } else if (isCanStaredParsing) {
                        isCanStaredParsing = false
                        userName = lineText
                        isDoneUserName = true
                        Logger.v(lineText)
                    } else if (isDoneUserName) {
                        isDoneUserName = false
                        userFather = lineText
                        isDoneUserFatherName = true
                    } else if (isDoneUserFatherName) {
                        isDoneUserFatherName = false
                        userMotherName = lineText
                        isDoneUserMother = true
                    } else if (isDoneUserMother) {
                        isDoneUserMother = false
                        if (lineText.matches("(.*)Date of Birth(.*)".toRegex())) {
                            val name =
                                lineText.split("Date of Birth".toRegex())
                                    .dropLastWhile { it.isEmpty() }
                                    .toTypedArray()
                            userDateOfBirthday = name[1].trim()
                        }
                        isDoneUserBirthday = true

                    } else if (isDoneUserBirthday) {
                        isDoneUserBirthday = false

                        userNIDNumber = lineText.replace("[^0-9]".toRegex(), "")

                        Logger.v("" + userNIDNumber)
                    }


                }
            }

        } else {

            for (block in result.textBlocks) {
                for (line in block.lines) {
                    val lineText = line.text

                    if (findName(lineText)) {
                        Logger.v("user name found")
                    } else if (findFatherName(lineText)) {
                        Logger.v("user father name found")
                    } else if (findEnglishName(lineText)) {
                        Logger.v("user english found")
                    }else if (findHusbandName(lineText)) {
                        Logger.v("user husband name found")
                    } else if (findMotherName(lineText)) {
                        Logger.v("user mother name  found")
                    }
                    if (findDateOfBrithday(lineText)) {
                        Logger.v("user birthday found")
                    }
                    if (findNIDNumber(lineText)) {
                        Logger.v("user NID found")
                    }

                }
            }
        }

        doOCSecondPage(context, secoundPageUri)

    }

    private fun findEnglishName(lineText: String): Boolean {
        if (lineText.matches("(.*)Name:(.*)".toRegex())) {
            val name =
                    lineText.split("Name:".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            userEnglishName = name[1]
            Logger.v("")
            return true
        } else {
            return false
        }

    }

    private fun doOCSecondPage(context: Context, resultUri: Uri) {
        iView?.showProgress()

        try {
            val image = FirebaseVisionImage.fromFilePath(
                    context,
                    Uri.fromFile(File(resultUri.path!!))
            )
            val options = FirebaseVisionCloudTextRecognizerOptions.Builder()
                    .setModelType(FirebaseVisionCloudTextRecognizerOptions.DENSE_MODEL)
                    .setLanguageHints(Arrays.asList("hi", "bn"))
                    .build()


            val textRecognizer = FirebaseVision.getInstance()
                    .getCloudTextRecognizer(options)


            textRecognizer.processImage(image)
                    .addOnSuccessListener(OnSuccessListener<FirebaseVisionText> { result ->

                        try {
                            parseNIDOldSecoundPage(context, result)
                            iView?.hiddenProgress()
                        } catch (e: Exception) {
                            Logger.e(""+e.message)
                            iView?.hiddenProgress()
                            iView?.setDefaultNIDImagInSecondNIDView()
                            iView?.onFailure(context.getString(R.string.m_second_ni_error))
                        }

                    })
                    .addOnFailureListener { e ->
                        Logger.e(""+e.message)
                        iView?.hiddenProgress()
                        iView?.setDefaultNIDImagInSecondNIDView()
                        iView?.onFailure(context.getString(R.string.m_second_ni_error))
                    }
        } catch (e: IOException) {
            e.printStackTrace()
            iView?.hiddenProgress()
            iView?.setDefaultNIDImagInSecondNIDView()
            iView?.onFailure(context.getString(R.string.m_second_ni_error))
        }

    }

    private fun parseNIDOldSecoundPage(context: Context, result: FirebaseVisionText) {

        val blocks = result.textBlocks
        if (blocks.size == 0) {
            iView?.hiddenProgress()
            iView?.setDefaultNIDImagInSecondNIDView()
            iView?.onFailure(context.getString(R.string.m_second_ni_error))
            return
        }
        Logger.v(Gson().toJson(result))

        Logger.v("" + result.text)


        if (isNID == false) {
            val blocks = result.textBlocks
            if (blocks.size == 0) {
                iView?.hiddenProgress()
                iView?.setDefaultNIDImagInSecondNIDView()
                iView?.onFailure(context.getString(R.string.m_second_ni_error))
                return
            }

            val resultText = result.text


            var temp = ""

//            val substring = resultText.substring(0, resultText.indexOf("Issue"))
            val substring = resultText
            if (substring.contains("ঠিকানা:")) {
                temp = substring.removePrefix("ঠিকানা:")
            }

            // remove blood group
            if (temp.contains("Issue Date")) {
                temp = temp.substring(0, temp.indexOf("Issue Date"))
            }

            // remove blood group
            if (temp.contains("Blood Group")) {
                temp = temp.substring(0, temp.indexOf("Blood Group"))
            }

            userAddress = temp


        } else {
            val resultText = result.text

            var substring = resultText.substring(resultText.lastIndexOf("ঠিকানা") + 1)
            substring = substring.substring(0, substring.indexOf("প্রদানকারী"))

            // remove blood group
            if (substring.contains("রক্তের গ্রুপ")) {
                substring = substring.substring(0, substring.indexOf("রক্তের গ্রুপ"))
            }

            userAddress = substring

            Logger.v("", "")
        }

        if (userNIDNumber.equals("")) {

            iView?.setDefaultNIDImagInFirstNIDView()
            iView?.onFailure(context.getString(R.string.m_first_ni_error))

        } else if (userName.equals("")) {

            iView?.setDefaultNIDImagInFirstNIDView()
            iView?.onFailure(context.getString(R.string.m_first_ni_error))

        } else if (userAddress.equals("")) {
            iView?.setDefaultNIDImagInSecondNIDView()
            iView?.onFailure(context.getString(R.string.m_second_ni_error))

        } else {
            iView?.openNextActivity(
                User(
                    userName,
                        userEnglishName,
                    userFather,
                    userMotherName,
                    birthday = userDateOfBirthday,
                    nid = userNIDNumber,
                    address = userAddress
                )
            )
        }


    }




}