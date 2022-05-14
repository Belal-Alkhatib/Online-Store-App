package com.example.and2_finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.and2_finalproject.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        //val auth = Firebase.auth
    }
         private fun authSingUp(email:String ,password:String) {

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e("hzm", "createUserWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user!!)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e("hzm", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }

//
    override fun onStart() {
        super.onStart()
        val currentUser  = auth.currentUser
        if(currentUser!=null)
            updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser){
        val i = Intent(this,MainActivity::class.java)
        //i.putExtra("user",user)
        startActivity(i)
    }

}