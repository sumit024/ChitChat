package com.app_devs.chitchat

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.app_devs.chitchat.R
import com.app_devs.chitchat.databinding.FragmentGetOTPBinding

class GetOTPFragment : Fragment() {
    private lateinit var binding:FragmentGetOTPBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentGetOTPBinding.inflate(layoutInflater,container,false)
        val ccp=binding.ccp
        ccp.registerCarrierNumberEditText(binding.phoneNum)
        binding.btnGetOTP.setOnClickListener {
            val phoneNumber=binding.phoneNum.text.toString()
            if(TextUtils.isEmpty(phoneNumber))
            {
                binding.phoneNum.error = "Can't be empty"
                binding.phoneNum.requestFocus()
            }
            else
            {
                val action=GetOTPFragmentDirections.actionGetOTPToProcessOTP(ccp.fullNumberWithPlus.trim())
                Navigation.findNavController(it).navigate(action)
            }
        }
        return binding.root
    }

}