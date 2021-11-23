package com.app_devs.chitchat.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app_devs.chitchat.firebase.FirestoreClass
import com.app_devs.chitchat.R
import com.app_devs.chitchat.databinding.FragmentUserProfileBinding
import com.app_devs.chitchat.utils.Constants
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
    private lateinit var db:FirebaseFirestore
    private var mSelectedImageUri:Uri?=null
    private lateinit var user: com.app_devs.chitchat.model.User
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentUserProfileBinding.inflate(layoutInflater,container,false)
        FirestoreClass().getUserData(this)
        binding.editUserName.setOnClickListener {
            showDialogToEditName()
        }
        binding.editProfilePhoto.setOnClickListener {
            updateProfilePhoto()
        }
        return binding.root
    }
    private fun showDialogToEditName()
    {
        val dialog=Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_layout)
        val editTextName=dialog.findViewById<EditText>(R.id.et_username_edit)
        val updateBtn=dialog.findViewById<Button>(R.id.btn_update)
        editTextName.setText(binding.tvUsername.text.toString())
        updateBtn.setOnClickListener {

        }
        dialog.show()
    }
    private fun updateProfilePhoto()
    {
        if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
        {
            Constants.showImageChooser(this)
        }
        else
        {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_EXTERNAL_STORAGE_CODE)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== Constants.READ_EXTERNAL_STORAGE_CODE)
        {
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Constants.showImageChooser(this)

            }
            else{
                Toast.makeText(requireContext(),"You've denied permission. You can enable it in app's settings.", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode== Constants.IMAGE_PICK_REQ_CODE && data!=null)
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
    private fun setData()
    {
        binding.loader.visibility=View.VISIBLE
        binding.tvUsername.text = user.name
        Glide.with(requireContext()).load(user.profileImage).placeholder(R.drawable.annonymous).into(binding.profilePhoto)
        binding.loader.visibility = View.GONE

    }
    fun getUser(loggedInUser: com.app_devs.chitchat.model.User)
    {
        user=loggedInUser
        setData()
    }
}