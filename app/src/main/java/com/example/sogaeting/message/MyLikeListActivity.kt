package com.example.sogaeting.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.example.sogaeting.R
import com.example.sogaeting.auth.UserDataModel
import com.example.sogaeting.databinding.ActivityMyLikeListBinding
import com.example.sogaeting.databinding.ListViewItemBinding
import com.example.sogaeting.utils.FirebaseAuthUtils
import com.example.sogaeting.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.listView
import org.jetbrains.anko.toast

class MyLikeListActivity : AppCompatActivity() {
    lateinit var bind : ActivityMyLikeListBinding

    private val TAG = "MyLikeListActivity"
    private val uid = FirebaseAuthUtils.getUid()

    private val likeUserList = mutableListOf<UserDataModel>()
    private val likeUserListUid = mutableListOf<String>()

    lateinit var listViewAdapter : ListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMyLikeListBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val userListView = findViewById<ListView>(R.id.userListView)
        listViewAdapter = ListViewAdapter(this, likeUserList)

        userListView.adapter = listViewAdapter

        userListView.setOnItemClickListener { parent, view, position, id ->
            checkMatching(likeUserList[position].uid.toString())
        }

        getMyLikeList()
    }

    private fun checkMatching(otherUid : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    val likeUserKey = dataModel.key.toString()
                    if (likeUserKey == uid){
                        toast("매칭완료!")
                    }else{
                        toast("매칭되지 않았습니다 ㅠ")
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

    // 내가 좋아요한 사람
    private fun getMyLikeList(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    // 내가 좋아요 한 사람들의 uid가 해당 리스트에 들어있음
                    likeUserListUid.add(dataModel.key.toString())
                }
                getUserDataList()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        // 어디에서 가져올거냐?
        FirebaseRef.userIikeRef.child(uid).addValueEventListener(postListener)
    }

    // 전체 유저 데이터 받아오기
    private fun getUserDataList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children){
                    val user = dataModel.getValue(UserDataModel::class.java)
                    if(likeUserListUid.contains(user?.uid)){
                        // 전체 유저중 내가 좋아요한 사람만
                        likeUserList.add(user!!)
                    }
                }
                listViewAdapter.notifyDataSetChanged()
                Log.e(TAG, "point : ${likeUserList.toString()}")

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        // 어디에서 가져올거냐?
        FirebaseRef.userInfoRef.addValueEventListener(postListener)
    }

    // 나를 좋아요한 사람

}