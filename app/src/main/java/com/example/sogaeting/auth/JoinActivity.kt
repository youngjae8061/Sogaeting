package com.example.sogaeting.auth

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sogaeting.MainActivity
import com.example.sogaeting.databinding.ActivityJoinBinding
import com.example.sogaeting.utils.FirebaseRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {
    lateinit var bind : ActivityJoinBinding

    private lateinit var auth : FirebaseAuth

    private var nickname = ""
    private var gender = ""
    private var city = ""
    private var age = ""
    private var uid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(bind.root)

        auth = Firebase.auth

        val getAction = registerForActivityResult(ActivityResultContracts.GetContent(), ActivityResultCallback { uri ->
            bind.joinProfile.setImageURI(uri)
        })

        bind.joinProfile.setOnClickListener {
            // 앨범 열기
            getAction.launch("image/*")
        }


        bind.join.setOnClickListener {
            nickname = bind.nickname.text.toString()
            gender = bind.gender.text.toString()
            city = bind.city.text.toString()
            age = bind.age.text.toString()

            auth.createUserWithEmailAndPassword(bind.email.text.toString(), bind.password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")

                        val user = auth.currentUser
                        uid = user?.uid.toString()


                        val userModel = UserDataModel(uid, nickname, age, gender, city)

                        // child(uid) uid로 하위 경로 생성
                        FirebaseRef.userInfoRef.child(uid).setValue(userModel)// 저장될 값




                        // 메인화면으로 이동
//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    }
                }
        }
    }
}