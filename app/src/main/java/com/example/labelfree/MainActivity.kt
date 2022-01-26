package com.example.labelfree

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ciderBtn = findViewById<Button>(R.id.ciderBtn)
        ciderBtn.setOnClickListener {
            val intent = Intent(this, LabelInfoActivity::class.java)
            intent.putExtra("Name", "칠성사이다 ECO")
            startActivity(intent)
        }

        val cokeBtn = findViewById<Button>(R.id.cokeBtn)
        cokeBtn.setOnClickListener {
            val intent = Intent(this, LabelInfoActivity::class.java)
            intent.putExtra("Name", "코카콜라 제로")
            startActivity(intent)
        }
    }
}