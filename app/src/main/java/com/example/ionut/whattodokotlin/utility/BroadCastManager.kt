package com.example.ionut.whattodokotlin.utility

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.example.ionut.whattodokotlin.MainActivity
import com.example.ionut.whattodokotlin.R
import com.example.ionut.whattodokotlin.database.ToDoDatabase
import org.jetbrains.anko.doAsync

class BroadCastManager: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent!!.getStringExtra("toDoName")
        val getId = intent!!.getIntExtra("id",0)
        val uniqueId = intent!!.getIntExtra("unique",0)
        val db = ToDoDatabase.getInstance(context!!)

            doAsync { db?.todoDao()?.updateById(getId, true)
                val intent = Intent(context,BroadCastManagerPeriodic::class.java)
                val notifManager:  NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notifManager.cancel(getId+100000)
                val pendingIntent = PendingIntent.getBroadcast(context, getId+100000, intent, 0)
                val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManger.cancel(pendingIntent)
                pendingIntent.cancel()}
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(context)
                    .setContentTitle(message)
                    .setContentText("FINAL!!!")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentIntent(PendingIntent.getActivity(context, 0, Intent(
                            context, MainActivity::class.java), PendingIntent.FLAG_ONE_SHOT
                    ))
            manager.notify(uniqueId, builder.build())
    }



}