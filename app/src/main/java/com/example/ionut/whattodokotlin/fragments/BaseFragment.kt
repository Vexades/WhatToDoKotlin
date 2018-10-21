package com.example.ionut.whattodokotlin.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import com.example.ionut.whattodokotlin.MainActivity
import com.example.ionut.whattodokotlin.database.ToDoDatabase
import com.example.ionut.whattodokotlin.database.ToDoModel
import com.example.ionut.whattodokotlin.utility.BroadCastManager
import com.example.ionut.whattodokotlin.utility.BroadCastManagerPeriodic
import org.jetbrains.anko.doAsync
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

open class BaseFragment: Fragment(){

    companion object {
        val TAKE_PHOTO = 1
        val PERIODIC = 10
    }

    var mCurrentPhotoPath: String? = null

    fun dispatchPicture(){
        if(ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) !=
           PackageManager.PERMISSION_GRANTED || (ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE))!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context, "Permissions not granted", Toast.LENGTH_SHORT).show()
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity!!,Manifest.permission.CAMERA)){

            }else{
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            }

        }else{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra("return-data",true)
            if(intent.resolveActivity(context!!.packageManager) != null){
                var photoFile: File? = null

                try{
                    photoFile = createImageFile()
                }catch (ex: IOException){
                    ex.localizedMessage
                }

                if(photoFile != null){
                    val photoUri = FileProvider.getUriForFile(context!!, "com.example.ionut.whattodokotlin",photoFile)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                    startActivityForResult(intent, TAKE_PHOTO)
                }
            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File{
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmm").format(Date())
        val imageFileName = "JPEG_"+timeStamp +"_"
        val storageDirectory: File = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image: File = File.createTempFile(imageFileName,".jpg",storageDirectory)
        mCurrentPhotoPath = image.absolutePath
        return image
    }

     fun insertModel(toDoModel: ToDoModel){
        doAsync {
            val db = ToDoDatabase.getInstance(context!!)
            db?.todoDao()?.insert(toDoModel)
            sentNotification()
            val intent = Intent(context,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context!!.startActivity(intent)
        }
    }

    fun sentNotification(){
        fun sentNotification(name: String,id: Int,context: Context, date: Date, periodicTime: Long){
            var idPeriodic = 0
            if(periodicTime != 0L){
                    idPeriodic = id + 100000
            }
            val intentExpire = Intent(context,BroadCastManager::class.java)
            intentExpire.putExtra("toDoName",name)
            intentExpire.putExtra("id",id)
            intentExpire.putExtra("date",date.time)

            val intentExpirePeriodic = Intent(context,BroadCastManagerPeriodic::class.java)
            intentExpirePeriodic.putExtra("toDoName",name)
            intentExpirePeriodic.putExtra("id",idPeriodic)
            intentExpirePeriodic.putExtra("date",date.time)
            
            val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if(periodicTime != 0L){
              var alarmIntentPeriodic = PendingIntent.getBroadcast(context,idPeriodic,intentExpirePeriodic,PendingIntent.FLAG_UPDATE_CURRENT)
                alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+periodicTime,periodicTime,alarmIntentPeriodic)
            }
            var alarmIntent = PendingIntent.getBroadcast(context,id,intentExpire,PendingIntent.FLAG_ONE_SHOT)
            alarm.set(AlarmManager.RTC_WAKEUP, date.time,alarmIntent)
        }
        doAsync {
            val db = ToDoDatabase.getInstance(context!!)
            val lastModel: ToDoModel = db?.todoDao()?.getAllModels()!!.last()
            sentNotification(lastModel.mName, lastModel.mId,context!!,lastModel.mDate,lastModel.mPeriodic)
        }

    }
}