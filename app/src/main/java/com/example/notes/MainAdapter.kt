package com.example.notes


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import org.json.JSONArray
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import org.json.JSONException
import android.graphics.drawable.ColorDrawable
import android.widget.EditText
import android.view.View.OnLongClickListener
import android.content.DialogInterface
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainAdapter(var activity: Activity, var jsonArray: JSONArray) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    var databaseHelper: DatabaseHelper? = null
    fun updateArray(jsonArray: JSONArray) {
        this.jsonArray = jsonArray
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_main, parent, false)
        databaseHelper = DatabaseHelper(view.context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val `object` = jsonArray.getJSONObject(position)
            holder.notetitle.text = `object`.getString("title")
            holder.notedescription.text = `object`.getString("description")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                try {
                    val `object` = jsonArray.getJSONObject(holder.adapterPosition)
                    val sID = `object`.getString("id")
                    val sTitle = `object`.getString("title")
                    val sDescription = `object`.getString("description")
                    val dialog = Dialog(activity)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.setContentView(R.layout.dialog_main)
                    dialog.show()
                    val titleedittext = dialog.findViewById<EditText>(R.id.title_edittext)
                    val descriptionedittext =
                        dialog.findViewById<EditText>(R.id.description_edittext)
                    val updatebutton = dialog.findViewById<Button>(R.id.submit_button)
                    titleedittext.setText(sTitle)
                    descriptionedittext.setText(sDescription)
                    updatebutton.text = "Update"
                    updatebutton.setOnClickListener(View.OnClickListener {
                        val sTitle = titleedittext.text.toString().trim { it <= ' ' }
                        val sDescription = descriptionedittext.text.toString().trim { it <= ' ' }
                        databaseHelper!!.update(sID, sTitle, sDescription)
                        updateArray(databaseHelper!!.getArray())
                        notifyItemChanged(holder.adapterPosition)
                        dialog.dismiss()
                    })
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
        holder.itemView.setOnLongClickListener(object : OnLongClickListener {
            override fun onLongClick(view: View): Boolean {
                val position = holder.adapterPosition
                try {
                    val `object` = jsonArray.getJSONObject(position)
                    val sID = `object`.getString("id")
                    val builder = AlertDialog.Builder(
                        activity
                    )
                    builder.setTitle("Confirm")
                    builder.setMessage("Are you sure to delete?")
                    builder.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface: DialogInterface, i: Int) {
                            databaseHelper!!.delete(sID)
                            jsonArray.remove(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, jsonArray.length())
                        }
                    })
                    builder.setNegativeButton("No", object : DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface: DialogInterface, i: Int) {
                            dialogInterface.dismiss()
                        }
                    })
                    builder.show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                return true
            }
        })
    }

    override fun getItemCount(): Int {
        return jsonArray.length()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var notetitle: TextView
        var notedescription: TextView

        init {
            notetitle = itemView.findViewById(R.id.note_title)
            notedescription = itemView.findViewById(R.id.note_description)
        }
    }
}