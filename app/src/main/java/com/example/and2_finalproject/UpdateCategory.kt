package com.example.and2_finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.and2_finalproject.databinding.ActivityUpdateCategoryBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions
import com.example.and2_finalproject.model.Category

class UpdateCategory : AppCompatActivity() {

    companion object {
        var categoryData: Category? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityUpdateCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (categoryData != null) {
            binding.tvName.setText(categoryData!!.name)
            binding.tvDescription.setText(categoryData!!.description)
            binding.tvName.requestFocus()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {

            val name = binding.tvName.text.toString()
            val description = binding.tvDescription.text.toString()

            val firebaseFunctions = FirebaseFunctions()
            if (name.isNotEmpty() && description.isNotEmpty()
            ) {
                //showDialog()
                val newCategory = HashMap<String, Any>()
                newCategory["name"] = name
                newCategory["description"] = description

                val oldId = categoryData!!.id

                firebaseFunctions.db.collection(firebaseFunctions.COLLECTION_CATEGORIES)
                    .document(oldId).update(newCategory)
                    .addOnSuccessListener {
                        Log.e("hzm", "Updated Successfully")
                    }.addOnFailureListener { exception ->
                        Log.e("hzm", exception.message.toString())
                    }
                finish()

            } else {
                Toast.makeText(this, "Please fill the data", Toast.LENGTH_SHORT).show()
            }

        }
    }
}