package com.example.sogaeting.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef {
    companion object{
        // 데이터베이스에 쓰기
        val database = Firebase.database
        val userInfoRef = database.getReference("userInfo") // 데이터 저장 경로
    }
}