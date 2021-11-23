package com.app_devs.chitchat.firebase

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.app_devs.chitchat.R
import com.app_devs.chitchat.fragments.*
import com.app_devs.chitchat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreClass {
    private val db=FirebaseFirestore.getInstance()
    lateinit var loggedInUser: User
     private fun addPhoneNumToDataBase(phoneNumber: String, context: Context) {
        val phone= hashMapOf(
                "contact" to phoneNumber
        )
        db.collection("phone")
                .add(phone)
                .addOnSuccessListener {
                    Toast.makeText(context,"Phone number added", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context,"Phone number not added", Toast.LENGTH_SHORT).show()
                }
    }
    fun checkIfAlreadyExists(phone: String, context: Context,view:View){
        var flag=true
        db.collection("phone")
                .get()
                .addOnSuccessListener{ document ->
                    for (doc in document) {
                        val data = doc.data
                        Log.d("SUNNY", data["contact"].toString())
                        if (phone == data["contact"].toString()) {
                            //move to chat screen since the user is already in db
                            flag=false
                            findNavController(view).navigate(R.id.action_processOTP_to_chatScreenActivity)

                            break
                        }
                    }
                    if(flag)
                    {
                        // it means that user is new so we add number to db and send it to profile segment
                        addPhoneNumToDataBase(phone,context)
                        val action= ProcessOTPFragmentDirections.actionProcessOTPToProfileSetUpFragment(phone)
                        findNavController(view).navigate(action)
                    }

                }

    }
    fun getUserData(fragment: Fragment)
    {
        db.collection("users").document(getCurrentUserId()).get()
                .addOnSuccessListener {
                    val user=it.toObject(User::class.java)
                    Log.d("SHIKHA",user.toString())
                    if (user != null) {
                        if(fragment is UserProfileFragment)
                            fragment.getUser(user)
                    }
                }

    }


    private fun getCurrentUserId():String
    {
        val currentUser=FirebaseAuth.getInstance().currentUser
        var currentUserId=""
        if(currentUser!=null)
        {
            currentUserId=currentUser.uid
        }
        return currentUserId
    }
    fun getUsersList(fragment:UsersListFragment)
    {
        db.collection("users").get()
                .addOnSuccessListener { document->
                    val userList=ArrayList<User>()
                    for (i in document.documents) {
                        val board = i.toObject(User::class.java)!!
                        if(board.uid != getCurrentUserId())
                            userList.add(board)
                    }
                    Log.d("SHIKHA LIST",userList.toString())
                    fragment.populateUsersListToUi(userList)
                }

    }
}