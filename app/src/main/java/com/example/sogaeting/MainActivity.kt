package com.example.sogaeting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.sogaeting.auth.IntroActivity
import com.example.sogaeting.databinding.ActivityIntroBinding
import com.example.sogaeting.databinding.ActivityMainBinding
import com.example.sogaeting.silder.CardStackAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    lateinit var bind : ActivityMainBinding

    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager : CardStackLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //로그아웃
        bind.logoutBtn.setOnClickListener {
            val auth = Firebase.auth
            auth.signOut()

            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }


        manager = CardStackLayoutManager(baseContext, object : CardStackListener{
            override fun onCardDragging(p0: Direction?, p1: Float) {

            }

            override fun onCardSwiped(p0: Direction?) {

            }

            override fun onCardRewound() {

            }

            override fun onCardCanceled() {

            }

            override fun onCardAppeared(p0: View?, p1: Int) {

            }

            override fun onCardDisappeared(p0: View?, p1: Int) {

            }
        })

        val testList = mutableListOf<String>()
        testList.add("a")
        testList.add("b")
        testList.add("c")

        cardStackAdapter = CardStackAdapter(baseContext, testList)
        bind.cardStackView.layoutManager = manager
        bind.cardStackView.adapter = cardStackAdapter
    }
}