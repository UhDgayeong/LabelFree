package com.example.labelfree

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var list : ArrayList<String>
    lateinit var autoTextView : AutoCompleteTextView
    lateinit var searchBtn : ImageButton

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = SplashActivity.list

        //list = intent.getStringArrayListExtra("list") as ArrayList<String>
        val listSize = list.size
        Log.d(TAG, "the size is ... ${listSize}")


        autoTextView = findViewById(R.id.autoTextView)

        val str = "'${list[rand(0, listSize)]}'를 검색해 보세요!"

        autoTextView.hint = str
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        autoTextView.setAdapter(adapter)
        autoTextView.threshold = 1

        autoTextView.onItemClickListener = AdapterView.OnItemClickListener{ parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
            val intent = Intent(this, LabelInfoActivity::class.java)
            intent.putExtra("Name", selectedItem)
            startActivity(intent)
        }

        searchBtn = findViewById(R.id.searchBtn)
        searchBtn.setOnClickListener {
            if (list.contains(autoTextView.text.trim().toString().uppercase(Locale.getDefault()))) {
                val intent = Intent(this, LabelInfoActivity::class.java)
                intent.putExtra("Name", autoTextView.text.trim().toString().uppercase(Locale.getDefault()))
                startActivity(intent)
            }

            else {
                Toast.makeText(this, "해당 음료 정보가 존재하지 않아요 :(", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun rand(from: Int, to: Int) : Int {
        val random = Random()
        return random.nextInt(to - from) + from
    }
}