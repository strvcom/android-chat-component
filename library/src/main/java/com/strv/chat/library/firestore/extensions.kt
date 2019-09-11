package com.strv.chat.library.firestore

import com.google.firebase.firestore.Query
import com.strv.chat.library.data.source.ListSource
import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.firestore.source.FirestoreListSource

internal inline fun <reified T: SourceEntity> Query.listSource(): ListSource<T> =
    FirestoreListSource(this, T::class.java)