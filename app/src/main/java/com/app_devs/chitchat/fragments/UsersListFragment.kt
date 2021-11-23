package com.app_devs.chitchat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app_devs.chitchat.firebase.FirestoreClass
import com.app_devs.chitchat.adapters.UsersListAdapter
import com.app_devs.chitchat.databinding.FragmentListUsersBinding
import com.app_devs.chitchat.model.User

class UsersListFragment : Fragment() {
    private lateinit var binding:FragmentListUsersBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentListUsersBinding.inflate(layoutInflater)
        FirestoreClass().getUsersList(this)
        return binding.root
    }

    fun populateUsersListToUi(list:ArrayList<User>)
    {
        if(list.size>0)
        {
            binding.usersRv.layoutManager=LinearLayoutManager(requireContext())
            val usersListAdapter= UsersListAdapter(requireContext(),list)
            binding.usersRv.adapter=usersListAdapter
        }
    }



}