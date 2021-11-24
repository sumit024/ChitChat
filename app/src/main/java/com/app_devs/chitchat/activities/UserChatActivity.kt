package com.app_devs.chitchat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.navArgs
import com.app_devs.chitchat.R
import com.app_devs.chitchat.databinding.ActivityUserChatBinding
import com.app_devs.chitchat.model.User
import com.bumptech.glide.Glide

class UserChatActivity : AppCompatActivity() {
    private val data by navArgs<UserChatActivityArgs>()
    private lateinit var binding:ActivityUserChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val receivingPerson:User=data.receiver
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        binding.receiverName.text=receivingPerson.name
        Glide.with(this).load(receivingPerson.profileImage).into(binding.profilePhotoReceiver)
        //Toast.makeText(this,receivingPerson.name,Toast.LENGTH_LONG).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}