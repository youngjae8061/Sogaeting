package com.example.sogaeting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.sogaeting.auth.IntroActivity
import com.example.sogaeting.utils.FirebaseAuthUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val uid = FirebaseAuthUtils.getUid()

        if(uid == "null"){
            CoroutineScope(Dispatchers.IO).launch {
                delay(1000)
                val intent = Intent(applicationContext, IntroActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                delay(1000)
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}