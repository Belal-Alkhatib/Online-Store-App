package com.example.and2_finalproject.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.and2_finalproject.ShowDetailsShop
import com.example.and2_finalproject.databinding.ProductCardViewBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions
import com.example.and2_finalproject.model.Product
import com.squareup.picasso.Picasso

class ProductAdapter(var data: ArrayList<Product>): RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {
    lateinit var context: Context
    lateinit var firebaseFunctions: FirebaseFunctions

    class MyViewHolder(val cardViewBinding: ProductCardViewBinding): RecyclerView.ViewHolder(cardViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding : ProductCardViewBinding
                = ProductCardViewBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.cardViewBinding.apply {
            tvName.text = data[position].name
            tvPrice.text = data[position].price.toString()
            Picasso.get().load(data[position].image).into(imgProduct);
        }

        holder.cardViewBinding.btnDelete.setOnClickListener {
            firebaseFunctions.deleteProductById(data[position].id)
        }


        holder.cardViewBinding.root.setOnClickListener {
            ShowDetailsShop.productData = data[position]
            context.startActivity(Intent(context,ShowDetailsShop::class.java))
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }




}