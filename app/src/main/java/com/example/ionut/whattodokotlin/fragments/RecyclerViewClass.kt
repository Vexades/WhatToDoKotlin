package com.example.ionut.whattodokotlin.fragments

//import kotlinx.android.synthetic.main.activity_main_fragment.view.*
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.ionut.whattodokotlin.MainActivity
import com.example.ionut.whattodokotlin.R
import com.example.ionut.whattodokotlin.database.ToDoDatabase
import com.example.ionut.whattodokotlin.database.ToDoModel
import com.example.ionut.whattodokotlin.utility.BroadCastManager
import com.example.ionut.whattodokotlin.utility.BroadCastManagerPeriodic
import com.example.ionut.whattodokotlin.utility.Notifications
import org.jetbrains.anko.doAsync
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


@Suppress("CAST_NEVER_SUCCEEDS")
class RecyclerViewClass(var toDoModels: List<ToDoModel>, val context: Context) : RecyclerView.Adapter<RecyclerViewClass.MyHolder>() {
    val DETAIL_FRAGMENT_TAG = "detailfragment"
    var finalDateLong: Long? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerViewClass.MyHolder = MyHolder(LayoutInflater.from(p0.context).inflate(R.layout.activity_main_fragment, p0, false))

    override fun getItemCount(): Int = toDoModels.size

    override fun onBindViewHolder(p0: RecyclerViewClass.MyHolder, p1: Int) {
        p0.bind(toDoModels.get(p1))
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val calendarText: TextView = itemView.findViewById(R.id.calendar_text)
        val timeText: TextView = itemView.findViewById(R.id.time_text)
        val notificationOff: ImageView = itemView.findViewById(R.id.notifications_off)
        val notificationOn: ImageView = itemView.findViewById(R.id.notifications_on)
        val timeLeft: TextView = itemView.findViewById(R.id.time_left)
        val mTodo: TextView = itemView.findViewById(R.id.todoInfo)
        val colorIndicator: View = itemView.findViewById(R.id.verticalBar)
        val doneIcon: ImageView = itemView.findViewById(R.id.done_icon)

        @SuppressLint("ServiceCast")
        fun bind(toDoModel: ToDoModel) {
            itemView.setOnClickListener {
                val mainScreen =  context as MainActivity
                 val fragment = FragmentDetail.newInstance(toDoModels[adapterPosition].mName,toDoModels[adapterPosition].mDate.time, toDoModels[adapterPosition].mId)
                 mainScreen.supportFragmentManager.beginTransaction().replace(R.id.frameLayout,fragment,DETAIL_FRAGMENT_TAG).addToBackStack(null).commit()
            }

            //task complet
            doneIcon.setOnClickListener {
                AsyncTask.execute {
                    val db = ToDoDatabase.getInstance(context)
                    db?.todoDao()?.updateIfCompleted(toDoModels.get(adapterPosition).mId, true)
                    Notifications(context).apply {
                        cancelPeriodicNotification(toDoModels[adapterPosition])
                        cancelFinal(toDoModels[adapterPosition])
                    }
                }
            }

                if ( toDoModels[adapterPosition].mPaused) {
                    notificationOn.visibility = View.INVISIBLE
                    notificationOff.visibility = View.VISIBLE
                }else{
                    notificationOn.visibility = View.VISIBLE
                    notificationOff.visibility = View.INVISIBLE
                }

                notificationOn.setOnClickListener {
                    Notifications(context).cancelPeriodicNotification(toDoModels[adapterPosition])
                    notificationOn.visibility = View.INVISIBLE
                    notificationOff.visibility = View.VISIBLE
                }

                notificationOff.setOnClickListener {
                    Notifications(context).resumePeriodicNotification(toDoModels[adapterPosition])
                    notificationOn.visibility = View.VISIBLE
                    notificationOff.visibility = View.INVISIBLE
                }


                mTodo.setText(toDoModel.mName)
                calendarText.setText((formatOnlyDate(toDoModel.mDate)))
                timeText.setText((formatOnlyTime(toDoModel.mDate)))
                finalDateLong = toDoModel.mDate.time
                timeLeft.setText(timeLeft(toDoModel.mDate))
            }
        }

        @SuppressLint("SimpleDateFormat")
        private fun formatOnlyDate(date: Date): String = SimpleDateFormat("dd.MM.yyyy").format(date)

        @SuppressLint("SimpleDateFormat")
        private fun formatOnlyTime(date: Date): String = SimpleDateFormat("HH:mm").format(date)

        private fun timeLeft(date: Date): String {
            val minute = 60000
            val hour = 3600000
            val day = 86400000
            val converted: Long?
            val handler = Handler()


            val oneMin = object : Runnable {
                override fun run() {
                    notifyDataSetChanged()
                    handler.postDelayed(this, 60000)
                }
            }


            val oneHour = object : Runnable {
                override fun run() {
                    notifyDataSetChanged()
                    handler.postDelayed(this, 3600000)
                }
            }

            val oneDay = object : Runnable {
                override fun run() {
                    notifyDataSetChanged()
                    handler.postDelayed(this, 86400000)
                }
            }


            val diff = date.time - Date().time
            if (diff in (minute + 1)..(hour - 1)) {
                converted = diff / minute as Int
                if (converted == 1L) {
                    handler.postDelayed(oneMin, 60000)
                    return "$converted minute remaining"
                } else {
                    handler.postDelayed(oneMin, 60000)
                    return "$converted minutes remaining"
                }
            } else if (diff > hour && diff < day) {
                converted = diff / hour
                if (converted == 1L) {
                    handler.postDelayed(oneHour, 3600000)
                    return "$converted hour remaining"
                } else {
                    handler.postDelayed(oneHour, 3600000)
                    return "$converted hours remaining"
                }
            } else if (diff > day) {
                converted = diff / day
                if (converted == 1L) {
                    handler.postDelayed(oneDay, 86400000)
                    return "$converted day remaining"
                } else {
                    handler.postDelayed(oneDay, 86400000)
                    return "$converted days remaining"
                }
            }
            return "Expired"
        }
    }
