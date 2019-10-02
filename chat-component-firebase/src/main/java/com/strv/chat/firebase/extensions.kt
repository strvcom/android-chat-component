package com.strv.chat.core.firebase

import com.google.firebase.database.DatabaseReference
import com.strv.chat.core.data.entity.SourceEntity
import com.strv.chat.core.data.source.ListSource

inline fun <reified T: SourceEntity> DatabaseReference.listSource(): ListSource<T> =
    FirebaseListSource(this, T::class.java)