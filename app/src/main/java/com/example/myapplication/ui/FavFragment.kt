package com.example.myapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.local.CarRepository
import com.example.myapplication.domain.Car
import com.example.myapplication.ui.adapter.CarAdapter

class FavFragment : Fragment() {

    private lateinit var listFav: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fav, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        infoList()

    }

    private fun getCarsOnLocalDb(): List<Car> {
        val repository = CarRepository(requireContext())
        return repository.getAll()
    }

    private fun setupView(view: View) {
        listFav = view.findViewById(R.id.rv_list_fav)
    }

    private fun infoList() {
        val cars = getCarsOnLocalDb()
        val carAdapter = CarAdapter(cars, isFavorityScream = true)
        listFav.apply {
            isVisible = true
            adapter = carAdapter
        }
        carAdapter.carItemLister = {
            //val isSaved = CarRepository(requireContext()).saveIfNotExist(car)
        }
    }



}