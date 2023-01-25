package com.example.notes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BatteryLowNotify: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val BatteryisLow = intent?.getBooleanExtra("state", false) ?: return

        if (BatteryisLow) {
            Toast.makeText(context, "Battery Is Low Please Charge Phone", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Battery Is Charged", Toast.LENGTH_LONG).show()
        }
    }

}