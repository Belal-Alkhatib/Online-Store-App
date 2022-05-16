package com.example.and2_finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.and2_finalproject.databinding.ActivityAddCategoryBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions

class AddCategory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseFunctions = FirebaseFunctions()

        binding.btnSave.setOnClickListener {
            if(binding.tvName.text.isNotEmpty() && binding.tvDescription.text.isNotEmpty()){
                firebaseFunctions.addCategory(binding.tvName.text.toString(),binding.tvDescription.text.toString())
            }
        }
    }
}