package com.example.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.domain.Car

class CarAdapter(private val cars: List<Car>, private val isFavorityScream: Boolean = false) :
    RecyclerView.Adapter<CarAdapter.ViewHolder>() {
    var carItemLister: (Car) -> Unit = {}

    // cria a view, elemeto da lista; EXEMPLO:  o cardView com seus dados.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.car_item, parent, false)
        return ViewHolder(view)
    }

    // pega o tamanho da lista
    override fun getItemCount() = cars.size

    // passar os dados para os compenentes do item layout
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.price.text = cars[position].price
        holder.battery.text = cars[position].battery
        holder.power.text = cars[position].power
        holder.recharge.text = cars[position].recharge

        if(isFavorityScream){
            holder.favorite.setImageResource(R.drawable.remove_fav)
        }

        holder.favorite.setOnClickListener {
            val car = cars[position]
            carItemLister(car)
            setupFavorite(car, holder)
        }
    }

    private fun setupFavorite(
        car: Car,
        holder: ViewHolder
    ) {
        car.isFavorite = !car.isFavorite
        if (car.isFavorite)
            holder.favorite.setImageResource(R.drawable.remove_fav)
        else
            holder.favorite.setImageResource(R.drawable.add_fav)
    }

    //pega os componentes do layout do item
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val price: TextView
        val battery: TextView
        val power: TextView
        val recharge: TextView
        val favorite: ImageView

        init {
            view.apply {
                price = findViewById(R.id.price_value)
                battery = findViewById(R.id.battery_value)
                power = findViewById(R.id.power_value)
                recharge = findViewById(R.id.recharge_value)
                favorite = findViewById(R.id.ic_fav)
            }
        }
    }
}