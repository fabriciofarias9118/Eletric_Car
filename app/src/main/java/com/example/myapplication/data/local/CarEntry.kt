package com.example.myapplication.data.local

import android.provider.BaseColumns

object CarsContract {


    object CarEntry : BaseColumns {
        const val TABLE_NAME = "car"
        const val COLUMN_CAR_ID = "car_id"
        const val COLUMN_PRICE= "price"
        const val COLUMN_BATTERY = "battery"
        const val COLUMN_POWER = "power"
        const val COLUMN_RECHARGE = "recharge"
        const val COLUMN_URL_PHOTO = "urlPhoto"
    }

    const val TABLE_CAR =
        "CREATE TABLE ${CarEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRYMARY KEY," +
                "${CarEntry.COLUMN_CAR_ID} TEXT," +
                "${CarEntry.COLUMN_PRICE} TEXT," +
                "${CarEntry.COLUMN_BATTERY} TEXT," +
                "${CarEntry.COLUMN_POWER} TEXT," +
                "${CarEntry.COLUMN_RECHARGE} TEXT," +
                "${CarEntry.COLUMN_URL_PHOTO} TEXT)"

    const val SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS ${CarEntry.TABLE_NAME}"
}