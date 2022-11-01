package com.haram.labelfree.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.haram.labelfree.R
import com.haram.labelfree.databinding.ActivityMainBinding
import com.haram.labelfree.ui.viewmodel.DrinkViewModel
import kotlinx.coroutines.runBlocking
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var list : ArrayList<String>
    lateinit var autoTextView : AutoCompleteTextView
    lateinit var searchBtn : ImageButton
    lateinit var mAdView : AdView
    lateinit var clearBtn : ImageButton
    lateinit var drawerLayout : DrawerLayout
    lateinit var navView : NavigationView
    lateinit var auth : FirebaseAuth

    private val GOOGLE_SIGN_IN = 1

    val viewModel: DrinkViewModel by viewModels()

    val TAG = "MainActivity"
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        runBlocking {
            //getRepoData()
            viewModel.reload()
            list = viewModel.getDrinkNameList()
        }

        auth = FirebaseAuth.getInstance()

        for (l in list) {
            Log.d("listTest", "$l : ${l[0].code}")
        }

        clearBtn = binding.clearBtn
        clearBtn.visibility = View.INVISIBLE

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val listSize = list.size

        autoTextView = binding.autoTextView
        clearBtn.setOnClickListener {
            autoTextView.text = null
        }

        autoTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                clearBtn.visibility = View.VISIBLE
                if (autoTextView.text.isBlank()) {
                    clearBtn.visibility = View.INVISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                clearBtn.visibility = View.INVISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearBtn.visibility = View.VISIBLE
            }
        })

        val str = "'${list[rand(0, listSize)]}'를(을) 검색해 보세요!"

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
        autoTextView.setOnKeyListener { _, keyCode, event ->
            when {
                //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {
                    //perform an action here e.g. a send message button click
                    //sendButton.performClick()
                    if (list.contains(autoTextView.text.trim().toString().uppercase(Locale.getDefault()))) {
                        val intent = Intent(this, LabelInfoActivity::class.java)
                        intent.putExtra("Name", autoTextView.text.trim().toString().uppercase(Locale.getDefault()))
                        startActivity(intent)
                    }

                    else {
                        Toast.makeText(this, "해당 음료 정보가 존재하지 않아요 :(", Toast.LENGTH_SHORT).show()
                    }

                    //return true
                    return@setOnKeyListener true
                }
                else -> false
            }
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

        val drawer_btn = findViewById<ImageView>(R.id.drawer_btn)
        drawerLayout = binding.drawerLayout
        drawer_btn.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START)
            } else {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        navView = binding.navView
        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.menu_list -> {
                    val intent = Intent(this, ProductListActivity::class.java)
                    startActivity(intent)
                }

                R.id.menu_bookmark -> Toast.makeText(applicationContext, "개발 예정", Toast.LENGTH_SHORT).show()

                R.id.menu_info -> {
                    val intent = Intent(this, AboutAppActivity::class.java)
                    startActivity(intent)
                }

                R.id.menu_sns -> {
                    val uri = Uri.parse("http://instagram.com/labelfree_haram")
                    val likeIng = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(likeIng)
                }
            }
            true
        }
        navView.itemIconTintList = null

        Log.d("emailtest", intent.getStringExtra("email")!!)

        binding.logoutLayout.setOnClickListener {
            signOut()
        }
    } // OnCreate



    private fun signOut() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        // 메뉴가 열려있을 경우 메뉴를 닫음
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        // 앱 종료하려면 뒤로가기 두 번 누르기
        else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "뒤로가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        }
    }

    private fun rand(from: Int, to: Int) : Int {
        val random = Random()
        return random.nextInt(to - from) + from
    }
}