package com.example.and2_finalproject

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
    val firebaseFunctions = FirebaseFunctions()
    lateinit var binding:ActivityProfileBinding
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
                        URI_IMAGE = uri
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

    override fun onStart() {
        super.onStart()
        val userId = auth.currentUser.toString()
        val visitor = getOneVisitor(userId)

        binding.tvname.setText(visitor.name)
        binding.etEmail.setText(visitor.email)
        binding.etPassword.setText(visitor.password)

        Log.e("bil",visitor.toString())
        if(visitor.image == ""){
            binding.personImage.setImageResource(R.drawable.ic_baseline_dark)
        }else{
            Picasso.get().load(URI_IMAGE).into(binding.personImage);
        }


    }
    private fun getOneVisitor(id: String): Visitor {
        var visitor: Visitor? = null
        db.collection("Visitors")
            .whereEqualTo("id", id)
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
                }
            }.addOnFailureListener { error ->
                Log.e("hzm", error.message.toString())
            }
        return visitor!!
    }
}