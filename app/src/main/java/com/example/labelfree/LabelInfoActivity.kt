package com.example.labelfree

import android.content.ContentValues.TAG
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.properties.Delegates

class Product(
    var name:String = "",
    var fat:Float = 0F,
    var cargo:Float = 0F,
    var protein:Float = 0F,
    var sugar:Float = 0F,
    var natrium:Float = 0F,
)

class LabelInfoActivity : AppCompatActivity() {

    private val TAG = "LabelInfoTest"
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



        val productList = ArrayList<Map<String, Any>>()

        db.collection("drinks").get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    productList.add(mapOf(
                        "productName" to doc.data["제품명"].toString(),
                        "cargo" to doc.data["탄수화물"].toString().toFloat(),
                        "sugar" to doc.data["당류"].toString().toFloat(),
                        "natrium" to doc.data["나트륨"].toString().toFloat(),
                        "fat" to doc.data["지방"].toString().toFloat(),
                        "protein" to doc.data["단백질"].toString().toFloat(),
                    ))
                    // Log.d(TAG, "onCreate: ${doc.data}")
                }

                for (pd in productList) {
                    Log.d(TAG,"탄수화물 : ${pd["cargo"]} / 당류 : ${pd["sugar"]} / 나트륨 : ${pd["natrium"]} / 지방 : ${pd["fat"]} / 단백질 : ${pd["protein"]}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

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