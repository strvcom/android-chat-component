package com.strv.chat.storage

import com.google.firebase.storage.FirebaseStorage

internal const val IMAGE_REFERENCE = "images"

internal fun path(firebaseStorage: FirebaseStorage, path: String) =
    firebaseStorage.reference.child(path)