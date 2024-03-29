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
import com.facebook.login.LoginManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.haram.labelfree.R
import com.haram.labelfree.databinding.ActivityMainBinding
import com.haram.labelfree.ui.viewmodel.DrinkViewModel
import kotlinx.coroutines.runBlocking
import java.util.*

// 메인 홈 화면
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
    lateinit var user: FirebaseUser

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
        user = auth.currentUser!!

        clearBtn = binding.clearBtn
        clearBtn.visibility = View.INVISIBLE

        MobileAds.initialize(this) {}

        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val listSize = list.size

        autoTextView = binding.autoTextView
        clearBtn.setOnClickListener {
            autoTextView.text = null
        }

        // 검색창에 아무것도 치지 않았을 때는 모두 지우는 버튼(X버튼)이 안 보이게 하고,
        // 검색창이 비어있지 않은 경우에는 X버튼이 보이게 설정
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

        // 검색창의 힌트 설정
        val str = "'${list[rand(0, listSize)]}'를(을) 검색해 보세요!"
        autoTextView.hint = str

        // 검색어 추천 기능 설정
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        autoTextView.setAdapter(adapter)
        autoTextView.threshold = 1

        // 추천 검색어 클릭 시 해당 음료 페이지로 넘어가도록 설정
        autoTextView.onItemClickListener = AdapterView.OnItemClickListener{ parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
            val intent = Intent(this, LabelInfoActivity::class.java)
            intent.putExtra("Name", selectedItem)
            startActivity(intent)
        }
        autoTextView.setOnKeyListener { _, keyCode, event ->
            when {
                // 검색어 입력 후 엔터 키를 눌렀을 때
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {
                    if (list.contains(autoTextView.text.trim().toString().uppercase(Locale.getDefault()))) {
                        val intent = Intent(this, LabelInfoActivity::class.java)
                        intent.putExtra("Name", autoTextView.text.trim().toString().uppercase(Locale.getDefault()))
                        startActivity(intent)
                    }
                    // 일치하는 음료가 없을 때
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
        // 돋보기 모양 버튼을 눌렀을 때의 설정
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

        // 서랍 버튼
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

        //Log.d("emailtest", intent.getStringExtra("email")!!)

        binding.logoutLayout.setOnClickListener {
            signOut()
        }

        setEmailTxt()
    } // OnCreate

    // 유저의 이메일 정보 표시
    private fun setEmailTxt() {
        val headerView = binding.navView.getHeaderView(0)
        val emailTxt = headerView.findViewById<TextView>(R.id.account_email_txt)
        emailTxt.text = user.email
    }

    // 로그아웃 후 로그인 화면으로
    private fun signOut() {
        auth.signOut()
        LoginManager.getInstance().logOut() //facebook logout
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    // 어플 종료 (뒤로가기 버튼 누를 때) 설정
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