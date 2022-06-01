package com.example.sogaeting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sogaeting.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    lateinit var bind : ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(bind.root)


        bind.joinBtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

    }
}