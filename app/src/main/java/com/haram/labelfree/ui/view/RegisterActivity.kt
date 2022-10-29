package com.haram.labelfree.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.haram.labelfree.R
import com.haram.labelfree.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.activity = this@RegisterActivity

        auth = FirebaseAuth.getInstance()
    }

    fun signUp(view: View) {
        auth?.createUserWithEmailAndPassword(binding.editEmail.text.toString(), binding.editPw.text.toString())
            ?.addOnCompleteListener {  //통신 완료가 된 후 무슨일을 할지
                    task ->
                if(task.isSuccessful){
                    //정상적으로 이메일과 비번이 전달되어
                    //새 유저 계정을 생성과 서버db 저장 완료 및 로그인
                    //즉, 기존에 있는 계정이 아니다!
                    goMainActivity(task.result.user!!)
                }
                else if (task.exception?.message.isNullOrEmpty()==false){
                    //예외메세지가 있다면 출력
                    //에러가 났다거나 서버가 연결이 실패했다거나
                    Toast.makeText(this,task.exception?.message, Toast.LENGTH_SHORT).show()
                }
                else{
                    //여기가 실행되는 경우는 이미 db에 해당 이메일과 패스워드가 있는 경우
                    //그래서 계정 생성이 아닌 로그인 함수로 이동
                    Toast.makeText(this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goMainActivity(user: FirebaseUser) {
        Log.d("userTest", user.toString())
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}