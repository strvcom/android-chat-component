package com.strv.chat.storage

import com.google.firebase.storage.StorageMetadata

fun storageMetadata(builder: StorageMetadata.Builder.() -> Unit) =
    StorageMetadata.Builder().apply(builder).build()

inline var StorageMetadata.Builder.mediaContentType: String
    get() = throw UnsupportedOperationException("")
    set(value) {
        setContentType(value)
    }
