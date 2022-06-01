package com.example.sogaeting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sogaeting.databinding.ActivityJoinBinding
import org.jetbrains.anko.toast

class JoinActivity : AppCompatActivity() {
    lateinit var bind : ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.join.setOnClickListener {

            if(!bind.email.text.toString().isNullOrEmpty()) toast(" ${bind.email.text.toString()}")
            if(!bind.password.text.toString().isNullOrEmpty()) toast(" ${bind.password.text.toString()}")
        }

    }
}