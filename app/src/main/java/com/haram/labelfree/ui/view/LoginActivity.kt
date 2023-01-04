package com.haram.labelfree.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.haram.labelfree.R
import com.haram.labelfree.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val TAG = "LoginActivityTest"
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var tokenId: String? = null

    private val GOOGLE_SIGN_IN = 1

    private var callbackManager: CallbackManager? = null

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

        // 클라이언트에 넣을 구글 로그인 옵션 설정
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // 구글 클라이언트 생성
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleSignInBtn.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, GOOGLE_SIGN_IN)
        }

        // 페이스북
        callbackManager = CallbackManager.Factory.create()
        binding.facebookSignInBtn.setOnClickListener {
            facebookLogin()
        }
    } // onCreate

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // 로그인 성공 확인
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "Google Sign in : " + account.email)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch(e: ApiException) {
                // 연결 실패
                Log.w(TAG, "Google sign in failed : ", e)
                Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun facebookLogin() {

        LoginManager.getInstance()
            .logInWithReadPermissions(this, listOf("email", "public_profile"))

        LoginManager.getInstance()
            .registerCallback(callbackManager, object: FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    // facebook 계정 정보를 firebase 서버에게 전달(로그인)
                    Log.d(TAG, "facebook:onSuccess:$result")
                    val accessToken = result.accessToken
                    firebaseAuthWithFacebook(result?.accessToken)
                }

                override fun onCancel() {
                    //취소가 된 경우 할일
                    Log.d(TAG, "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    //에러가 난 경우 할일
                    Log.d(TAG, "facebook:onError", error)
                }
            })
    }

    private fun firebaseAuthWithFacebook(accessToken: AccessToken?) {
        // AccessToken 으로 Facebook 인증
        val credential = FacebookAuthProvider.getCredential(accessToken?.token!!)

        // 성공 시 Firebase 에 유저 정보 보내기 (로그인)
        auth.signInWithCredential(credential)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){ // 정상적으로 email, password 가 전달된 경우
                    // 로그인 처리
                    //goMainActivity(task.result?.user!!)
                    startMainActivity()
                    finish()
                } else {
                    // 예외 발생 시 메시지 출력
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 스플레시 액티비티로 구성하여 성공한다면 finish() 동작 수행
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                    startMainActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "로그인 연결 실패", Toast.LENGTH_SHORT).show()
                }
            }
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

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goMainActivity(user: FirebaseUser) {
        Log.d("userTest", user.toString())
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}