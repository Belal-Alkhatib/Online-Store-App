package com.example.and2_finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.and2_finalproject.databinding.ActivitySignUpBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var binding : ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //startActivity(Intent(this,MainActivity::class.java)) // --> what is that


        binding.btnAdmin.setOnClickListener {
            val i = Intent(this,LoginActivity::class.java)
            startActivity(i)
        }

        binding.tvLoginLink.setOnClickListener {
            val i = Intent(this,LoginActivity::class.java)
            startActivity(i)
        }

        auth = FirebaseAuth.getInstance()
        //val auth = Firebase.auth
        binding.btnSignUp.setOnClickListener {
            if(binding.etFullName.text.isNotEmpty() && binding.etEmail.text.isNotEmpty() &&
                binding.etPassword.text.isNotEmpty()){

                val name = binding.etFullName.text.toString()
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()


                authSingUp(name,email,password)
            }else{
                Toast.makeText(this, "Please Fill in The Required Fields", Toast.LENGTH_SHORT).show()

            }
        }
    }


    // authSingUp(binding.etEmail.text.toString(),binding.etPassword.text.toString())
         private fun authSingUp(name:String,email:String ,password:String) {
             Log.e( "authSingUp: ",email )
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e("hzm", "createUserWithEmail:success")

                        val firebaseFun = FirebaseFunctions()
                        val userId = auth.currentUser.toString()
                        Log.e("hzm", "authSingUp: $userId")

                        var visitor = hashMapOf(
                            "id" to userId,
                            "name" to name,
                            "email" to email,
                            "password" to password,
                            "image" to ""
                        )
                        firebaseFun.db.collection(firebaseFun.COLLECTION_VISITORS).document(userId)
                            .set(visitor)
                            .addOnSuccessListener { documentReference ->
                                Log.e("hzm", "Added Successfully $userId")
                                val i = Intent(this,ProfileActivity::class.java)
                                startActivity(i)
                            }
                            .addOnFailureListener {
                                Log.e("hzm", it.message.toString())
                            }



                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e("hzm", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }



}