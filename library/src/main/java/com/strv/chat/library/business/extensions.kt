package com.strv.chat.library.business

import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.Query
import com.strv.chat.library.business.data.SourceEntity

inline fun <reified T: SourceEntity> DatabaseReference.listSource(): ListSource<T> =
    FirebaseListSource(this, T::class.java)

inline fun <reified T: SourceEntity> Query.listSource(): ListSource<T> =
    FirestoreListSource(this, T::class.java)