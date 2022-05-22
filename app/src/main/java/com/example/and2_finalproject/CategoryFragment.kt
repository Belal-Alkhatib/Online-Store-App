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
import com.example.and2_finalproject.adapter.CategoryAdapter
import com.example.and2_finalproject.adapter.ProductAdapter
import com.example.and2_finalproject.databinding.FragmentCategoryBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions
import com.example.and2_finalproject.model.Category
import com.example.and2_finalproject.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore

class CategoryFragment : Fragment() {
    lateinit var db: FirebaseFirestore
    lateinit var binding: FragmentCategoryBinding
   // private var progressDialog: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = com.google.firebase.ktx.Firebase.firestore

        if (!LoginActivity.isAdmin) {
            binding.btnAdd.visibility = View.INVISIBLE
        }

        binding.btnSearch.setOnClickListener {
            if(binding.tvSearch.text.isNotEmpty()){
                val search = binding.tvSearch.text.toString()
                whereCategoryName(search)
            }else{
                Toast.makeText(requireContext(), "Please Fill Fields And Select a Operation", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)

        if (!LoginActivity.isAdmin) {
            binding.btnAdd.visibility = View.INVISIBLE
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
//        all code
    //    showDialog()
        val firebaseFunctions = FirebaseFunctions()
        val categoriesArr = ArrayList<Category>()
        firebaseFunctions.db.collection(firebaseFunctions.COLLECTION_CATEGORIES)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val id = document.id
                    val name = document.getString("name")!!
                    val description = document.getString("description")!!
//                    val price = document.getDouble("price")!!
//                    val location = document.getString("location")!!
//                    val bought = document.get("bought")!!.toString().toInt()
//                    val rate = document.getDouble("rate")!!
//                    val image = document.getString("image")!!
//                    val categoryName = document.getString("categoryName")!!
                    categoriesArr.add(
                        Category(
                            id,
                            name,
                            description
//                            price,
//                            location,
//                            bought,
//                            rate,
//                            image,
//                            categoryName
                        )
                    )
                }
                val categoriesAdapter = CategoryAdapter(categoriesArr)
//                binding.rvCategory.layoutManager = GridLayoutManager(requireContext(), 1)
                binding.rvCategory.layoutManager =LinearLayoutManager(requireContext())
                binding.rvCategory.adapter = categoriesAdapter
          //      hideDialog()
            }.addOnFailureListener { error ->
                Log.e("hzm", error.message.toString())
           //     hideDialog()
                Toast.makeText(requireContext(), "Error while retrieving data", Toast.LENGTH_SHORT).show()
            }



        binding.btnAdd.setOnClickListener {
            startActivity(Intent(requireContext(),AddCategory::class.java))
        }

    }
/*
    private fun showDialog() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Uploading image ...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    private fun hideDialog() {
        if (progressDialog!!.isShowing)
            progressDialog!!.dismiss()
    }*/

    private fun whereCategoryName(categoryName:String){
        //**********
        val categoryList = ArrayList<Category>()

        db.collection("categories").whereEqualTo("name",categoryName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for( document in querySnapshot){
                    var id = document.get("id").toString()
                    var description = document.get("description").toString()
                    var name = document.get("name").toString()


                    var cat = Category(id, name, description)

                    categoryList.add(cat)

                    Toast.makeText(requireContext(), "Category Search Downloaded", Toast.LENGTH_SHORT).show()

                    val employeeAdapter = CategoryAdapter(categoryList)
                    binding.rvCategory.adapter = employeeAdapter
                    binding.rvCategory.layoutManager = LinearLayoutManager(requireContext())

                }

            }
            .addOnFailureListener { exception ->

            }
        //**********
    }
}