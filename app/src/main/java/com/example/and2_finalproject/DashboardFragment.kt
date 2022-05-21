package com.example.and2_finalproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.and2_finalproject.adapter.ProductAdapter
import com.example.and2_finalproject.databinding.FragmentDashboardBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions
import com.example.and2_finalproject.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query

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

        if (LoginActivity.isAdmin) {
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
                        productsArr.add(
                            Product(
                                id,
                                name,
                                description,
                                price,
                                location,
                                bought,
                                rate,
                                image,
                                categoryName
                            )
                        )
                    }

                    var totalSoldProducts = 0
                    var totalPrice = 0.0
                    for (product in productsArr) {
                        totalSoldProducts += product.bought
                        totalPrice += product.bought * product.price
                    }

                    binding.tvTotalPrice.text = totalPrice.toString()
                    binding.tvTotalSoldProducts.text = totalSoldProducts.toString()

                }.addOnFailureListener { error ->
                    Log.e("hzm", error.message.toString())
                }

            val topProductsArr = ArrayList<Product>()
            firebaseFunctions.db.collection(firebaseFunctions.COLLECTION_PRODUCTS)
                .orderBy("bought", Query.Direction.DESCENDING).limit(3)
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
                        topProductsArr.add(
                            Product(
                                id,
                                name,
                                description,
                                price,
                                location,
                                bought,
                                rate,
                                image,
                                categoryName
                            )
                        )
                    }
                    val productsAdapter = ProductAdapter(topProductsArr)
                    binding.rvTopRated.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvTopRated.adapter = productsAdapter
                }.addOnFailureListener { error ->
                    Log.e("hzm", error.message.toString())
                    Toast.makeText(
                        requireContext(),
                        "Error while retrieving data",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        } else { //not admin
            binding.cardView.visibility = View.GONE
            binding.cardView2.visibility = View.GONE
            binding.txtProductsTitle.text = "Bought Products"

            val productsArr = ArrayList<Product>()
            firebaseFunctions.db.collection(firebaseFunctions.COLLECTION_BOUGHT_PRODUCTS)
                .whereEqualTo("buyerId", FirebaseAuth.getInstance().currentUser!!.uid)
                .get()
                .addOnSuccessListener { querySnapshot ->

                    for (document in querySnapshot) {
                        val productId = document.getString("productId")!!
                        Log.e("hzm", "onResume: $productId")
                        firebaseFunctions.db.collection(firebaseFunctions.COLLECTION_PRODUCTS)
                            .document(productId)
                            .get()
                            .addOnSuccessListener { document ->
                                Log.e("hzm", "Found product: $document.id")

                                val id = document.id
                                val name = document.getString("name")!!
                                val description = document.getString("description")!!
                                val price = document.getDouble("price")!!
                                val location = document.getString("location")!!
                                val bought = document.get("bought")!!.toString().toInt()
                                val rate = document.getDouble("rate")!!
                                val image = document.getString("image")!!
                                val categoryName = document.getString("categoryName")!!
                                productsArr.add(
                                    Product(
                                        id,
                                        name,
                                        description,
                                        price,
                                        location,
                                        bought,
                                        rate,
                                        image,
                                        categoryName
                                    )
                                )
                                val productsAdapter = ProductAdapter(productsArr)
                                binding.rvTopRated.layoutManager =
                                    LinearLayoutManager(requireContext())
                                binding.rvTopRated.adapter = productsAdapter
                            }
                    }

                }.addOnFailureListener { error ->
                    Log.e("hzm", error.message.toString())
                    Toast.makeText(
                        requireContext(),
                        "Error while retrieving data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

    }


}