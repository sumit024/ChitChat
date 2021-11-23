package com.app_devs.chitchat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.app_devs.chitchat.R

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController=findNavController(R.id.fragment)
        val appBarConfiguration= AppBarConfiguration(setOf(R.id.getOTP,R.id.processOTP,R.id.profileSetUpFragment,R.id.chatScreenActivity))
        setupActionBarWithNavController(navController,appBarConfiguration)
    }

}