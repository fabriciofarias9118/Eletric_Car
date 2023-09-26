package com.example.myapplication.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.R

class CalculateActivity : AppCompatActivity() {

    lateinit var price: EditText
    lateinit var kmTraveled: EditText
    lateinit var txtResult: TextView
    lateinit var btnCalculate: Button
    lateinit var btnClose: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate)

        price = findViewById(R.id.et_preco_kwh)
        kmTraveled = findViewById(R.id.et_km_percorrido)
        txtResult = findViewById(R.id.result)
        btnCalculate = findViewById(R.id.btn_calcular)
        btnClose = findViewById(R.id.iv_close)

        setupListeners()

        setupCachedResult()

    }

    private fun setupCachedResult() {
        val valCalculate = getSharedPref()
        txtResult.text = valCalculate.toString()
    }

    private fun setupListeners() {
        btnCalculate.setOnClickListener {
            calculate()
        }
        btnClose.setOnClickListener {
            finish()
        }
    }

    private fun calculate() {
        val price = price.text.toString().toFloat()
        val km = kmTraveled.text.toString().toFloat()
        val result = price / km

        txtResult.text = result.toString()
        saveSharedPref(result)
    }

    // savando o calculo com shared preferences
    fun saveSharedPref(result: Float){

        /*getPreferences() é um método disponível em atividades e contextos Android que permite
        acessar as preferências compartilhadas associadas a essa atividade ou contexto.
        O argumento (Context.MODE_PRIVATE) indica que essas preferências compartilhadas só serão
        acessíveis pelo próprio aplicativo.*/
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return

        //chamando edit() nas preferências compartilhadas para obter um editor
        with(sharedPref.edit()){
            // passa a chave 'saved_calc' com o valor 'result' do tipo float
            putFloat(getString(R.string.saved_calc), result)
            apply() //confirmando alterações. use apply() pois faz operações de forma assincrona.
        }

    }

    fun getSharedPref(): Float {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getFloat(getString(R.string.saved_calc), 0.0f)
    }
}