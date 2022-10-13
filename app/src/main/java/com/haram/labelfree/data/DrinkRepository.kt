package com.haram.labelfree.data

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class DrinkRepository {

    val db = Firebase.firestore

    // 컬렉션의 모든 문서 가져오기
//    lateinit var drinkDocs: ArrayList<String> by lazy {
//        getData()
//    }

    private var drinkDocs = arrayListOf<String>()
    //private var drinkDocs = HashMap<String, Map<String, Any>>()

    suspend fun getFirebaseData(): MutableList<DocumentSnapshot> {
        // placesRef : db.collection 인듯. 여기다 .get().addOn리스너 있어서..

        val snapshot = db.collection("drinks").get().await()
        Log.d("doctest", snapshot.documents.toString())
        return snapshot.documents

//        db.collection("drinks")
//            .get()
//            .addOnSuccessListener { documents ->
//                for (doc in documents) {
//                    //Log.d("doctest", "${doc.id} => ${doc.data}")
//                    //drinkDocs.put(doc.id, doc.data)
//                    drinkDocs.add(doc.id)
//                    //Log.d("testinggg", drinkDocs.toString())
//                }
//                Log.d("testinggg", drinkDocs.toString())
//            }
//            .addOnFailureListener { exception ->
//                Log.w("doctest", "Error getting documents: ", exception)
//            }

        //Log.d("testinggg", drinkDocs.toString())
        // 데이터를 다 불러오기 전에 리턴을 실행하므로 drinkDocs의 사이즈는 0
    }

    fun getData(): ArrayList<String> {
        Log.d("testingggg", drinkDocs.toString())
        return drinkDocs
    }

}