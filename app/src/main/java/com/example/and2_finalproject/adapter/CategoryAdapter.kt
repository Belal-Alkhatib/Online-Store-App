package com.example.and2_finalproject.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.and2_finalproject.LoginActivity
import com.example.and2_finalproject.databinding.CategoryCardViewBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions
import com.example.and2_finalproject.model.Category
import kotlin.collections.ArrayList

class CategoryAdapter(var data: ArrayList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {
    lateinit var context: Context
    lateinit var firebaseFunctions: FirebaseFunctions

    class MyViewHolder(val cardViewBinding: CategoryCardViewBinding) :
        RecyclerView.ViewHolder(cardViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding: CategoryCardViewBinding =
            CategoryCardViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.cardViewBinding.apply {
            tvName.text = data[position].name
            tvDescription.text = data[position].description
        }

        if (!LoginActivity.isAdmin) {
            holder.cardViewBinding.btnDelete.visibility = View.INVISIBLE
        }

        holder.cardViewBinding.btnDelete.setOnClickListener {
            firebaseFunctions.deleteCategoryById(data[position].id)
        }

        /*holder.cardViewBinding.root.setOnClickListener {

        }*/

    }

    override fun getItemCount(): Int {
        return data.size
    }


}