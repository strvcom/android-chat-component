package com.strv.chat.firestore

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.strv.chat.core.data.source.ListQuerySource
import com.strv.chat.core.data.entity.SourceEntity
import com.strv.chat.core.data.source.DocumentSource
import com.strv.chat.firestore.source.FirestoreDocumentSource
import com.strv.chat.firestore.source.FirestoreListQuerySource

internal inline fun <reified T: SourceEntity> Query.listSource(): ListQuerySource<T> =
    FirestoreListQuerySource(this, T::class.java)

internal inline fun <reified T: SourceEntity> DocumentReference.source(): DocumentSource<T> =
    FirestoreDocumentSource(this, T::class.java)