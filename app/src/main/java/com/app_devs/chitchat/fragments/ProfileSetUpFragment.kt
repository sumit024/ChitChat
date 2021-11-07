package com.app_devs.chitchat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.app_devs.chitchat.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ProfileSetUpFragment : Fragment() {
    private val data by navArgs<ProfileSetUpFragmentArgs>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_profile_set_up, container, false)
    }


}