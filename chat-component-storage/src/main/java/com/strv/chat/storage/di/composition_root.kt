@file:JvmName("CloudStorageCompositionRoot")

package com.strv.chat.storage.di

import com.google.firebase.storage.FirebaseStorage
import com.strv.chat.storage.client.CloudStorageMediaClient
import com.strv.chat.core.domain.client.MediaClient

fun cloudStorageMediaClient(firebaseStore: FirebaseStorage): MediaClient =
    CloudStorageMediaClient(firebaseStore)