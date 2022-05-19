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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseFun = FirebaseFunctions()

        //startActivity(Intent(this,AddProduct::class.java)) // --> what is that


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
                val userId = auth.currentUser

                authSingUp(email,password)
                firebaseFun.addVisitor(userId.toString(),name,email,password,"")
            }else{
                Toast.makeText(this, "Please Fill in The Required Fields", Toast.LENGTH_SHORT).show()

            }
        }
    }


    // authSingUp(binding.etEmail.text.toString(),binding.etPassword.text.toString())
         private fun authSingUp(email:String ,password:String) {
             Log.e( "authSingUp: ",email )
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e("hzm", "createUserWithEmail:success")
                        val i = Intent(this,MainActivity::class.java)
                        startActivity(i)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e("hzm", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }



}