package com.haram.labelfree.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class DrinkRepository {

    val db = Firebase.firestore

    private var drinkDocNames = arrayListOf<String>() // 제품명 리스트
    private var drinkCompanies = arrayListOf<String>()
    private var drinkMap = mapOf<String, String>()

    suspend fun getFirebaseData(): Boolean {

        return try {
            val snapshot = db.collection("drinks").get().await()
            //Log.d("doctest", snapshot.documents.toString())
            //Log.d("doctest", snapshot.documents.get(0).toString())

            for (doc in snapshot.documents) {
                // 전체 데이터 : Log.d("testingDoc", doc.data.toString())
                Log.d("testingDoc", doc.data?.get("제품명") as String)
                drinkDocNames.add(doc.data!!.get("제품명") as String)
                drinkCompanies.add(doc.data!!.get("제조회사") as String)
            }

            true
        } catch (e: FirebaseFirestoreException) {
            Log.e("doctest", "ErrorE:" + e.message.toString())
            false
        }
    }

    fun getDrinkNameList(): ArrayList<String> {
        return drinkDocNames
    }

    fun getDrinkCompanyList(): ArrayList<String> {
        return drinkCompanies
    }

    suspend fun getDrinkInfo(docName: String): Boolean {

        return try {
            val snapshot = db.collection("drinks").document(docName).get().await()

            drinkMap = mapOf(
                "productName" to snapshot.get("제품명").toString(),
                "carbo" to snapshot.get("탄수화물").toString(),
                "sugar" to snapshot.get("당류").toString(),
                "natrium" to snapshot.get("나트륨").toString(),
                "fat" to snapshot.get("지방").toString(),
                "protein" to snapshot.get("단백질").toString(),
                "date" to snapshot.get("유통기한").toString(),
                "ml" to snapshot.get("총 내용량").toString(),
                "cal" to snapshot.get("총 칼로리").toString(),
                "raw" to snapshot.get("원재료명").toString(),
                "caution" to snapshot.get("주의사항").toString(),
                "type" to snapshot.get("식품유형").toString(),
                "company" to snapshot.get("제조회사").toString(),
                "manufac" to snapshot.get("제조원").toString(),
                "contmat" to snapshot.get("용기재질").toString(),
                "source" to snapshot.get("출처").toString()
            )

            true
        } catch (e: FirebaseFirestoreException) {
            Log.e("fbError", "ErrorE: " + e.message.toString())
            false
        }
    }

    fun getDrinkInfoMap(): Map<String, String> {
        return drinkMap
    }



}