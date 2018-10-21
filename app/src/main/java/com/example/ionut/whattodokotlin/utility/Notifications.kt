package com.example.ionut.whattodokotlin.utility

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.view.View
import com.example.ionut.whattodokotlin.database.ToDoDatabase
import com.example.ionut.whattodokotlin.database.ToDoModel

class Notifications(val context: Context){
    fun resumePeriodicNotification(toDoModel: ToDoModel){
        val intentExpirePeriodic = Intent(context, BroadCastManagerPeriodic::class.java)
        intentExpirePeriodic.putExtra("toDoName", toDoModel.mName)
        intentExpirePeriodic.putExtra("id", toDoModel.mPeriodic)
        intentExpirePeriodic.putExtra("date", toDoModel.mDate.time)
        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var alarmIntentPeriodic = PendingIntent.getBroadcast(context, toDoModel.mId + 100000, intentExpirePeriodic, PendingIntent.FLAG_UPDATE_CURRENT)
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + toDoModel.mPeriodic,toDoModel.mPeriodic, alarmIntentPeriodic)
        AsyncTask.execute({
            val db = ToDoDatabase.getInstance(context)
            db?.todoDao()?.updateByIdPaused(toDoModel.mId, false)
        })
    }

    fun cancelPeriodicNotification(toDoModel: ToDoModel){
        val intent = Intent(context, BroadCastManagerPeriodic::class.java)
        val notifManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifManager.cancel(toDoModel.mId + 100000)
        val pendingIntent = PendingIntent.getBroadcast(context, toDoModel.mId + 100000, intent, 0)
        val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManger.cancel(pendingIntent)
        pendingIntent.cancel()
        AsyncTask.execute {
            val db = ToDoDatabase.getInstance(context)
            db?.todoDao()?.updateByIdPaused(toDoModel.mId, true)
        }
    }

    fun cancelFinal(toDoModel: ToDoModel){
        val intent = Intent(context, BroadCastManager::class.java)
        val notifManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifManager.cancel(toDoModel.mId)
        val pendingIntent = PendingIntent.getBroadcast(context, toDoModel.mId , intent, 0)
        val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManger.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}