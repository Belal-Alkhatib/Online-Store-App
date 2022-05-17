package com.example.and2_finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.and2_finalproject.databinding.ActivityShowDetailsShopBinding
import com.example.and2_finalproject.model.Product
import com.squareup.picasso.Picasso

class ShowDetailsShop : AppCompatActivity() {

    companion object {
        var productData: Product? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityShowDetailsShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (productData != null) {
            Picasso.get().load(productData!!.image).into(binding.imgShop)
            binding.tvName.text = binding.tvName.text.toString() + productData!!.name
            binding.tvDescription.text = binding.tvDescription.text.toString() + productData!!.description
            binding.tvLocation.text = productData!!.location
            binding.tvPrice.text = productData!!.price.toString()
            binding.tvBought.text = productData!!.bought.toString()
            binding.tvRating.text = productData!!.rate.toString()
            binding.tvCategory.text = productData!!.categoryName.toString()
        }

    }
}