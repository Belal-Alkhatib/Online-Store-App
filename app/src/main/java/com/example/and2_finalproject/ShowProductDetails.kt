package com.example.and2_finalproject

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.and2_finalproject.databinding.ActivityShowProductDetailsBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions
import com.example.and2_finalproject.model.Product
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class ShowProductDetails : AppCompatActivity() {

    lateinit var binding:ActivityShowProductDetailsBinding
    private var progressDialog: ProgressDialog? = null

    companion object {
        var productData: Product? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (productData != null) {
            Picasso.get().load(productData!!.image).into(binding.imgShop)
            binding.tvName.setText(productData!!.name)
            binding.tvDescription.setText(productData!!.description)
            binding.tvLocation.text = productData!!.location
            binding.tvPrice.setText(productData!!.price.toString())
            binding.tvBought.text = productData!!.bought.toString()
            binding.tvRating.text = productData!!.rate.toString()
            binding.tvCategory.text = productData!!.categoryName.toString()
        }

        binding.btnCanel.setOnClickListener {
            finish()
        }

        binding.btnEdit.setOnClickListener {
            binding.btnEdit.visibility = View.INVISIBLE
            binding.btnSave.visibility = View.VISIBLE

            binding.tvName.isEnabled = true
            binding.tvDescription.isEnabled = true
            binding.tvPrice.isEnabled = true

            binding.tvName.requestFocus()

            binding.imgShop.setOnClickListener {
                getContent.launch("image/*")
            }
        }

        val storage = Firebase.storage
        val storageRef = storage.reference
        val imageRef = storageRef.child("images")

        val firebaseFunctions = FirebaseFunctions()

        binding.btnSave.setOnClickListener {

            val name = binding.tvName.text.toString()
            val description = binding.tvDescription.text.toString()
            val price = binding.tvPrice.text.toString()
            val location = binding.tvLocation.text.toString()
            val bought = binding.tvBought.text.toString()
            val rate = binding.tvRating.text.toString()
            val category = binding.tvCategory.text.toString()

            if (name.isNotEmpty() && description.isNotEmpty() &&
                price.isNotEmpty() && location.isNotEmpty()
            ) {
                showDialog()
                // Get the data from an ImageView as bytes
                val bitmap = (binding.imgShop.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val data = baos.toByteArray()

                val childRef = imageRef.child(System.currentTimeMillis().toString() + "_images.png")
                var uploadTask = childRef.putBytes(data)
                uploadTask.addOnFailureListener { exception ->
                    Log.e("hzm", exception.message!!)
                    hideDialog()
                    // Handle unsuccessful uploads
                }.addOnSuccessListener {
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                    // ...
                    Log.e("hzm", "uploaded image Successfully")
                    Toast.makeText(this, "updated Successfully", Toast.LENGTH_SHORT).show()
                    childRef.downloadUrl.addOnSuccessListener { uri ->

                        firebaseFunctions.updateProduct(
                            productData!!.id,
                            name,
                            description,
                            price.toDouble(),
                            location,
                            bought.toInt(),
                            rate.toDouble(),
                            uri!!.toString(),
                            category
                        )
                    }
                    hideDialog()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }
            }else{
                Toast.makeText(this, "Please fill the data", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            binding.imgShop.setImageURI(uri)
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