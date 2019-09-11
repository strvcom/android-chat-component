package com.strv.chat.library.firebase

import com.google.firebase.database.DatabaseReference
import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.data.source.ListSource

inline fun <reified T: SourceEntity> DatabaseReference.listSource(): ListSource<T> =
    FirebaseListSource(this, T::class.java)