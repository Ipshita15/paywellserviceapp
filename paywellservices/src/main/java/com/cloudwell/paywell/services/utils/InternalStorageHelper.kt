package com.cloudwell.paywell.services.utils

import com.cloudwell.paywell.services.app.AppController
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 8/4/19.
 */
class InternalStorageHelper {
    companion object {
        val CombustionfileName = "Combustion.json"

        fun writeData(toJson: String, fileName: String) {
            val path = AppController.getContext().getFilesDir()
            val file = File(path, fileName)

            val stream = FileOutputStream(file)
            try {
                stream.write(toJson.toByteArray())
            } finally {
                stream.close()
            }
        }

        fun readData(fileName: String): String {

            val path = AppController.getContext().getFilesDir()
            val file = File(path, fileName)

            val length = file.length().toInt()

            val bytes = ByteArray(length)

            val fileInputString = FileInputStream(file)
            try {
                fileInputString.read(bytes)
            } finally {
                fileInputString.close()
            }

            return String(bytes)
        }

    }
}
