package com.example.and2_finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.and2_finalproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.tvSignUpLink.setOnClickListener {
            val i = Intent(this,SignUpActivity::class.java)
            startActivity(i)
        }


        binding.btnSignin.setOnClickListener {
            if(binding.etEmail.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()){

                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                if(email == "admin" && password == "admin"){ // اذا تحقق هذا الشرط يكون مسجل الدخول أدمن
                    val i = Intent(this, Dashboard::class.java)
                    startActivity(i)
                }else{
                    authLogin(email,password)
                }


            }else{
                Toast.makeText(this, "Please Fill in The Required Fields", Toast.LENGTH_SHORT).show()

            }
        }
    }


    //authLogin(binding.etEmail.text.toString(),binding.etPassword.text.toString())

    private fun authLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e("hzm", "signInWithEmail:success")
                    val user = auth.currentUser

                    val i = Intent(this,MainActivity::class.java)
                    startActivity(i)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e("hzm", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }



    }




