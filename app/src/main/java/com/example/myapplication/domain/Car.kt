package com.example.myapplication.domain

import com.google.gson.annotations.SerializedName

data class Car(
    @SerializedName("id") val id: Int?,
    @SerializedName("preco") val price: String,
    @SerializedName("bateria") val battery: String,
    @SerializedName("potencia") val power: String,
    @SerializedName("recarga") val recharge: String,
    @SerializedName("urlPhoto") val urlPhoto: String,
    var isFavorite: Boolean

/*    val id: Int,
    val price: String,
    val battery: String,
    val power: String,
    val recharge: String,
    val urlPhoto: String,
    var isFavorite: Boolean*/
)