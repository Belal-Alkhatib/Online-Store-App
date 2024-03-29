package com.example.and2_finalproject.firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.and2_finalproject.model.Admin
import com.example.and2_finalproject.model.Category
import com.example.and2_finalproject.model.Product
import com.example.and2_finalproject.model.Visitor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseFunctions {
    var db = FirebaseFirestore.getInstance()
    val TAG = "IUG";
    val COLLECTION_PRODUCTS = "products";
    val COLLECTION_ADMINS = "Admins";
    val COLLECTION_VISITORS = "Visitors";
    val COLLECTION_CATEGORIES = "categories";
    val COLLECTION_BOUGHT_PRODUCTS = "bought";



    //category
    fun addCategory(name: String, description: String) {
        var db = FirebaseFirestore.getInstance()
        val category = hashMapOf("name" to name, "description" to description)
        db.collection(COLLECTION_CATEGORIES)
            .add(category)
            .addOnSuccessListener { documentReference ->
                Log.e("hzm", "Added Successfully ${documentReference.id}")

            }
            .addOnFailureListener {
                Log.e("hzm", it.message.toString())
            }
    }

    fun deleteCategoryById(id: String): Boolean {
        var status = true
        db.collection(COLLECTION_CATEGORIES).document(id)
            .delete()
            .addOnSuccessListener {
                Log.e(TAG, "Deleted Successfully")
                status = true
            }.addOnFailureListener { exception ->
                Log.e(TAG, exception.message.toString())
                status = false
            }
        return status
    }

    fun getAllCategories(): ArrayList<Category> {
        val arr = ArrayList<Category>()
        db.collection(COLLECTION_CATEGORIES)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val id = document.id
                    val name = document.getString("name")!!
                    val description = document.getString("description")!!
                    arr.add(Category(id, name, description))
                }
            }.addOnFailureListener { error ->
                Log.e("hzm", error.message.toString())
            }
        return arr
    }




    //Product
    fun addProduct(
        name: String, description: String, price: Double,
        location: String, bought:Int, rate:Double, image: String, categoryName: String
    ) {
        val product = hashMapOf(
            "name" to name,
            "description" to description,
            "price" to price,
            "location" to location,
            "bought" to bought,
            "rate" to rate,
            "image" to image,
            "categoryName" to categoryName
        )
        db.collection(COLLECTION_PRODUCTS)
            .add(product)
            .addOnSuccessListener {
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
                    document.get("bought")!!.toString().toInt(),
                    document.getDouble("rate")!!,
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
                    val id = document.id
                    val name = document.getString("name")!!
                    val description = document.getString("description")!!
                    val price = document.getDouble("price")!!
                    val location = document.getString("location")!!
                    val bought = document.get("bought")!!.toString().toInt()
                    val rate = document.getDouble("rate")!!
                    val image = document.getString("image")!!
                    val categoryName = document.getString("categoryName")!!
                    arr.add(Product(id, name, description, price, location,bought,rate, image, categoryName))
                }
            }.addOnFailureListener { error ->
                Log.e("hzm", error.message.toString())
            }
        return arr
    }

    fun updateProduct(
        oldId: String, name: String, description: String, price: Double,
        location: String, bought:Int, rate:Double, image: String, categoryName: String
    ): Boolean {
        var status = true
        val product = HashMap<String, Any>()
        product["name"] = name
        product["description"] = description
        product["price"] = price
        product["location"] = location
        product["bought"] = bought
        product["rate"] = rate
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

    fun deleteProductById(id: String): Boolean {
        var status = true
        db.collection(COLLECTION_PRODUCTS).document(id)
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




    //visitor
    /**  بعض التعديلات على الدالة
     * @Bilal
    1. حذف الphone number
     2. جع الid الخاص بالمستخدم هو نفسه الcurrent user
     */
    fun addVisitor(
        id: String,
        name: String,
        email: String,
        password: String,
        image: String
    ) {
        var visitor = hashMapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "password" to password,
            "image" to image
        )
        db.collection(COLLECTION_VISITORS).document(id)
            .set(visitor)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "Added Successfully")
            }
            .addOnFailureListener {
                Log.e(TAG, it.message.toString())
            }
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


    /**
    @Bilal
     */
     fun updateVisitor(context:Context, id: String, name: String, email: String, password: String, image: String){
        val user = HashMap<String,Any>()
        user["name"] = name
        user["email"] = email
        user["password"] = password
        user["image"] = image

        db.collection(COLLECTION_VISITORS).document(id)
            .update(user)
            .addOnSuccessListener {
                Toast.makeText(context,"Yor Information Updated",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context,"Yor Information Didn't Updated",Toast.LENGTH_LONG).show()
                Log.e(TAG, exception.message.toString())
            }
    }

    //Admin
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