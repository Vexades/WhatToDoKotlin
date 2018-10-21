package com.example.ionut.whattodokotlin.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ionut.whattodokotlin.R
import com.example.ionut.whattodokotlin.database.ToDoDatabase
import com.example.ionut.whattodokotlin.database.ToDoModel
import org.jetbrains.anko.doAsync


class RecyclerViewCurrent : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recycler,container,false)
        val recyclerId = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerId.layoutManager = LinearLayoutManager(context)
      doAsync {
         val db = ToDoDatabase.getInstance(context!!)
         val access: List<ToDoModel>? = db?.todoDao()?.getAllByDone(false,false)
         val recycler = RecyclerViewClass(access!!,context!!)
          recyclerId.adapter = recycler
     }
        return view
    }
}