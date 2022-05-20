package com.example.and2_finalproject

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.and2_finalproject.databinding.ActivityProfileBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.net.URI

class ProfileActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val storage = Firebase.storage
        val storageRef = storage.reference
        val imageRef = storageRef.child("images")

        val firebaseFunctions = FirebaseFunctions()

        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
            binding.personImage.setImageURI(uri)
        }

        binding.btnEdit.setOnClickListener {
            binding.tvname.isEnabled = true
            binding.etEmail.isEnabled = true
            binding.btnSave.visibility = View.VISIBLE

        }

        binding.btnSave.setOnClickListener {

            if(binding.tvname.text.isNotEmpty() && binding.etEmail.text.isNotEmpty()){

                val userId = auth.currentUser.toString()
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

                    // Handle unsuccessful uploads
                }.addOnSuccessListener {
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                    // ...
                    Log.e("hzm", "Image Uploaded Successfully")
                    Toast.makeText(this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
                    childRef.downloadUrl.addOnSuccessListener { uri ->
                        firebaseFunctions.updateVisitor(this, userId, name, email, password, uri.toString())
                        binding.btnSave.visibility = View.GONE

                    }

                }
            }else{
                Toast.makeText(this, "Please fill the data", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnCamera.setOnClickListener {
            getContent.launch("images/*")
        }

    }
}