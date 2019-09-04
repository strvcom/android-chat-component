package com.strv.chat.library.firebase.di

import com.google.firebase.database.DatabaseReference
import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.data.dataSource.ListSource
import com.strv.chat.library.firebase.FirebaseListSource

inline fun <reified T: SourceEntity> DatabaseReference.listSource(): ListSource<T> =
    FirebaseListSource(this, T::class.java)