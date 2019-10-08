package com.strv.chat.firebase

import com.google.firebase.database.DatabaseReference
import com.strv.chat.core.data.entity.SourceEntity
import com.strv.chat.core.data.source.ListQuerySource

inline fun <reified T: SourceEntity> DatabaseReference.listSource(): ListQuerySource<T> =
    FirebaseListQuerySource(this, T::class.java)