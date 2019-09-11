package com.strv.chat.library.common

import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.Query
import com.strv.chat.library.data.firebase.FirebaseListSource
import com.strv.chat.library.data.firestore.FirestoreListSource
import com.strv.chat.library.data.model.SourceEntity
import com.strv.chat.library.domain.ListSource

inline fun <reified T: SourceEntity> DatabaseReference.listSource(): ListSource<T> =
    FirebaseListSource(this, T::class.java)

inline fun <reified T: SourceEntity> Query.listSource(): ListSource<T> =
    FirestoreListSource(this, T::class.java)