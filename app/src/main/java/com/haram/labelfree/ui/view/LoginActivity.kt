package com.haram.labelfree.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.haram.labelfree.R
import com.haram.labelfree.databinding.ActivityLoginBinding
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.activity = this@LoginActivity

        auth = FirebaseAuth.getInstance()

        binding.editEmail.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            val ps: Pattern =
                Pattern.compile("^[a-zA-Z0-9@.]+$")
            if (source == "" || ps.matcher(source).matches()) {
                return@InputFilter source
            }
            Toast.makeText( this, "영문, 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
            ""
        }, InputFilter.LengthFilter(25))

        binding.editPw.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            val ps: Pattern =
                Pattern.compile("""^[0-9a-zA-Z!@#$%^+\-=]*$""")
            if (source == "" || ps.matcher(source).matches()) {
                return@InputFilter source
            }
            Toast.makeText( this, "영문, 숫자, 특수기호 !@#\$%^+\\-=만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
            ""
        }, InputFilter.LengthFilter(20))
    }

    fun signIn(view: View) {
        auth.signInWithEmailAndPassword(binding.editEmail.text.toString(), binding.editPw.text.toString())
            .addOnCompleteListener {  //통신 완료가 된 후 무슨일을 할지
                    task ->
                if(task.isSuccessful){
                    //로그인 처리
                    goMainActivity(task.result.user!!)
                    Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                } else{
                    // 오류가 난 경우
                    Toast.makeText(this, "이메일 또는 비밀번호를 잘못 입력했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goMainActivity(user: FirebaseUser) {
        Log.d("userTest", user.toString())
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}