package com.app_devs.chitchat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app_devs.chitchat.R
import com.app_devs.chitchat.databinding.UserRowLayoutBinding
import com.app_devs.chitchat.model.User
import com.bumptech.glide.Glide

class UsersListAdapter(private val context: Context,private val list:ArrayList<User>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener:OnClickListener?=null
    class MyViewHolder(val binding:UserRowLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(UserRowLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MyViewHolder)
        {
            val model=list[position]
            holder.binding.tvUsername.text=model.name
            Glide.with(context).load(model.profileImage).placeholder(R.drawable.annonymous).into(holder.binding.ivProfile)
            holder.itemView.setOnClickListener {
                onClickListener?.onClick(position,model)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickListener{
        fun onClick(position: Int,user: User)
    }
    fun onClickListener(onClickListener: OnClickListener)
    {
        this.onClickListener=onClickListener
    }
}