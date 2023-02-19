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
import com.haram.labelfree.R
import com.haram.labelfree.databinding.ActivityRegisterBinding
import java.util.regex.Pattern

// IntroActivity 이후 회원가입을 할 때의 액티비티
class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.activity = this@RegisterActivity

        auth = FirebaseAuth.getInstance()

        // 이메일 입력 제한 설정
        binding.editEmail.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            val ps: Pattern =
                Pattern.compile("^[a-zA-Z0-9@.]+$")
            if (source == "" || ps.matcher(source).matches()) {
                return@InputFilter source
            }
            Toast.makeText( this, "영문, 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
            ""
        }, InputFilter.LengthFilter(25))

        // 비밀번호 입력 제한 설정
        binding.editPw.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            val ps: Pattern =
                Pattern.compile("""^[0-9a-zA-Z!@#$%^+\-=]*$""")
            if (source == "" || ps.matcher(source).matches()) {
                return@InputFilter source
            }
            Toast.makeText( this, "영문, 숫자, 특수기호 !@#\$%^+\\-=만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
            ""
        }, InputFilter.LengthFilter(20))

        binding.editPwCheck.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            val ps: Pattern =
                Pattern.compile("""^[0-9a-zA-Z!@#$%^+\-=]*$""")
            if (source == "" || ps.matcher(source).matches()) {
                return@InputFilter source
            }
            Toast.makeText( this, "영문, 숫자, 특수기호만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
            ""
        }, InputFilter.LengthFilter(20))
    }

    // 회원가입
    fun signUp(view: View) {
        if(binding.editPw.text.toString() != binding.editPwCheck.text.toString()) {
            Toast.makeText(this, "입력한 비밀번호가 서로 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
        }

        else {
            auth?.createUserWithEmailAndPassword(binding.editEmail.text.toString(), binding.editPw.text.toString())
                ?.addOnCompleteListener {  //통신 완료가 된 후 무슨일을 할지
                        task ->
                    if(task.isSuccessful){
                        // 기존에 있는 계정이 아니어서 회원가입 성공 시
                        goMainActivity(task.result.user!!)
                        finish()
                    }
                    else if (task.exception?.message.isNullOrEmpty()==false){
                        // 에러가 났다거나 서버가 연결이 실패했다거나
                        Toast.makeText(this,task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                    else{
                        // 이미 db에 해당 이메일과 패스워드가 있는 경우
                        Toast.makeText(this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    // 회원가입 성공 후 메인 액티비티로 이동하는 메소드
    private fun goMainActivity(user: FirebaseUser) {
        Log.d("userTest", user.toString())
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}