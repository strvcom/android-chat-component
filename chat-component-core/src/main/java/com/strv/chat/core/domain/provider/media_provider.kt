package com.strv.chat.core.domain.provider

import android.content.Context
import android.net.Uri

interface MediaProvider {

    var uri: Uri?

    fun newImageUri(context: Context): Uri
}