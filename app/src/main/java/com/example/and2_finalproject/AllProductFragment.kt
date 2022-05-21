package com.example.and2_finalproject

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.and2_finalproject.adapter.ProductAdapter
import com.example.and2_finalproject.databinding.FragmentAllProductBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions
import com.example.and2_finalproject.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore

class SearchFragment : Fragment() {
    lateinit var db: FirebaseFirestore

    lateinit var binding: FragmentAllProductBinding
    private var progressDialog: ProgressDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllProductBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = com.google.firebase.ktx.Firebase.firestore

        if (!LoginActivity.isAdmin) {
            binding.btnAdd.visibility = View.INVISIBLE
        }

 binding.btnSearch.setOnClickListener {
     if(binding.tvSearch.text.isNotEmpty()){
         val search = binding.tvSearch.text.toString()
         var spennerChecked = binding.spinner.selectedItem
//
         when (spennerChecked){
             "Category Name" ->  whereCategoryName(search)
             "Product Name" -> whereProductName(search)
             "Price" -> wherePrice(search.toInt())
             "Rate" -> whereRate(search.toInt())
             else -> Toast.makeText(requireContext(), "Please Select a Operation", Toast.LENGTH_SHORT).show()

         }

     }else{
         Toast.makeText(requireContext(), "Please Fill Fields And Select a Operation", Toast.LENGTH_SHORT).show()
     }
 }
    }
    override fun onResume() {
        super.onResume()
        showDialog()
        val firebaseFunctions = FirebaseFunctions()
        val productsArr = ArrayList<Product>()
        firebaseFunctions.db.collection(firebaseFunctions.COLLECTION_PRODUCTS)
            .orderBy("categoryName")
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
                binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
                binding.rvProducts.adapter = productsAdapter
                hideDialog()
            }.addOnFailureListener { error ->
                Log.e("hzm", error.message.toString())
                hideDialog()
                Toast.makeText(requireContext(), "Error while retrieving data", Toast.LENGTH_SHORT).show()
            }



        binding.btnAdd.setOnClickListener {
            startActivity(Intent(requireContext(),AddProduct::class.java))
        }

    }

    private fun showDialog() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Uploading image ...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    private fun hideDialog() {
        if (progressDialog!!.isShowing)
            progressDialog!!.dismiss()
    }

    private fun whereCategoryName(categoryName:String){
        Log.e("bil","Category Name")
        //**********
        val productList = ArrayList<Product>()

        db.collection("products").whereEqualTo("categoryName",categoryName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for( document in querySnapshot){
                    var id = document.get("id").toString()
                    var name = document.get("name").toString()
                    val description = document.get("description").toString()
                    val price = document.get("price").toString().toDouble()
                    val location = document.get("location").toString()
                    val bought = document.get("bought").toString().toInt()
                    val rate = document.get("rate").toString().toDouble()
                    val img = document.get("image").toString()
                    val categoryName = document.get("categoryName").toString()

                    var pro = Product(id, name, description, price, location, bought, rate, img, categoryName )

                    productList.add(pro)

                    Toast.makeText(requireContext(), "Category Search Downloaded", Toast.LENGTH_SHORT).show()

                    val employeeAdapter = ProductAdapter(productList)
                    binding.rvProducts.adapter = employeeAdapter
                    binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())

                }
                Log.e("bil",productList.toString())

            }
            .addOnFailureListener { exception ->

            }
        //**********
    }
    private fun whereProductName(productName:String){
        //**********
        val productList = ArrayList<Product>()

        db.collection("products").whereEqualTo("name",productName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for( document in querySnapshot){
                    var id = document.get("id").toString()
                    var name = document.get("name").toString()
                    val description = document.get("description").toString()
                    val price = document.get("price").toString().toDouble()
                    val location = document.get("location").toString()
                    val bought = document.get("bought").toString().toInt()
                    val rate = document.get("rate").toString().toDouble()
                    val img = document.get("image").toString()
                    val categoryName = document.get("categoryName").toString()

                    var pro = Product(id, name, description, price, location, bought, rate, img, categoryName )

                    productList.add(pro)
                    Toast.makeText(requireContext(), "Category Search finished", Toast.LENGTH_SHORT).show()

                    val employeeAdapter = ProductAdapter(productList)
                    binding.rvProducts.adapter = employeeAdapter
                    binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())

                }
            }
            .addOnFailureListener { exception ->

            }
        //**********
    }
    private fun wherePrice(price:Int){
        //**********
        val productList = ArrayList<Product>()

        db.collection("products").whereEqualTo("price",price)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for( document in querySnapshot){
                    var id = document.get("id").toString()
                    var name = document.get("name").toString()
                    val description = document.get("description").toString()
                    val price = document.get("price").toString().toDouble()
                    val location = document.get("location").toString()
                    val bought = document.get("bought").toString().toInt()
                    val rate = document.get("rate").toString().toDouble()
                    val img = document.get("image").toString()
                    val categoryName = document.get("categoryName").toString()

                    var pro = Product(id, name, description, price, location, bought, rate, img, categoryName )

                    productList.add(pro)
                    Toast.makeText(requireContext(), "Category Search Downloaded", Toast.LENGTH_SHORT).show()

                    val employeeAdapter = ProductAdapter(productList)
                    binding.rvProducts.adapter = employeeAdapter
                    binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())

                }
            }
            .addOnFailureListener { exception ->

            }
        //**********
    }
    private fun whereRate(rate:Int){
        //**********
        val productList = ArrayList<Product>()

        db.collection("products").whereEqualTo("rate",rate)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for( document in querySnapshot){
                    var id = document.get("id").toString()
                    var name = document.get("name").toString()
                    val description = document.get("description").toString()
                    val price = document.get("price").toString().toDouble()
                    val location = document.get("location").toString()
                    val bought = document.get("bought").toString().toInt()
                    val rate = document.get("rate").toString().toDouble()
                    val img = document.get("image").toString()
                    val categoryName = document.get("categoryName").toString()

                    var pro = Product(id, name, description, price, location, bought, rate, img, categoryName )

                    productList.add(pro)
                    Toast.makeText(requireContext(), "Category Search Downloaded", Toast.LENGTH_SHORT).show()

                    val employeeAdapter = ProductAdapter(productList)
                    binding.rvProducts.adapter = employeeAdapter
                    binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())

                }
            }
            .addOnFailureListener { exception ->

            }
        //**********
    }
}

