package com.example.myapplication.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.data.local.CarsContract.SQL_DELETE_ENTRIES
import com.example.myapplication.data.local.CarsContract.TABLE_CAR

class CarsDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABSE_VERSIO){

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLE_CAR)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object{
        const val DATABSE_VERSIO = 1
        const val DATABASE_NAME = "DbCars.db"
    }
}