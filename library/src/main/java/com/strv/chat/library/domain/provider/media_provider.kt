package com.strv.chat.library.domain.provider

import android.net.Uri

interface MediaProvider {

    fun imageUri(): Uri

    fun uploadPhoto(uri: Uri)

}