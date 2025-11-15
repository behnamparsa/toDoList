package com.techmania.todolistx

import android.content.Context
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class FileHelper {

    val FILENAME = "listinfo.dat"

    fun writeData(item: ArrayList<String>, context: Context)
    {
        var fos: FileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE)

        var oas = ObjectOutputStream(fos)
        oas.writeObject(item)
        oas.close()


    }

    fun readData(context: Context): ArrayList<String> {
        return try {
            context.openFileInput(FILENAME).use { fis ->
                ObjectInputStream(fis).use { ois ->
                    (ois.readObject() as? ArrayList<String>) ?: arrayListOf()
                }
            }
        } catch (e: Exception) {
            arrayListOf()
        }
    }


}