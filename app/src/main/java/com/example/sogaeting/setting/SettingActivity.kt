package com.example.sogaeting.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sogaeting.R
import com.example.sogaeting.auth.IntroActivity
import com.example.sogaeting.databinding.ActivitySettingBinding
import com.example.sogaeting.message.MyLikeListActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {
    lateinit var bind : ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(bind.root)

        // 1. 앱에서 notification 띄우기 - 매칭완료 알림
        // 2. Firebase 콘솔에서 모든 앱에 Push 보내기
        // 3. Firebase 콘솔에서 특정 사용자에게 메세제 보내기

        // 4. Firebase 콘솔 말고 앱에서 직접 다른 사람에게 Push 메세지 보내기-



        bind.myPageBtn.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        bind.myLikeListBtn.setOnClickListener {
            val intent = Intent(this, MyLikeListActivity::class.java)
            startActivity(intent)
        }

        bind.logoutBtn.setOnClickListener {
            val auth = Firebase.auth
            auth.signOut()

            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }
    }
}