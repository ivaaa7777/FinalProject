package com.example.notes

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.notes.MainAdapter
import android.os.Bundle
import com.example.notes.R
import androidx.recyclerview.widget.LinearLayoutManager
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.notes.DatabaseHelper

class MainActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    var addbutton: FloatingActionButton? = null
    var databaseHelper: DatabaseHelper? = null
    var adapter: MainAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        addbutton = findViewById(R.id.add_button)

        databaseHelper = DatabaseHelper(applicationContext)

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(this, databaseHelper!!.getArray())
        recyclerView!!.setAdapter(adapter)

        addbutton!!.setOnClickListener(View.OnClickListener {
            val dialog = Dialog(this@MainActivity)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_main)
            dialog.show()

            val titleedittext = dialog.findViewById<EditText>(R.id.title_edittext)
            val descriptionedittext = dialog.findViewById<EditText>(R.id.description_edittext)
            val submitbutton = dialog.findViewById<Button>(R.id.submit_button)

            submitbutton.setOnClickListener {
                val sTitle = titleedittext.text.toString().trim { it <= ' ' }
                val sDescription = descriptionedittext.text.toString().trim { it <= ' ' }

                databaseHelper!!.insert(sTitle, sDescription)
                adapter!!.updateArray(databaseHelper!!.getArray())

                dialog.dismiss()
            }
        })
    }
}