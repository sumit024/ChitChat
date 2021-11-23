package com.app_devs.chitchat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.app_devs.chitchat.R
import com.app_devs.chitchat.databinding.ActivityChatScreenBinding
import com.app_devs.chitchat.model.User

class ChatScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatScreenBinding
    var user= User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // FirestoreClass().getUserData(this)
        val navController=findNavController(R.id.hostFragment)
        binding.bottomNavigation.setupWithNavController(navController)
        val appBarConfiguration= AppBarConfiguration(setOf(R.id.chatFragment,R.id.profileFragment))
        setupActionBarWithNavController(navController,appBarConfiguration)
    }

    fun getUser(loggedInUser: User)
    {
        user=loggedInUser
    }

    override fun onBackPressed() {

    }
}