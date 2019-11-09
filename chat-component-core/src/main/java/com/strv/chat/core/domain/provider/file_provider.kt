package com.strv.chat.core.domain.provider

import android.content.Context
import android.net.Uri

/**
 * Provides interactions with the file system.
 */
interface FileProvider {

    /**
     * Returns a url of a new created file.
     *
     * @param context [Context].
     */
    fun newFile(context: Context): Uri
}