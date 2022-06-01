package com.example.sogaeting.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.sogaeting.R
import com.example.sogaeting.auth.UserDataModel
import com.example.sogaeting.databinding.ActivityMyPageBinding
import com.example.sogaeting.utils.FirebaseAuthUtils
import com.example.sogaeting.utils.FirebaseRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MyPageActivity : AppCompatActivity() {

    lateinit var bind : ActivityMyPageBinding

    private val TAG = "MyPageActivity"
    private val uid = FirebaseAuthUtils.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(bind.root)

        getMyData()
    }

    private fun getMyData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, dataSnapshot.toString())
                val data = dataSnapshot.getValue(UserDataModel::class.java)

                bind.myUid.text = data!!.uid
                bind.myNickname.text = data!!.nickname
                bind.myAge.text = data!!.age
                bind.myCity.text = data!!.city
                bind.myGender.text = data!!.gender

                // 이미지 가져오기
                val storageRef = Firebase.storage.reference.child("${data.uid}.png")
                storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                    if(task.isSuccessful){
                        Glide.with(baseContext)
                            .load(task.result)
                            .into(bind.myImage)
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        // 어디에서 가져올거냐?
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)
    }
}