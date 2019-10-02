package com.strv.chat.firestore

import com.google.firebase.firestore.Query
import com.strv.chat.core.data.source.ListSource
import com.strv.chat.core.data.entity.SourceEntity
import com.strv.chat.firestore.source.FirestoreListSource

internal inline fun <reified T: SourceEntity> Query.listSource(): ListSource<T> =
    FirestoreListSource(this, T::class.java)