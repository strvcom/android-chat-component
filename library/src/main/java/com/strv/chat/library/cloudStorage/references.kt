package com.strv.chat.library.cloudStorage

import com.google.firebase.storage.FirebaseStorage

const val IMAGE_REFERENCE = "images"

fun path(firebaseStorage: FirebaseStorage, path: String) =
    firebaseStorage.reference.child(path)