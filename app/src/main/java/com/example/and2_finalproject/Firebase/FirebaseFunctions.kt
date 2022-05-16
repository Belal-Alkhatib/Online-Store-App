package com.example.and2_finalproject.firebase

import android.util.Log
import android.widget.Toast
import com.example.and2_finalproject.model.Admin
import com.example.and2_finalproject.model.Category
import com.example.and2_finalproject.model.Product
import com.example.and2_finalproject.model.Visitor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseFunctions {
    var db = FirebaseFirestore.getInstance()
    val TAG = "IUG";
    val COLLECTION_PRODUCTS = "products";
    val COLLECTION_ADMINS = "Admins";
    val COLLECTION_VISITORS = "Visitors";
    val COLLECTION_CATEGORIES = "categories";


    fun addProduct(
        id: String, name: String, description: String, price: Double,
        location: String, image: String, categoryName: String
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

    fun addCategory(name: String, description: String) {
        val category = hashMapOf("name" to name, "description" to description)
        db.collection(COLLECTION_CATEGORIES)
            .add(category)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "Added Successfully")
            }
            .addOnFailureListener {
                Log.e(TAG, it.message.toString())
            }
    }


    fun addVisitor(
        id: String,
        name: String,
        email: String,
        phoneNumber: String,
        password: String,
        image: String
    ) {
        var visitor = hashMapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "phoneNumber" to phoneNumber,
            "password" to password,
            "image" to image
        )
        db.collection(COLLECTION_VISITORS)
            .add(visitor)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "Added Successfully")
            }
            .addOnFailureListener {
                Log.e(TAG, it.message.toString())
            }
    }

    fun getOneProducts(id: String): Product {
        var product: Product? = null
        db.collection(COLLECTION_PRODUCTS)
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                product = Product(
                    document.id,
                    document.getString("name")!!,
                    document.getString("description")!!,
                    document.getDouble("price")!!,
                    document.getString("location")!!,
                    document.getString("image")!!,
                    document.getString("categoryName")!!
                )
                Log.e(TAG, "Added Successfully")
            }.addOnFailureListener { error ->
                Log.e("hzm", error.message.toString())
            }
        return product!!
    }

    fun getAllProducts(): ArrayList<Product> {
        val arr = ArrayList<Product>()
        db.collection(COLLECTION_PRODUCTS)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    var id = document.getString("id")!!
                    var name = document.getString("name")!!
                    var description = document.getString("description")!!
                    var price = document.getDouble("price")!!
                    var location = document.getString("location")!!
                    var image = document.getString("image")!!
                    var categoryName = document.getString("categoryName")!!
                    arr.add(Product(id, name, description, price, location, image, categoryName))
                }
            }.addOnFailureListener { error ->
                Log.e("hzm", error.message.toString())
            }
        return arr
    }

    fun getOneVisitor(id: String): Visitor {
        var visitor: Visitor? = null
        db.collection(COLLECTION_PRODUCTS)
            .whereEqualTo("id", id)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    visitor = Visitor(
                        document.id,
                        document.getString("name")!!,
                        document.getString("email")!!,
                        document.getString("password")!!,
                        document.getString("image")!!
                    )
                    Log.e(TAG, "Added Successfully")
                }
            }.addOnFailureListener { error ->
                Log.e("hzm", error.message.toString())
            }
        return visitor!!
    }

    fun updateProduct(
        oldId: String, name: String, description: String, price: Double,
        location: String, image: String, categoryName: String
    ): Boolean {
        var status = true
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
                status = true
            }.addOnFailureListener { exception ->
                Log.e(TAG, exception.message.toString())
                status = false
            }
        return status
    }

    //    delete product
    fun deleteProductById(id: String): Boolean {
        var status = true
        db.collection("product").document(id)
            .delete()
            .addOnSuccessListener { it ->
                Log.e(TAG, "Deleted Successfully")
                status = true
            }.addOnFailureListener { exception ->
                Log.e(TAG, exception.message.toString())
                status = false
            }
        return status
    }


    fun getOneAdmin(id: String): Admin {
        var admin: Admin? = null
        db.collection(COLLECTION_PRODUCTS)
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                admin = Admin(
                    document.id,
                    document.getString("name")!!,
                    document.getString("password")!!
                )
                Log.e(TAG, "Added Successfully")
            }.addOnFailureListener { error ->
                Log.e("hzm", error.message.toString())
            }
        return admin!!
    }

    fun addAdmin(name: String, password: String) {
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


}