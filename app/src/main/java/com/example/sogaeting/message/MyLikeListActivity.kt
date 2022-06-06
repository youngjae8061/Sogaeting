package com.example.sogaeting.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.sogaeting.R
import com.example.sogaeting.databinding.ActivityMyLikeListBinding
import com.example.sogaeting.utils.FirebaseAuthUtils
import com.example.sogaeting.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.toast

class MyLikeListActivity : AppCompatActivity() {
    lateinit var bind : ActivityMyLikeListBinding

    private val TAG = "MyLikeListActivity"
    private val uid = FirebaseAuthUtils.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMyLikeListBinding.inflate(layoutInflater)
        setContentView(bind.root)

        getMyLikeList()
    }

    // 내가 좋아요한 사람
    private fun getMyLikeList(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        // 어디에서 가져올거냐?
        FirebaseRef.userIikeRef.child(uid).addValueEventListener(postListener)
    }
}