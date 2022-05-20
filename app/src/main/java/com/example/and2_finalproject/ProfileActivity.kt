package com.example.and2_finalproject

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.and2_finalproject.databinding.ActivityProfileBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions
import com.example.and2_finalproject.model.Visitor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class ProfileActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null
    val firebaseFunctions = FirebaseFunctions()
    lateinit var binding: ActivityProfileBinding
    var db = FirebaseFirestore.getInstance()

    lateinit var auth: FirebaseAuth
    private var URI_IMAGE: Uri? = null //5.1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val storage = Firebase.storage
        val storageRef = storage.reference
        val imageRef = storageRef.child("images")



        binding.btnEdit.setOnClickListener {
            binding.tvname.isEnabled = true
            binding.btnSave.visibility = View.VISIBLE
            binding.btnEdit.visibility = View.INVISIBLE
            binding.btnCamera.visibility = View.VISIBLE

        }

        binding.btnSave.setOnClickListener {

            if (binding.tvname.text.isNotEmpty() && binding.etEmail.text.isNotEmpty()) {

                showDialog()

                val userId = auth.currentUser!!.uid
                val name = binding.tvname.text.toString()
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                val bitmap = (binding.personImage.drawable as BitmapDrawable).bitmap
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
                    Log.e("hzm", "Image Uploaded Successfully")
                    Toast.makeText(this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
                    childRef.downloadUrl.addOnSuccessListener { uri ->
                        URI_IMAGE = uri
                        firebaseFunctions.updateVisitor(
                            this,
                            userId,
                            name,
                            email,
                            password,
                            uri.toString()
                        )
                        binding.btnSave.visibility = View.GONE

                    }
                    binding.tvname.isEnabled = false
                    binding.btnSave.visibility = View.INVISIBLE
                    binding.btnEdit.visibility = View.VISIBLE
                    binding.btnCamera.visibility = View.INVISIBLE
                    hideDialog()

                }
            } else {
                Toast.makeText(this, "Please fill the data", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnCamera.setOnClickListener {
            getContent.launch("image/*")
        }


        val userId = auth.currentUser!!.uid
        Log.e("hzm", "onStart: $userId" )
        //val visitor = getOneVisitor(userId)

        var visitor: Visitor? = null
        db.collection("Visitors")
            .whereEqualTo("id", userId)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    visitor = Visitor(
                        document.id,
                        document.getString("name")!!,
                        document.getString("email")!!,
                        document.getString("password")!!,
                        document.getString("image")!!
                    )
                    Log.e("hzm", "Added Successfully")

                    binding.tvname.setText(visitor!!.name)
                    binding.etEmail.setText(visitor!!.email)
                    binding.etPassword.setText(visitor!!.password)
                    Log.e("bil", visitor.toString())
                    if (visitor!!.image == "") {
                        binding.personImage.setImageResource(R.drawable.ic_baseline_dark)
                    } else {
                        Picasso.get().load(visitor!!.image).into(binding.personImage);
                    }
                }

            }.addOnFailureListener { error ->
                Log.e("hzm", error.message.toString())
            }




    }


    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            binding.personImage.setImageURI(uri)
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