package com.example.sogaeting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.example.sogaeting.auth.IntroActivity
import com.example.sogaeting.auth.UserDataModel
import com.example.sogaeting.databinding.ActivityIntroBinding
import com.example.sogaeting.databinding.ActivityMainBinding
import com.example.sogaeting.setting.SettingActivity
import com.example.sogaeting.silder.CardStackAdapter
import com.example.sogaeting.utils.FirebaseAuthUtils
import com.example.sogaeting.utils.FirebaseRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    lateinit var bind : ActivityMainBinding

    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager : CardStackLayoutManager

    private val TAG = "MainActivity"

    private val usersDataList = mutableListOf<UserDataModel>()

    private lateinit var currentUserGender: String
    private var userCount = 0
    private val uid = FirebaseAuthUtils.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)



        bind.setting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }


        manager = CardStackLayoutManager(baseContext, object : CardStackListener{
            override fun onCardDragging(p0: Direction?, p1: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {
                if(direction == Direction.Right){
                    userLikeOtherUser(uid, usersDataList[userCount].uid.toString())
                }
                if(direction == Direction.Left){
                }
                userCount++
                if(userCount == usersDataList.count()){ // 다 넘겨서 더이상 넘길 유저가 없으면
                    getUserDataList(currentUserGender) // 다시 유저 가져오기
                    toast("유저 새롭게 받아오기")
                }
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

        cardStackAdapter = CardStackAdapter(baseContext, usersDataList)
        bind.cardStackView.layoutManager = manager
        bind.cardStackView.adapter = cardStackAdapter

        getMyData()
    }

    private fun getMyData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, dataSnapshot.toString())
                val data = dataSnapshot.getValue(UserDataModel::class.java)

                currentUserGender = data?.gender.toString()
                getUserDataList(currentUserGender)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        // 어디에서 가져올거냐? (경로)
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)
    }

    private fun getUserDataList(currentUserGender : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children){
                    val user = dataModel.getValue(UserDataModel::class.java)
                    if(user?.gender.toString() != currentUserGender){
                        usersDataList.add(user!!)
                    }
                }

                cardStackAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        // 어디에서 가져올거냐?
        FirebaseRef.userInfoRef.addValueEventListener(postListener)
    }

    private fun userLikeOtherUser(myUid : String, otherUid : String){
        FirebaseRef.userIikeRef.child(myUid).child(otherUid).setValue("true")// 저장될 값

        getOtherUserListList(otherUid)
    }

    // 내가 좋아요한 사람이 나를 좋아하는지
    private fun getOtherUserListList(otherUid: String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    if (dataModel.key.toString() == uid) {
                        toast("매칭완료")
                        createNotificationCannel()
                        sendNotification()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        // 어디에서 가져올거냐?
        FirebaseRef.userIikeRef.child(otherUid).addValueEventListener(postListener)
    }

    private fun createNotificationCannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("test_Channel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(){
        var builder = NotificationCompat.Builder(this, "test_Channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("매칭완료")
            .setContentText("통했네요!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(123, builder.build())
        }
    }
}