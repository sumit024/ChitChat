package com.app_devs.chitchat.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.app_devs.chitchat.fragments.ProfileSetUpFragment

object Constants {
    const val IMAGE_PICK_REQ_CODE=1
    const val READ_EXTERNAL_STORAGE_CODE=2

    fun showImageChooser(activity: ProfileSetUpFragment)
    {
        val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, IMAGE_PICK_REQ_CODE)
    }

    fun getFileExtension(activity: Activity,uri: Uri?):String?
    {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }



}