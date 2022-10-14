package com.haram.labelfree.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class DrinkRepository {

    val db = Firebase.firestore

    // 컬렉션의 모든 문서 가져오기
//    lateinit var drinkDocs: ArrayList<String> by lazy {
//        getData()
//    }

    private var drinkDocNames = arrayListOf<String>()
    //private var drinkDocs = HashMap<String, Map<String, Any>>()

    suspend fun getFirebaseData(): Boolean {
        // placesRef : db.collection 인듯. 여기다 .get().addOn리스너 있어서..

        return try {
            val snapshot = db.collection("drinks").get().await()
            //Log.d("doctest", snapshot.documents.toString())
            //Log.d("doctest", snapshot.documents.get(0).toString())

            for (doc in snapshot.documents) {
                // 전체 데이터 : Log.d("testingDoc", doc.data.toString())
                Log.d("testingDoc", doc.data?.get("제품명") as String)
                drinkDocNames.add(doc.data!!.get("제품명") as String)
            }

            true
        } catch (e: FirebaseFirestoreException) {
            Log.e("doctest", "ErrorE:" + e.message.toString())
            false
        }


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
        Log.d("testingggg", drinkDocNames.toString())
        return drinkDocNames
    }

}