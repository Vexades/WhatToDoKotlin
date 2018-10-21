package com.example.ionut.whattodokotlin.fragments

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.ionut.whattodokotlin.R
import com.example.ionut.whattodokotlin.database.ToDoDatabase
import java.text.SimpleDateFormat
import java.util.*

private const val NAME = "name"
private const val DATE = "date"
private const val ID = "id"

class FragmentDetail: Fragment(){

        private var name: String? = null
        private var date: Long? = null
        private var id: Int? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.detail_layout,container,false)
        val mName = view.findViewById<TextView>(R.id.nameDetail)
        val mTime = view.findViewById<TextView>(R.id.timeDetail)
        val mDate = view.findViewById<TextView>(R.id.dateDetail)
        val editName = view.findViewById<ImageView>(R.id.edit_description)
        mName.text = name
        mTime.text = timeText(date)
        mDate.text = dateText(date)
        editName.setOnClickListener {
            Toast.makeText(context,"asdas",Toast.LENGTH_SHORT).show()
        }
        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(NAME)
            date = it.getLong(DATE)
            id = it.getInt(ID)
        }
    }

    companion object {
        fun newInstance(name: String, date:Long?, id: Int) = FragmentDetail().apply {
        arguments =  Bundle().apply {
                putString(NAME,name)
            date?.let { putLong(DATE, it) }
            putInt(ID,id)
            }
        }
    }


    fun updateFileds(date: Long = 0, name: String = ""){
        AsyncTask.execute{
            val db = context?.let { ToDoDatabase.getInstance(it)}
            if(date != 0L ) { db?.todoDao()?.updateDate(id,date)}
            if(name != "") {db?.todoDao()?.updateName(id,name)}
        }
    }

    fun timeText(date: Long?) : String = SimpleDateFormat("H:mm").format(date?.let { Date(it) })
    fun dateText(date: Long?) : String = SimpleDateFormat("E d.M.y").format(date?.let { Date(it) })

}