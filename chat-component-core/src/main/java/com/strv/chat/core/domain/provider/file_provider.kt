package com.strv.chat.core.domain.provider

import android.content.Context
import android.net.Uri

interface FileProvider {

    var uri: Uri?

    fun newFile(context: Context): Uri
}