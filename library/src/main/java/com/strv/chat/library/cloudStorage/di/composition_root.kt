@file:JvmName("CloudStorageCompositionRoot")

package com.strv.chat.library.cloudStorage.di

import com.google.firebase.storage.FirebaseStorage
import com.strv.chat.library.cloudStorage.client.CloudStorageMediaClient
import com.strv.chat.library.domain.client.MediaClient

fun cloudStorageMediaClient(firebaseStore: FirebaseStorage): MediaClient =
    CloudStorageMediaClient(firebaseStore)