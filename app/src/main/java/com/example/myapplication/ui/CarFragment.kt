package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.CarsApi
import com.example.myapplication.data.local.CarRepository
import com.example.myapplication.domain.Car
import com.example.myapplication.ui.adapter.CarAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.net.URL

class CarFragment : Fragment() {

    lateinit var fabCal: FloatingActionButton
    lateinit var list: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var noInternetImage: ImageView
    lateinit var noIntenetText: TextView
    lateinit var carsApi: CarsApi

    var carArray: ArrayList<Car> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_car, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRetrofit()
        setupViews(view)
        setupListeners()
        progressBar.isVisible = true

    }

    override fun onResume() {
        super.onResume()
        if (chekForInternet(context)) {
            //callService()
            getAllCars()
        } else {
            emptyState()
        }
    }

    fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://igorbag.github.io/cars-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        carsApi = retrofit.create(CarsApi::class.java)
    }

    fun getAllCars() {
        carsApi.getAllCars().enqueue(object : Callback<List<Car>> {
            // quando retorna ok
            override fun onResponse(call: Call<List<Car>>, response: Response<List<Car>>) {
                if (response.isSuccessful){
                    progressBar.isVisible = false
                    noInternetImage.isVisible = false
                    noIntenetText.isVisible = false
                    // passar a lista
                    response.body()?.let {
                        infoList(it)
                    }
                } else {
                    // quando falhar
                    Toast.makeText(context, "Erro!!! Tente novamente mais tarde.", Toast.LENGTH_LONG).show()
                }
            }
            // quando falhar
            override fun onFailure(call: Call<List<Car>>, t: Throwable) {
                Toast.makeText(context, "Erro!!! Tente novamente mais tarde.", Toast.LENGTH_LONG).show()
            }
        })
    }


    fun setupViews(view: View) {
        list = view.findViewById(R.id.rv_list)
        fabCal = view.findViewById(R.id.fab_calculate)
        progressBar = view.findViewById(R.id.progressBar)
        noInternetImage = view.findViewById(R.id.ic_empty_state)
        noIntenetText = view.findViewById(R.id.txt_no_connection)
    }

    fun emptyState() {
        progressBar.isVisible = false
        list.isVisible = false
        noInternetImage.isVisible = true
        noIntenetText.isVisible = true
    }

    private fun setupListeners() {
        fabCal.setOnClickListener {
            startActivity(Intent(context, CalculateActivity::class.java))
        }
    }

    fun infoList(carlist: List<Car>) {
        val carAdapter = CarAdapter(carlist)
        list.apply {
            isVisible = true
            adapter = carAdapter
        }
        carAdapter.carItemLister = {car ->
            val isSaved = CarRepository(requireContext()).saveIfNotExist(car)
        }
    }

    fun callService() {
        val urlBase = "https://igorbag.github.io/cars-api/cars.json"
        progressBar.isVisible = true
        MyTask().execute(urlBase)

    }

    //verificando a conexão com a internet
    fun chekForInternet(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // se for android M
            val network =
                connectivityManager.activeNetwork ?: return false //verifica se tem conexão
            val activityNetwork = connectivityManager.getNetworkCapabilities(network)
                ?: return false// verifica a capacidade da conexão

            // tipo de conexão
            return when {
                activityNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activityNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    inner class MyTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("MyTask", "iniciando...")
        }

        override fun doInBackground(vararg url: String?): String {

            var urlConnection: HttpURLConnection? = null

            try {
                //pegar a primeira prosição do url Vararg
                val urlBase = URL(url[0])
                //abrir conexão com a internet
                urlConnection = urlBase.openConnection() as HttpURLConnection
                // tempo para o timeout
                urlConnection.connectTimeout = 60000
                // tempo de leitura
                urlConnection.readTimeout = 60000
                urlConnection.setRequestProperty(
                    "Accept",
                    "application/json"
                )

                val responseCode = urlConnection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // stream dos dados que são trafegados pela internet
                    var response = urlConnection.inputStream.bufferedReader().use { it.readText() }
                    publishProgress(response)
                } else {
                    Log.e("Erro", "Serviço indisponivel no momento ... ")
                    Toast.makeText(
                        context,
                        "Serviço indisponivel no momento ...",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (ex: Exception) {
                Log.e("Erro", "Erro ao realizar processamento...")
            } finally {
                // se urlConnection for != de null, disconectar.
                urlConnection?.disconnect()
            }
            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                val jsonArray = JSONTokener(values[0]).nextValue() as JSONArray
                Log.e("passou", "PASSOU")
                for (i in 0 until jsonArray.length()) {
                    val id = jsonArray.getJSONObject(i).getString("id")
                    Log.d("ID ->", id)

                    val price = jsonArray.getJSONObject(i).getString("preco")
                    Log.d("price ->", price)

                    val battery = jsonArray.getJSONObject(i).getString("bateria")
                    Log.d("battey ->", battery)

                    val power = jsonArray.getJSONObject(i).getString("potencia")
                    Log.d("power ->", power)

                    val recharge = jsonArray.getJSONObject(i).getString("recarga")
                    Log.d("recharge ->", recharge)

                    val urlPhoto = jsonArray.getJSONObject(i).getString("urlPhoto")
                    Log.d("urlPhoto ->", urlPhoto)

                    val model = Car(
                        id = id.toInt(),
                        price = price,
                        battery = battery,
                        power = power,
                        recharge = recharge,
                        urlPhoto = urlPhoto,
                        isFavorite = false
                    )
                    carArray.add(model)
                }
                progressBar.isVisible = false
                noInternetImage.isVisible = false
                noIntenetText.isVisible = false
                // exibir a lista
                //infoList()
            } catch (ex: Exception) {
                Log.e("Erro ->", ex.message.toString())
            }
        }

    }

}