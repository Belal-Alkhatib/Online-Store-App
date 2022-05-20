package com.example.and2_finalproject

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.and2_finalproject.adapter.ProductAdapter
import com.example.and2_finalproject.databinding.ActivityMainBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions
import com.example.and2_finalproject.model.Product
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNav = binding.bottomNavigationView
        val navController = findNavController(R.id.fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.search, R.id.home, R.id.profile))
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNav.setupWithNavController(navController)

        showDialog()
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
                val productsAdapter = ProductAdapter(productsArr)
                binding.rvProducts.layoutManager = GridLayoutManager(this, 2)
                binding.rvProducts.adapter = productsAdapter
                hideDialog()
            }.addOnFailureListener { error ->
                Log.e("hzm", error.message.toString())
                hideDialog()
                Toast.makeText(this, "Error while retrieving data", Toast.LENGTH_SHORT).show()
            }

        binding.btnTest.setOnClickListener {
            val i = Intent(this,ProfileActivity::class.java)
            startActivity(i)
        }

    }
    private fun showDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Uploading image ...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    private fun hideDialog() {
        if (progressDialog!!.isShowing)
            progressDialog!!.dismiss()
    }
}