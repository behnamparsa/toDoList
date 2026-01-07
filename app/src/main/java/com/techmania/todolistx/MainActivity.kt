package com.techmania.todolistx

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var addButton: Button
    private lateinit var input: EditText

    private lateinit var adapter: ArrayAdapter<String>
    private val fileHelper = FileHelper()

    // All tasks with title + date/time
    private val items: ArrayList<TodoItem> = arrayListOf()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Handle system bars insets if you use edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listView = findViewById(R.id.list)
        addButton = findViewById(R.id.button)
        input = findViewById(R.id.editText)

        // Load saved items
        items.addAll(fileHelper.readData(this))

        // Sort by date/time
        items.sortBy { it.dueAtMillis }

        // Adapter shows "yyyy-MM-dd HH:mm - title"
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            items.map { formatItem(it) }.toMutableList()
        )
        listView.adapter = adapter

        // Add new task: ask for date/time, then save and sort
        addButton.setOnClickListener {
            val title = input.text.toString().trim()
            if (title.isEmpty()) {
                input.error = "Please enter a task"
                return@setOnClickListener
            }

            showDateTimePicker(title)
        }

        // Click item -> confirm delete
        listView.setOnItemClickListener { _, _, position, _ ->
            val item = items[position]

            AlertDialog.Builder(this)
                .setTitle("Delete task")
                .setMessage("Do you want to delete:\n\n${formatItem(item)}?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes") { _, _ ->
                    items.removeAt(position)
                    refreshList()
                }
                .show()
        }
    }

    /**
     * Show date + time pickers, then create/save a new TodoItem.
     */
    private fun showDateTimePicker(title: String) {
        val cal = Calendar.getInstance()

        // First pick date
        val dateDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // Then pick time
                val timeDialog = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        cal.set(Calendar.MINUTE, minute)
                        cal.set(Calendar.SECOND, 0)
                        cal.set(Calendar.MILLISECOND, 0)

                        val newItem = TodoItem(
                            title = title,
                            dueAtMillis = cal.timeInMillis
                        )
                        items.add(newItem)
                        refreshList()
                        input.text.clear()
                    },
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                )

                timeDialog.show()
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        dateDialog.show()
    }

    /**
     * Refresh list: sort by date/time, update adapter text, and persist.
     */
    private fun refreshList() {
        items.sortBy { it.dueAtMillis }

        val displayItems = items.map { formatItem(it) }

        adapter.clear()
        adapter.addAll(displayItems)
        adapter.notifyDataSetChanged()

        fileHelper.writeData(items, applicationContext)
    }

    private fun formatItem(item: TodoItem): String {
        return "${dateFormat.format(item.dueAtMillis)} - ${item.title}"
    }
}
