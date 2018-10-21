package com.example.ionut.whattodokotlin.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context


@Database(entities = arrayOf(ToDoModel::class), version = 4, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class ToDoDatabase: RoomDatabase() {
     abstract fun todoDao(): ToDoDao

    companion object {
        private var INSTANCE: ToDoDatabase? = null

        fun getInstance(context: Context): ToDoDatabase?{
            if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, ToDoDatabase::class.java, "todo_database").build()
                }
            return INSTANCE
        }
    }
}