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