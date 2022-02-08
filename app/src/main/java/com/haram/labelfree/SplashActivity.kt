package com.haram.labelfree

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import com.haram.labelfree.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    //lateinit var list : ArrayList<String>
    val TAG = "SplashActivity"

    companion object {
        lateinit var list : ArrayList<String>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        list = ArrayList()
        settingList()

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            //intent.putExtra("list", list as ArrayList<String>)
            startActivity(intent)
            finish()
        }, 3000)
    }

    private fun settingList() {
        val db = Firebase.firestore
        db.collection("drinks")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    list.add(doc.data["제품명"].toString())
                    println("########## added!")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        println("the size is .. ${list.size}")
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}