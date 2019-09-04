package com.strv.chat.library.firestore

import com.google.firebase.firestore.Query
import com.strv.chat.library.data.dataSource.ListSource
import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.firestore.dataSource.FirestoreListSource

internal inline fun <reified T: SourceEntity> Query.listSource(): ListSource<T> =
    FirestoreListSource(this, T::class.java)