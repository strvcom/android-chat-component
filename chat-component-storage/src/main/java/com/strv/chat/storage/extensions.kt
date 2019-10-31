package com.strv.chat.storage

import com.google.firebase.storage.StorageMetadata

/**
 * [StorageMetadata] builder function.
 */
internal fun storageMetadata(builder: StorageMetadata.Builder.() -> Unit) =
    StorageMetadata.Builder().apply(builder).build()

internal inline var StorageMetadata.Builder.mediaContentType: String
    get() = throw UnsupportedOperationException("")
    set(value) {
        contentType = value
    }
