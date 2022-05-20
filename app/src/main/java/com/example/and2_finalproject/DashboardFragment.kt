package com.example.and2_finalproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.and2_finalproject.databinding.FragmentDashboardBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions
import com.example.and2_finalproject.model.Product

class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

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
            //startActivity(Intent(this,ProfileActivity::class.java))
        }

        binding.btnSearch.setOnClickListener {
            //startActivity(Intent(this,MainActivity::class.java))

        }

        binding.btnPopular.setOnClickListener {

        }
    }


}