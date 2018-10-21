package com.example.ionut.whattodokotlin.utility

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.TextWatcher
import java.util.*

class SelectedDateNotifications(val context: Context): TextWatcher{



    private val HOURS_TYPE = 1
    private val MINUTES_TYPE = 0
    private val DAYS_TYPE = 2

    private var date: Long = 0
    var hoursInput: TextInputEditText? = null
    var daysInput: TextInputEditText? = null
    var minutesInput: TextInputEditText? = null

    fun setDate(long: Long){
        this.date = long
    }

    fun setHourInput(hoursIntput: TextInputEditText) {
        this.hoursInput = hoursIntput
        this.hoursInput!!.addTextChangedListener(this)
    }
    fun setDayInput(dayIntput: TextInputEditText) {
        this.daysInput = dayIntput
        this.daysInput!!.addTextChangedListener(this)
    }
    fun setMinuteInput(minutesInput: TextInputEditText) {
        this.minutesInput = minutesInput
        this.minutesInput!!.addTextChangedListener(this)
    }

    fun getDayInput(): TextInputEditText? {return this.daysInput}
    fun getHourInput(): TextInputEditText? {return this.hoursInput}
    fun getMinuteInput(): TextInputEditText? {return this.minutesInput}



    override fun afterTextChanged(s: Editable?) {
       try {
           if(getDayInput()!!.text.toString().isNotEmpty() && date > 0){
               if(getDayInput()!!.text.toString().trim().toInt() > convertToHumanDate(DAYS_TYPE)){
                   toolarge(convertToHumanDate(DAYS_TYPE),getDayInput()!!.text.toString().trim().toInt(),"days")
               }
               finalTimeInMillis()
           }

           if(getHourInput()!!.text.toString().isNotEmpty()  && date> 0){
               if(getHourInput()!!.text.toString().trim().toInt() > convertToHumanDate(HOURS_TYPE)){
                   toolarge(convertToHumanDate(HOURS_TYPE),getHourInput()!!.text.toString().trim().toInt(),"hours")
               }
               finalTimeInMillis()
           }

           if(getMinuteInput()!!.text.toString().isNotEmpty()  && date > 0){
               if(getMinuteInput()!!.text.toString().trim().toInt() > convertToHumanDate(MINUTES_TYPE)){
                   toolarge(convertToHumanDate(MINUTES_TYPE),getMinuteInput()!!.text.toString().trim().toInt(),"minutes")
               }
               finalTimeInMillis()
           }

       }catch (e: NumberFormatException){
           e.printStackTrace()
       }
    }



    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    private fun convertToHumanDate(type: Int): Int{
        var returnInt = 0
        if(date > 0){
            val currentDate = Date()
            val finishedDate = Date(date)
            val differenceInMillis:Long = finishedDate.time - currentDate.time
            when(type){
                0 ->returnInt = (differenceInMillis/60000).toInt()
                1 ->returnInt = (differenceInMillis/3600000).toInt()
                2 ->returnInt= (differenceInMillis/86400000).toInt()
            }
            return returnInt
        }
        return 0
    }

    private fun toolarge(howManyTillFinish: Int, userEntered: Int, typeOfDate: String){
        AlertDialog.Builder(context).setTitle("Number is too large")
                .setMessage(howManyTillFinish.toString() +" "+typeOfDate+" till finished. You entered: "+userEntered+". Please enter a smaller value")
                .setNegativeButton("Dismiss", (DialogInterface.OnClickListener{which, dialog -> which.dismiss()})).show()
    }

    fun finalTimeInMillis(): Long{
        try{
            var totalDays: Long = 0
            var totalHours: Long = 0
            var totalMinutes: Long = 0
            var totalInput: Long = 0
            if(!daysInput!!.text.toString().isEmpty()){
              totalDays =  daysInput!!.text.toString().toLong()*86400000
            }

            if(!hoursInput!!.text.toString().isEmpty()){
               totalHours = hoursInput!!.text.toString().toLong()*3600000
            }

            if(!minutesInput!!.text.toString().isEmpty()){
               totalMinutes = minutesInput!!.text.toString().toLong()*60000
            }
            var differenceInMillis = Date(date).time - Date().time
            if(daysInput?.text.toString().trim().isEmpty() &&
                    hoursInput?.text.toString().trim().isEmpty() &&
                    minutesInput?.text.toString().trim().isEmpty() ){
                return 0
            }else if(differenceInMillis >= totalInput){
                totalInput = totalDays + totalHours + totalMinutes
                return totalInput
            }
        }catch (e:NumberFormatException){
            e.printStackTrace()
        }
        return 0
    }


}