package com.example.myapplication.data.local

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import com.example.myapplication.data.local.CarsContract.CarEntry.COLUMN_BATTERY
import com.example.myapplication.data.local.CarsContract.CarEntry.COLUMN_CAR_ID
import com.example.myapplication.data.local.CarsContract.CarEntry.COLUMN_POWER
import com.example.myapplication.data.local.CarsContract.CarEntry.COLUMN_PRICE
import com.example.myapplication.data.local.CarsContract.CarEntry.COLUMN_RECHARGE
import com.example.myapplication.data.local.CarsContract.CarEntry.COLUMN_URL_PHOTO
import com.example.myapplication.domain.Car

class CarRepository(private val context: Context) {

    fun save(car: Car): Boolean {
        var isSaved = false
        try {

            val dbHelper = CarsDbHelper(context)
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(COLUMN_CAR_ID, car.id)
                put(COLUMN_PRICE, car.price)
                put(COLUMN_BATTERY, car.battery)
                put(COLUMN_POWER, car.power)
                put(COLUMN_RECHARGE, car.recharge)
                put(COLUMN_URL_PHOTO, car.urlPhoto)
            }

            val inserted = db?.insert(CarsContract.CarEntry.TABLE_NAME, null, values)

            if (inserted != null) {
                isSaved = true
            }

        } catch (ex: Exception) {
            ex.message?.let {
                Log.e("Erro ao inserir -> ", it)
            }
        }

        return isSaved
    }

    fun findCarsById(id: Int): Car {
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.readableDatabase
        //Lista das colunas a serem exibidas no resultado da Query
        val columns = arrayOf(
            BaseColumns._ID,
            COLUMN_CAR_ID,
            COLUMN_PRICE,
            COLUMN_BATTERY,
            COLUMN_POWER,
            COLUMN_RECHARGE,
            COLUMN_URL_PHOTO
        )

        val filter = "$COLUMN_CAR_ID = ?"
        val filterValues = arrayOf(id.toString())

        val cursor = db.query(
            CarsContract.CarEntry.TABLE_NAME, //Nome da tabela
            columns, // as colunas a serem exibidas
            filter,  // where (filtro)
            filterValues, // valor do where, substuindo o parametro ?
            null,
            null,
            null
        )

        var id: Long = 0
        var price: String = ""
        var battery: String = ""
        var power: String = ""
        var recharge: String = ""
        var urlPhoto: String = ""

        with(cursor) {


            while (moveToNext()) {
                id = getLong(getColumnIndexOrThrow(COLUMN_CAR_ID))
                Log.d("ID -> ", id.toString())

                price = getString(getColumnIndexOrThrow(COLUMN_PRICE))
                Log.d("price -> ", price.toString())

                battery = getString(getColumnIndexOrThrow(COLUMN_BATTERY))
                Log.d("battery -> ", battery.toString())

                power = getString(getColumnIndexOrThrow(COLUMN_POWER))
                Log.d("power -> ", power.toString())

                recharge = getString(getColumnIndexOrThrow(COLUMN_RECHARGE))
                Log.d("recharge -> ", recharge.toString())

                urlPhoto = getString(getColumnIndexOrThrow(COLUMN_URL_PHOTO))
                Log.d("urlPhoto -> ", urlPhoto.toString())

            }

        }
        cursor.close()
        return Car(
            id = id.toInt(),
            price = price,
            battery = battery,
            power = power,
            recharge = recharge,
            urlPhoto = urlPhoto,
            isFavorite = true
        )

    }

    fun saveIfNotExist(car: Car) {
        val car = car.id?.let { findCarsById(it) }
        if (car?.id == ID_WHEN_NO_CAR) {
            save(car)
        }
    }

    fun getAll(): List<Car> {
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.readableDatabase
        //Lista das colunas a serem exibidas no resultado da Query
        val columns = arrayOf(
            BaseColumns._ID,
            COLUMN_CAR_ID,
            COLUMN_PRICE,
            COLUMN_BATTERY,
            COLUMN_POWER,
            COLUMN_RECHARGE,
            COLUMN_URL_PHOTO
        )

        val cursor = db.query(
            CarsContract.CarEntry.TABLE_NAME, //Nome da tabela
            columns, // as colunas a serem exibidas
            null,  // where (filtro)
            null, // valor do where, substuindo o parametro ?
            null,
            null,
            null
        )

        val cars = mutableListOf<Car>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_CAR_ID))
                Log.d("ID -> ", id.toString())

                val price = getString(getColumnIndexOrThrow(COLUMN_PRICE))
                Log.d("price -> ", price.toString())

                val battery = getString(getColumnIndexOrThrow(COLUMN_BATTERY))
                Log.d("battery -> ", battery.toString())

                val power = getString(getColumnIndexOrThrow(COLUMN_POWER))
                Log.d("power -> ", power.toString())

                val recharge = getString(getColumnIndexOrThrow(COLUMN_RECHARGE))
                Log.d("recharge -> ", recharge.toString())

                val urlPhoto = getString(getColumnIndexOrThrow(COLUMN_URL_PHOTO))
                Log.d("urlPhoto -> ", urlPhoto.toString())

                cars.add(
                    Car(
                        id = id.toInt(),
                        price = price,
                        battery = battery,
                        power = power,
                        recharge = recharge,
                        urlPhoto = urlPhoto,
                        isFavorite = true
                    )
                )
            }

        }
        cursor.close()
        return cars

    }
    companion object {
        const val ID_WHEN_NO_CAR = 0
    }
}