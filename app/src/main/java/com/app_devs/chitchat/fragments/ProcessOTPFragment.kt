package com.app_devs.chitchat.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app_devs.chitchat.ProcessOTPFragmentArgs
import com.app_devs.chitchat.R
import com.app_devs.chitchat.databinding.FragmentProcessOTPBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit


class ProcessOTPFragment : Fragment() {
    private val data by navArgs<ProcessOTPFragmentArgs>()
    private lateinit var binding:FragmentProcessOTPBinding
    private var phoneNumber=""
    private lateinit var auth: FirebaseAuth
    private var otpId:String=""

    //what the otp is
    private var otpText=""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentProcessOTPBinding.inflate(layoutInflater, container, false)
        auth= FirebaseAuth.getInstance()
        phoneNumber=data.phoneNum
        checkAlreadyAuthenticated(phoneNumber)
        binding.getPhone.text=phoneNumber
        sendOTP()
        binding.verify.setOnClickListener {
            otpText=binding.pinview.text.toString()
            if (otpText.isEmpty()) {
                Toast.makeText(requireContext(), "Invalid OTP: Can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                binding.loader.visibility = View.VISIBLE
                Log.d("SUMIT",otpId)
                Log.d("SUMIT",otpText)
                val phoneAuthCredential = PhoneAuthProvider.getCredential(otpId,otpText)
                signInWithPhoneAuthCredential(phoneAuthCredential)
            }
        }
        binding.resend.setOnClickListener {
            sendOTP()
        }

        return binding.root
    }

    private fun checkAlreadyAuthenticated(phoneNumber: String) {

    }

    private fun sendOTP() {
        binding.loader.visibility = View.VISIBLE
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                //Log.d(TAG, "onVerificationCompleted:$credential")
                val code=credential.smsCode
                binding.pinview.setText(code)
                signInWithPhoneAuthCredential(credential)
                binding.loader.visibility = View.GONE
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

                // Show a message and update the UI
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show();
                binding.loader.visibility = View.GONE
            }

            override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                // Save verification ID and resending token so we can use them later
                super.onCodeSent(verificationId, token)
                otpId=verificationId
                binding.loader.visibility = View.GONE
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(activity as AppCompatActivity)              // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)


    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        binding.loader.visibility = View.VISIBLE
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful)
            {
                findNavController().navigate(R.id.action_processOTP_to_profileSetUpFragment)
                Toast.makeText(requireContext(), "Logged In", Toast.LENGTH_LONG).show();
                binding.loader.visibility = View.GONE
            }
            else
            {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show();
                binding.loader.visibility = View.GONE
            }
        }
    }
}