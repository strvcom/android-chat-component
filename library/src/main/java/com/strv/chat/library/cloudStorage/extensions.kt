package com.strv.chat.library.cloudStorage

import com.google.firebase.storage.StorageMetadata

fun storageMetadata(builder: StorageMetadata.Builder.() -> Unit) =
    StorageMetadata.Builder().apply(builder).build()

inline var StorageMetadata.Builder.contentType: String
    get() = throw UnsupportedOperationException("")
    set(value) {
        setContentType(value)
    }
