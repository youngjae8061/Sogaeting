package com.example.sogaeting.silder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sogaeting.R
import com.example.sogaeting.auth.UserDataModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CardStackAdapter (val context:Context, val items:List<UserDataModel>) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardStackAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view:View = inflater.inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardStackAdapter.ViewHolder, position: Int) {
        holder.binding(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image = itemView.findViewById<ImageView>(R.id.profileImgArea)
        val itemNickname = itemView.findViewById<TextView>(R.id.itemNickname)
        val itemAge = itemView.findViewById<TextView>(R.id.itemAge)
        val itemCity = itemView.findViewById<TextView>(R.id.itemCity)
        fun binding(data:UserDataModel){

            val storageRef = Firebase.storage.reference.child("${data.uid}.png")
            storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                if(task.isSuccessful){
                    Glide.with(context)
                        .load(task.result)
                        .into(image)
                }
            })

            itemNickname.text = "이름 : ${data.nickname}"
            itemAge.text = "나이 : ${data.age}"
            itemCity.text = "지역 : ${data.city}"
        }
    }
}