package com.example.notes

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.nio.file.attribute.AclEntry.Builder

class MainActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    var addbutton: FloatingActionButton? = null
    var databaseHelper: DatabaseHelper? = null
    var adapter: MainAdapter? = null
    lateinit var receiver: BatteryLowNotify
    val CHANNEL_ID = "iva"


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView = findViewById(R.id.recycler_view)
        addbutton = findViewById(R.id.add_button)

        databaseHelper = DatabaseHelper(applicationContext)

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(this, databaseHelper!!.getArray())
        recyclerView!!.setAdapter(adapter)

        fun showExpandableNotification() {

            val intent = Intent(this, this::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("name", "asd")
            intent.putExtra("lastName", "asd")
            val pendingIntent =
                PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    } else {
                        PendingIntent.FLAG_UPDATE_CURRENT
                    }
                )

            val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_launcher_background)
                setContentTitle("asd")
                setContentText("asd")
                setContentIntent(pendingIntent)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Test Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            with(NotificationManagerCompat.from(this)) {
                notify(1, builder.build())
            }
        }


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

                showExpandableNotification()

                dialog.dismiss()
            }
        })

        receiver = BatteryLowNotify()
        IntentFilter(Intent.ACTION_BATTERY_LOW).also {
            registerReceiver(receiver, it)
        }

    }

}