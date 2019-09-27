package com.strv.chat.library.domain.provider

import android.content.Context
import android.net.Uri

interface MediaProvider {

    var uri: Uri?

    fun newImageUri(context: Context): Uri
}