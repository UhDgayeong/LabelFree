package com.haram.labelfree.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class DrinkRepository {

    val db = Firebase.firestore

    private var drinkDocNames = arrayListOf<String>() // 제품명 리스트
    private var drinkMap = mapOf<String, String>()
    //private var drinkDocs = HashMap<String, Map<String, Any>>()

    suspend fun getFirebaseData(): Boolean {

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
    }

    fun getDrinkNameList(): ArrayList<String> {
        Log.d("testingggg", drinkDocNames.toString())
        return drinkDocNames
    }

    fun getDrinkInfo(docName: String) {
        db.collection("drinks")
            .whereEqualTo("제품명", docName)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    drinkMap = mapOf(
                        "productName" to doc.data["제품명"].toString(),
                        "carbo" to doc.data["탄수화물"].toString(),
                        "sugar" to doc.data["당류"].toString(),
                        "natrium" to doc.data["나트륨"].toString(),
                        "fat" to doc.data["지방"].toString(),
                        "protein" to doc.data["단백질"].toString(),
                        "date" to doc.data["유통기한"].toString(),
                        "ml" to doc.data["총 내용량"].toString(),
                        "cal" to doc.data["총 칼로리"].toString(),
                        "raw" to doc.data["원재료명"].toString(),
                        "caution" to doc.data["주의사항"].toString(),
                        "type" to doc.data["식품유형"].toString(),
                        "company" to doc.data["제조회사"].toString(),
                        "manufac" to doc.data["제조원"].toString(),
                        "contmat" to doc.data["용기재질"].toString(),
                        "source" to doc.data["출처"].toString()
                    )
                }
            }
            .addOnFailureListener { exception ->
                Log.w("RepoError", "Error getting documents.", exception)
            }
    }

    fun getDrinkInfoMap(): Map<String, String> {
        return drinkMap
    }



}