package com.example.and2_finalproject.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.and2_finalproject.LoginActivity
import com.example.and2_finalproject.ShowProductDetails
import com.example.and2_finalproject.databinding.ProductCardViewBinding
import com.example.and2_finalproject.firebase.FirebaseFunctions
import com.example.and2_finalproject.model.Product
import com.squareup.picasso.Picasso

class ProductAdapter(var data: ArrayList<Product>): RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {
    lateinit var context: Context
    val firebaseFunctions =  FirebaseFunctions()

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

        if (!LoginActivity.isAdmin) {
            holder.cardViewBinding.btnDelete.visibility = View.INVISIBLE
        }

        holder.cardViewBinding.btnDelete.setOnClickListener {

            AlertDialog.Builder(context).apply {
                setTitle("Delete book")
                setMessage("Are you sure that you want to delete this book?")
                setPositiveButton("Yse"){
                        _, _ ->
                    if(firebaseFunctions.deleteProductById(data[position].id)){
                        data.removeAt(position)
                        notifyDataSetChanged()
                    }

                }
                setCancelable(true)
                setNegativeButton("No"){ dialogInterface: DialogInterface, _ ->
                    dialogInterface.cancel()
                }
                create()
                show()
            }
        }


        holder.cardViewBinding.root.setOnClickListener {
            ShowProductDetails.productData = data[position]
            context.startActivity(Intent(context,ShowProductDetails::class.java))
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }




}