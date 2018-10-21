package com.example.ionut.whattodokotlin.database

import android.arch.persistence.room.*
import java.util.*


@Entity(tableName = "todo_table")
  class ToDoModel constructor(mName: String, mPeriodic: Long, mPaused: Boolean, mDate: Date,
                      mDone: Boolean, mPath: String, mCompleted: Boolean ){


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var mId: Int = 0

    @ColumnInfo(name = "name")
    var mName: String = mName

    @ColumnInfo(name = "periodiclong")
    var mPeriodic: Long = mPeriodic

    @ColumnInfo(name = "paused")
    var mPaused: Boolean = mPaused

    @ColumnInfo(name = "date")
    @TypeConverters(DateConverter::class)
    var mDate: Date = mDate

    @ColumnInfo(name = "done")
    var mDone: Boolean = mDone

    @ColumnInfo(name = "path")
    var mPath: String = mPath
    @ColumnInfo(name = "completed")
    var mCompleted: Boolean = mCompleted
}