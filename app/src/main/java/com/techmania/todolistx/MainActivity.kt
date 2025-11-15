package com.techmania.todolistx

import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var item: EditText
    private lateinit var add: Button
    private lateinit var listView: ListView

    private var itemList = ArrayList<String>()
    private var fileHelper = FileHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        item = findViewById(R.id.editText)
        add = findViewById(R.id.button)
        listView = findViewById(R.id.list)

        // Load saved items (simple strings)
        itemList = fileHelper.readData(this)

        // Simple string adapter
        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            itemList
        )

        listView.adapter = arrayAdapter

        // Add button: add text as a simple item
        add.setOnClickListener {
            val itemName = item.text.toString().trim()

            if (itemName.isEmpty()) {
                item.error = "Please enter an item"
                return@setOnClickListener
            }

            itemList.add(itemName)
            fileHelper.writeData(itemList, applicationContext)
            arrayAdapter.notifyDataSetChanged()
            item.setText("")
        }

        // Tap item -> confirm delete
        listView.setOnItemClickListener { _, _, position, _ ->
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Delete")
            alert.setMessage("Do you want to delete this item from list?")
            alert.setCancelable(false)

            alert.setNegativeButton("No") { dialogInterface: DialogInterface, _ ->
                dialogInterface.cancel()
            }

            alert.setPositiveButton("Yes") { _, _ ->
                itemList.removeAt(position)
                arrayAdapter.notifyDataSetChanged()
                fileHelper.writeData(itemList, applicationContext)
            }

            alert.create().show()
        }
    }
}
