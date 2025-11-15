package com.techmania.todolistx

import android.content.Context
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class FileHelper {

    private val FILENAME = "listinfo.dat"

    fun writeData(items: ArrayList<String>, context: Context) {
        val fos: FileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE)
        ObjectOutputStream(fos).use { oos ->
            oos.writeObject(items)
        }
    }

    fun readData(context: Context): ArrayList<String> {
        return try {
            context.openFileInput(FILENAME).use { fis ->
                ObjectInputStream(fis).use { ois ->
                    @Suppress("UNCHECKED_CAST")
                    ois.readObject() as? ArrayList<String> ?: arrayListOf()
                }
            }
        } catch (e: Exception) {
            // If file doesn't exist or something went wrong: start with an empty list
            arrayListOf()
        }
    }
}
