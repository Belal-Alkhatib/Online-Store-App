package com.example.and2_finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.and2_finalproject.databinding.ActivityDashboardBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions
import com.example.and2_finalproject.model.Product

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseFunctions = FirebaseFunctions()

        val productsArr = ArrayList<Product>()
        firebaseFunctions.db.collection(firebaseFunctions.COLLECTION_PRODUCTS)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val id = document.id
                    val name = document.getString("name")!!
                    val description = document.getString("description")!!
                    val price = document.getDouble("price")!!
                    val location = document.getString("location")!!
                    val bought = document.get("bought")!!.toString().toInt()
                    val rate = document.getDouble("rate")!!
                    val image = document.getString("image")!!
                    val categoryName = document.getString("categoryName")!!
                    productsArr.add(Product(id, name, description, price, location,bought,rate, image, categoryName))
                }

                var totalSoldProducts = 0
                var totalPrice = 0.0
                for(product in productsArr){
                    totalSoldProducts += product.bought
                    totalPrice += product.bought * product.price
                }

                binding.tvTotalPrice.text = totalPrice.toString()
                binding.tvTotalSoldProducts.text = totalSoldProducts.toString()

            }.addOnFailureListener { error ->
                Log.e("hzm", error.message.toString())
            }

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