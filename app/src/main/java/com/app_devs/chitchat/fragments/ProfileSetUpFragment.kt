package com.app_devs.chitchat.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app_devs.chitchat.utils.Constants
import com.app_devs.chitchat.R
import com.app_devs.chitchat.model.User
import com.app_devs.chitchat.databinding.FragmentProfileSetUpBinding
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileSetUpFragment : Fragment() {
    private val data by navArgs<ProfileSetUpFragmentArgs>()
    private lateinit var binding: FragmentProfileSetUpBinding
    private var phoneNumber:String=""
    private var mSelectedImageUri:Uri?=null
    private var mUserProfileImageUri=""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileSetUpBinding.inflate(layoutInflater,container,false)
        phoneNumber=data.phone
        binding.profilePhoto.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)
                ==PackageManager.PERMISSION_GRANTED)
            {
                Constants.showImageChooser(this)
                Log.d("SHIKHA","image chooser open")
            }
            else
            {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_EXTERNAL_STORAGE_CODE)
                Log.d("SHIKHA","requesting permission")
            }
        }
        binding.btnProceed.setOnClickListener {
            if(verifyInput(binding.etName.text.toString()) )
            {
                if(mSelectedImageUri!=null)
                {
                    uploadImageToStorage()
                }
                else
                {
                    uploadProfileToDatabase()
                }
            }
            else{
                binding.etName.apply {
                    error="Can't be empty!"
                    requestFocus()
                }
            }
        }
        return binding.root
    }
    private fun verifyInput(str:String):Boolean
    {
        return (!TextUtils.isEmpty(str))
    }
    private fun uploadProfileToDatabase()
    {
        val user= User(binding.etName.text.toString(),mUserProfileImageUri)
        val db=FirebaseFirestore.getInstance()
        db.collection("users").add(user).addOnSuccessListener {
            Log.d("SHIKHA",user.toString())
            Toast.makeText(requireContext(),"Profile added",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_profileSetUpFragment_to_chatScreenActivity)
        }.addOnFailureListener {
                    Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_SHORT).show()
                }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== Constants.READ_EXTERNAL_STORAGE_CODE)
        {
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Constants.showImageChooser(this)
                Log.d("SHIKHA","permission granted")
            }
            else{
                Toast.makeText(requireContext(),"You've denied permission. You can enable it in app's settings.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK && requestCode== Constants.IMAGE_PICK_REQ_CODE && data!=null)
        {
            mSelectedImageUri=data.data
            Log.d("SHIKHA",mSelectedImageUri.toString())
            Glide.with(requireContext()).load(mSelectedImageUri).into(binding.profilePhoto)
        }
        else
        {
            Log.d("SHIKHA","chutya kat gya")
        }
    }
    private fun uploadImageToStorage()
    {
        binding.loader.visibility=View.VISIBLE
        if(mSelectedImageUri!=null)
        {
            val mStorageRef=FirebaseStorage.getInstance()
                    .reference
                    .child("ProfileImage"+System.currentTimeMillis()+
                            "."+ Constants.getFileExtension(requireActivity(),mSelectedImageUri))
            mStorageRef.putFile(mSelectedImageUri!!).addOnSuccessListener {
                taskSnapShot->
                Log.d("SHIKHA",taskSnapShot.metadata!!.reference!!.downloadUrl.toString())
                binding.loader.visibility=View.GONE
                taskSnapShot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri->
                    Log.e("SHIKHA", uri.toString())
                    mUserProfileImageUri=uri.toString()
                    uploadProfileToDatabase()
                }
            }.addOnFailureListener{
                binding.loader.visibility=View.GONE
                Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
            }

        }
    }


}