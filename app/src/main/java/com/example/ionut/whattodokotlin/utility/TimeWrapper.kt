package com.example.ionut.whattodokotlin.utility

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TimePicker
import java.text.SimpleDateFormat
import java.util.*

class TimeWrapper(val context: Context){
     private var onTimeWrapperChange: OnTimeWrapperChanged? = null
    private var onTimeInMIllis: OnTimeInMillisChanged? = null

    init {
        setDate()

    }


     var currentDate: Date? =  null
    private var isTimeChosen: Boolean = false

    private fun setDate(){
        val format = SimpleDateFormat("cccc \ndd.MM.yyyy, H:mm", Locale.US)
        if(currentDate == null || currentDate!!.time < Date().time) {
            currentDate = Date()
            onTimeWrapperChange?.onDateChanged(format.format(currentDate))
        }else{
            if(isTimeChosen){
                onTimeWrapperChange?.onDateChanged(format.format(currentDate))
            }else{
                onTimeWrapperChange?.onDateChanged(format.format(Date()))
                isTimeChosen = false
            }
        }
    }

    fun setDate(imageView: ImageView,context: Context){
        val calendar = GregorianCalendar()
        val timeSet = TimePickerDialog.OnTimeSetListener{view: TimePicker?, hourOfDay: Int, minute: Int ->
            calendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
            calendar.set(Calendar.MINUTE,minute)
            currentDate = calendar.time
            isTimeChosen = true
            setDate()
            this.onTimeWrapperChange?.onDateChangedSubText(currentDate!!)
            this.onTimeInMIllis?.onTimeInMillis(currentDate!!.time)
        }

        val calendarSet = DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            TimePickerDialog(context,timeSet,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show()

        }
        imageView.setOnClickListener{
            DatePickerDialog(context,calendarSet, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
            setDate()
        }
    }

     interface OnTimeWrapperChanged{
         fun onDateChanged(newValue: String = "")
         fun setFinalDate(setDate: Long = 0)
         fun onDateChangedSubText(date: Date)
     }

    interface OnTimeInMillisChanged{
        fun onTimeInMillis(time: Long)
    }

    fun setOnTimeWrapperChanged(onTimeWrapperChanged: OnTimeWrapperChanged){
        this.onTimeWrapperChange = onTimeWrapperChanged
    }

    fun setOntimeInMillisChanged(onTimeInMillisChanged: OnTimeInMillisChanged){
        this.onTimeInMIllis = onTimeInMillisChanged!!
    }

     fun getTimeTillFinish(date: Date): String{
        val oneHour = 3600000
        val oneDay = 86400000
        var difference = date.time - Date().time
        if(difference < 0){
            return "Date is in the past. Please select another date"
        }else{
            if(difference < oneHour){
                var minutes = difference/60000 as Int
                return if(minutes < 10){
                    "Due time in 0$minutes minute"
                }else{
                    "Due time in $minutes minutes"
                }
            }else if(difference in oneHour..oneDay){
                var hoursConvert: Int = (difference/3600000).toInt()
                var remaining: Long = difference%3600000
                var remainingMinutes: Int = (remaining/60000).toInt()
                return "Due in $hoursConvert hours and $remainingMinutes minutes"
            }else if(difference >= oneDay){
                var convertedDays: Int = (difference/86400000).toInt()
                var convertedDaysRemaing: Long = difference%86400000
                var convertedHours: Int = (convertedDaysRemaing/3600000).toInt()
                var remainingHours: Long = convertedDaysRemaing%3600000
                var convertedMinutes: Int = (remainingHours/60000).toInt()
                return "Due in $convertedDays days, $convertedHours hours and $convertedMinutes minutes"
            }
        }
        return ""
    }

}