package com.example.and2_finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.and2_finalproject.databinding.ActivityDashboardBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseFunctions = FirebaseFunctions()
        val productsArr = firebaseFunctions.getAllProducts()
        var totalSoldProducts = 0
        var totalPrice = 0.0
        for(product in productsArr){
            totalSoldProducts += product.bought
            totalPrice += product.bought * product.price
        }

        binding.tvTotalPrice.text = totalPrice.toString()
        binding.tvTotalSoldProducts.text = totalSoldProducts.toString()

        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this,ProfileActivity::class.java))
        }

        binding.btnSearch.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))

        }

        binding.btnPopular.setOnClickListener {

        }

    }
}