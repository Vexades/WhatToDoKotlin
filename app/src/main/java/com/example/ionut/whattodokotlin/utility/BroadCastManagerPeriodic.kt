package com.example.ionut.whattodokotlin.utility

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import com.example.ionut.whattodokotlin.MainActivity
import com.example.ionut.whattodokotlin.R
import java.util.*

class BroadCastManagerPeriodic: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("toDoName")
        val getId = intent!!.getIntExtra("id",0)
        val paused = intent.getBooleanExtra("paused",false)
        val dateTillFinisg = intent.getLongExtra("date",0)
        var tillFinish: String = TimeWrapper(context!!).getTimeTillFinish(Date(dateTillFinisg))

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context)
                .setContentTitle(message)
                .setContentText(when(tillFinish){
                    "Due time in 00 minute" -> "Final periodic notification!"
                    else -> tillFinish
                })
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(PendingIntent.getActivity(context, 0, Intent(
                        context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
                ))
        manager.notify(getId, builder.build())
    }
}