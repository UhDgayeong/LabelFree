package com.haram.labelfree.data

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DrinkRepository {

    val db = Firebase.firestore

    // 컬렉션의 모든 문서 가져오기
//    lateinit var drinkDocs: ArrayList<String> by lazy {
//        getData()
//    }

    private var drinkDocs = arrayListOf<String>()

    fun getData(): ArrayList<String> {
        db.collection("drinks")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    Log.d("doctest", "${doc.id} => ${doc.data}")
                    drinkDocs.add(doc.data.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.w("doctest", "Error getting documents: ", exception)
                drinkDocs.add("error")
            }

        return drinkDocs
    }

}