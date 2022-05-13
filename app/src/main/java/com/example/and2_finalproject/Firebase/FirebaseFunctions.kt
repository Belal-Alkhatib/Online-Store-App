package com.example.and2_finalproject.Firebase

import android.util.Log
import com.example.and2_finalproject.model.Admin
import com.example.and2_finalproject.model.Category
import com.example.and2_finalproject.model.Product
import com.example.and2_finalproject.model.Visitor
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseFunctions {
    var db = FirebaseFirestore.getInstance()
    val TAG = "IUG";
    val COLLECTION_PRODUCTS = "products";
    val COLLECTION_ADMINS = "Admins";
    val COLLECTION_VISITORS = "Visitors";
    val COLLECTION_CATEGORIES = "categories";

    fun addProduct(product: Product) {
        db.collection(COLLECTION_PRODUCTS)
            .add(product)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "Added Successfully")
            }
            .addOnFailureListener {
                Log.e(TAG, it.message.toString())
            }
    }

    fun addProductd(
        id: String, name: String, description: String, price: Double,
        location: String, image: Int, categoryName: String
    ) {
        val product = hashMapOf(
            "id" to id,
            "name" to name,
            "description" to description,
            "price" to price,
            "location" to location,
            "image" to image,
            "categoryName" to categoryName
        )
        db.collection(COLLECTION_PRODUCTS)
            .add(product)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "Added Successfully")
            }
            .addOnFailureListener {
                Log.e(TAG, it.message.toString())
            }
    }

    fun addCategory(name: String, description: String,) {
        val category = hashMapOf("name" to name , "description" to description)
        db.collection(COLLECTION_CATEGORIES)
            .add(category)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "Added Successfully")
            }
            .addOnFailureListener {
                Log.e(TAG, it.message.toString())
            }
    }

    fun addAdmin( name:String,  password:String) {
        var admin = hashMapOf("name" to name, "password" to password)
        db.collection(COLLECTION_ADMINS)
            .add(admin)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "Added Successfully")
            }
            .addOnFailureListener {
                Log.e(TAG, it.message.toString())
            }
    }

    //    private fun addAdmind(name:String , pass:String) {
//        val admin = hashMapOf("name" to name,"password" to pass)
//        db.collection(COLLECTION_ADMINS)
//            .add(admin)
//            .addOnSuccessListener { documentReference ->
//                Log.e(TAG, "Added Successfully")
//            }
//            .addOnFailureListener {
//                Log.e(TAG,it.message.toString())
//            }
//    }
    fun addVisitor(visitor: Visitor) {
        db.collection(COLLECTION_VISITORS)
            .add(visitor)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "Added Successfully")
            }
            .addOnFailureListener {
                Log.e(TAG, it.message.toString())
            }
    }

    fun getOneProducts(id: String) {
        db.collection(COLLECTION_PRODUCTS)
//            .document(id) بتعمل خطء انا مش فاهمه
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    var id = document.getString("id")
                    var name = document.getString("name")
                    var description = document.getString("description")
                    var price = document.getDouble("price")
                    var location = document.getString("location")
                    var image = document.getString("image")?.toInt()
                    var categoryName = document.getString("categoryName")
//                    var id:String , var name:String , var description:String , var price:Double,
//                    var location:String , var image:Int , var categoryName:String
                }
            }.addOnFailureListener { error ->
                Log.e("hzm", error.message.toString())
            }
    }

    fun getAllProducts() {
        db.collection(COLLECTION_PRODUCTS)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    var id = document.getString("id")
                    var name = document.getString("name")
                    var description = document.getString("description")
                    var price = document.getDouble("price")
                    var location = document.getString("location")
                    var image = document.getString("image")?.toInt()
                    var categoryName = document.getString("categoryName")
//                    var id:String , var name:String , var description:String , var price:Double,
//                    var location:String , var image:Int , var categoryName:String
                }
            }.addOnFailureListener { error ->
                Log.e("hzm", error.message.toString())
            }
    }

    //    delete product
    fun deleteProductById(id: String) {
        db.collection("product").document(id)
            .delete()
            .addOnSuccessListener { it ->
                Log.e(TAG, "Deleted Successfully")
            }.addOnFailureListener { exception ->
                Log.e(TAG, exception.message.toString())
            }
    }

    fun updateProduct(
        oldId: String, name: String, description: String, price: Double,
        location: String, image: Int, categoryName: String
    ) {
        val product = HashMap<String, Any>()
        product["name"] = name
        product["description"] = description
        product["price"] = price
        product["location"] = location
        product["image"] = image
        product["categoryName"] = categoryName


        db.collection(COLLECTION_PRODUCTS).document(oldId).update(product)
            .addOnSuccessListener {
                Log.e(TAG, "Updated Successfully")
            }.addOnFailureListener {exception->
                Log.e(TAG, exception.message.toString())
            }
    }

}