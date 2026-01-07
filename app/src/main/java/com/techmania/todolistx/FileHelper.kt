package com.techmania.todolistx

import android.content.Context
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class FileHelper {

    // You can rename this if you like; it's the file where we store TodoItem list
    private val FILENAME = "todoitems.dat"

    fun writeData(items: ArrayList<TodoItem>, context: Context) {
        val fos: FileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE)
        ObjectOutputStream(fos).use { oos ->
            oos.writeObject(items)
        }
    }

    fun readData(context: Context): ArrayList<TodoItem> {
        return try {
            context.openFileInput(FILENAME).use { fis ->
                ObjectInputStream(fis).use { ois ->
                    @Suppress("UNCHECKED_CAST")
                    ois.readObject() as? ArrayList<TodoItem> ?: arrayListOf()
                }
            }
        } catch (e: Exception) {
            // First launch / no file / corrupted file: start with an empty list
            arrayListOf()
        }
    }
}
