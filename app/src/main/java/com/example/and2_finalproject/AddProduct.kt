package com.example.and2_finalproject

import android.R
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.and2_finalproject.databinding.ActivityAddProductBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions
import com.example.and2_finalproject.model.Category
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class AddProduct : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null
    lateinit var binding: ActivityAddProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storage = Firebase.storage
        val storageRef = storage.reference
        val imageRef = storageRef.child("images")

        val firebaseFunctions = FirebaseFunctions()
        val categoriesNames = ArrayList<String>()

        firebaseFunctions.db.collection(firebaseFunctions.COLLECTION_CATEGORIES)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val id = document.id
                    val name = document.getString("name")!!
                    val description = document.getString("description")!!
                    categoriesNames.add(name)
                    val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, categoriesNames)
                    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                    binding.spCategories.adapter = adapter
                }
            }.addOnFailureListener { error ->
                Log.e("hzm", error.message.toString())
            }

        binding.imgProduct.setOnClickListener {
            getContent.launch("image/*")
        }
        binding.btnSave.setOnClickListener {
            val name = binding.tvName.text.toString()
            val description = binding.tvDescription.text.toString()
            val price = binding.tvPrice.text.toString()
            val location = binding.tvLocation.text.toString()
            val category = binding.spCategories.selectedItem.toString()

            if (name.isNotEmpty() && description.isNotEmpty() &&
                price.isNotEmpty() && location.isNotEmpty()
            ) {
                showDialog()
                // Get the data from an ImageView as bytes
                val bitmap = (binding.imgProduct.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val data = baos.toByteArray()

                val childRef = imageRef.child(System.currentTimeMillis().toString() + "_images.png")
                var uploadTask = childRef.putBytes(data)
                uploadTask.addOnFailureListener { exception ->
                    Log.e("hzm", exception.message!!)
                    hideDialog()
                }
                    .addOnSuccessListener {
                        Log.e("hzm", "Image Uploaded Successfully")
                        childRef.downloadUrl.addOnSuccessListener { uri ->

                            firebaseFunctions.addProduct(
                                name,
                                description,
                                price.toDouble(),
                                location,
                                bought = 0,
                                rate = 0.0,
                                uri!!.toString(),
                                category
                            )
                        }
                        hideDialog()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }
            } else {
                Toast.makeText(this, "Please fill the data", Toast.LENGTH_SHORT).show()
            }
        }


    }


    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            binding.imgProduct.setImageURI(uri)
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