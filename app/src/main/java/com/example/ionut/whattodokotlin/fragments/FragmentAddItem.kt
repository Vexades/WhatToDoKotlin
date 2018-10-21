package com.example.ionut.whattodokotlin.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ionut.whattodokotlin.MainActivity
import com.example.ionut.whattodokotlin.R
import com.example.ionut.whattodokotlin.database.ToDoDatabase
import com.example.ionut.whattodokotlin.database.ToDoModel
import com.example.ionut.whattodokotlin.utility.SelectedDateNotifications
import com.example.ionut.whattodokotlin.utility.TimeWrapper
import com.example.ionut.whattodokotlin.utility.TimeWrapper.OnTimeWrapperChanged
import kotlinx.android.synthetic.main.single_item_view.*
import org.jetbrains.anko.doAsync
import java.text.SimpleDateFormat
import java.util.*

class FragmentAddItem: BaseFragment(), OnTimeWrapperChanged, TimeWrapper.OnTimeInMillisChanged {


    companion object {
        val PERMISSION_CAMERA = 1
    }

    var photoPath: String = ""

    var description: TextInputEditText? = null
    var daysSelect: TextInputEditText? = null
    var hoursSelect: TextInputEditText? = null
    var minutesSelect: TextInputEditText? = null

    var dateViewMain: TextView? = null
    var dateViewAfterTextSelection: TextView? = null

    var saveInDb: Button? = null

    var photoImage: ImageView? = null

    var takePhoto: FloatingActionButton? = null

    var openDialog: ImageView? = null

    var timeWrapper: TimeWrapper? = null
    var selectedNofif: SelectedDateNotifications? = null
    var notifLayout: LinearLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.single_item_view, container, false)
        var oneTimeRun = true
        description = v.findViewById(R.id.description)
        saveInDb = v.findViewById(R.id.save)
        daysSelect = v.findViewById(R.id.textDays)
        hoursSelect = v.findViewById(R.id.textHours)
        minutesSelect = v.findViewById(R.id.textMinutes)
        dateViewMain = v.findViewById(R.id.date)
        dateViewAfterTextSelection = v.findViewById(R.id.dateTextView)
        takePhoto = v.findViewById(R.id.photo_button)
        photoImage = v.findViewById(R.id.photo)
        openDialog = v.findViewById(R.id.calendarImageView)
        notifLayout = v.findViewById(R.id.notif_layout)


        timeWrapper = TimeWrapper(context!!)
        timeWrapper!!.setOnTimeWrapperChanged(this)
        timeWrapper!!.setOntimeInMillisChanged(this)
        if(oneTimeRun) {
            dateViewMain!!.text = SimpleDateFormat("cccc \ndd.MM.yyyy, H:mm", Locale.US).format(timeWrapper!!.currentDate)
            oneTimeRun = false
        }

        selectedNofif = SelectedDateNotifications(context!!)
        selectedNofif!!.setMinuteInput(minutesSelect!!)
        selectedNofif!!.setHourInput(hoursSelect!!)
        selectedNofif!!.setDayInput(daysSelect!!)



        timeWrapper!!.setDate(openDialog!!,context!!)

        takePhoto!!.setOnClickListener {
            dispatchPicture()
        }

        saveInDb!!.setOnClickListener{
            dbInsert()
            doAsync {
                val db = ToDoDatabase.getInstance(context!!)
                var asd = db?.todoDao()?.getAllModels()?.size
                Log.d("asffffffffffffffd", asd.toString())
            }

        }

        return v
    }

    override fun onDateChanged(newValue: String) {
        dateViewMain!!.text = newValue
    }

    override fun onTimeInMillis(time: Long) {
        selectedNofif!!.setDate(time)
    }


    override fun onDateChangedSubText(date: Date) {
        dateViewAfterTextSelection!!.visibility = View.VISIBLE
        dateViewAfterTextSelection!!.text = timeWrapper!!.getTimeTillFinish(date)
        notifLayout!!.visibility = View.VISIBLE
    }

    override fun setFinalDate(setDate: Long) {

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            Glide.with(this).load(mCurrentPhotoPath).into(photoImage!!)
            photoImage!!.visibility = View.VISIBLE
            photoPath = mCurrentPhotoPath!!
        }
    }

    fun dbInsert(){
        val newModel = ToDoModel(description!!.text.toString().trim(),selectedNofif!!.finalTimeInMillis(),false,timeWrapper!!.currentDate!!,false,photoPath,false)
        if(description!!.text.toString().trim().isEmpty()){
            fieldNotCompleted("Description")
        }else if(dateViewAfterTextSelection!!.text.toString().trim().isEmpty()){
            fieldNotCompleted("Date")
        }else{
            insertModel(newModel)
            val intent = Intent(context,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    fun fieldNotCompleted(typeOfField: String){
        AlertDialog.Builder(context)
                .setTitle("Empty Field")
                .setMessage("$typeOfField is empty. Please fill it")
                .setNegativeButton("Dismiss") { dialog, which ->  dialog.dismiss()}.show()
    }

}