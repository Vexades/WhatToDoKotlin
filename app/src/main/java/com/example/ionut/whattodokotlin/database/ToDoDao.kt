package com.example.ionut.whattodokotlin.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface ToDoDao{
    @Insert
    fun insert(toDoModels: ToDoModel)

    @Query("select * from todo_table")
    fun getAllModels(): List<ToDoModel>

    @Query("update todo_table set done= :done where id= :id")
    fun updateById(id: Int, done: Boolean)

    @Query("update todo_table set paused= :paused where id = :id")
    fun updateByIdPaused(id: Int, paused: Boolean)

    @Query("select * from todo_table where done= :done and completed= :completed")
    fun getAllByDone(done: Boolean, completed: Boolean): List<ToDoModel>?

    @Query("update todo_table set completed= :completed where id= :id")
    fun updateIfCompleted(id: Int,completed: Boolean)

    @Query("select * from todo_table where completed= :completed")
    fun getAllByCompleted(completed: Boolean): List<ToDoModel>?

    @Query("update  todo_table set name= :name where id= :id")
    fun updateName(id: Int?, name: String?)

    @Query("update  todo_table set date= :date where id= :id")
    fun updateDate(id: Int?, date: Long?)


}