package com.example.sogaeting.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

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

                        // 이미지 업로드
                        uploadImage(uid)


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

    private fun uploadImage(uid : String){

        val storage = Firebase.storage
        val storageRef = storage.reference.child("${uid}.png")

        // 이미지뷰에 있는 그림을 firebase에 저장하기
        bind.joinProfile.isDrawingCacheEnabled = true
        bind.joinProfile.buildDrawingCache()
        val bitmap = (bind.joinProfile.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }
}