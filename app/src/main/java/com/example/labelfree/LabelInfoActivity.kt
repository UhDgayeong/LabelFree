package com.example.labelfree

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates

class LabelInfoActivity : AppCompatActivity() {

    private val TAG = "LabelInfo"
    private val db = Firebase.firestore
    private val docName = "코카콜라 제로"

    private var carbo : Float = 0F
    private var sugar : Float = 0F
    private var natrium : Float = 0F
    private var fat : Float = 0F
    private var protein : Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_label_info)

        val carboTxt = findViewById<TextView>(R.id.carboTxt)
        val sugarTxt = findViewById<TextView>(R.id.sugarTxt)
        val natriumTxt = findViewById<TextView>(R.id.natriumTxt)
        val fatTxt = findViewById<TextView>(R.id.fatTxt)
        val proteinTxt = findViewById<TextView>(R.id.proteinTxt)



        db.collection("drinks")
            .whereEqualTo("제품명", docName)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    //Log.d(TAG, "${doc["나트륨"]} => ${doc["탄수화물"]}")
                    carboTxt.text = doc["탄수화물"].toString()
                    carbo = doc["탄수화물"].toString().toFloat()

                    sugarTxt.text = doc["당류"].toString()
                    sugar = doc["당류"].toString().toFloat()

                    natriumTxt.text = doc["나트륨"].toString()
                    natrium = doc["나트륨"].toString().toFloat()

                    fatTxt.text = doc["지방"].toString()
                    fat = doc["지방"].toString().toFloat()

                    proteinTxt.text = doc["단백질"].toString()
                    protein = doc["단백질"].toString().toFloat()

                    Log.d(TAG,"탄수화물 : ${carbo} / 당류 : ${sugar} / 나트륨 : ${natrium} / 지방 : ${fat} / 단백질 : ${protein}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }


    }
}