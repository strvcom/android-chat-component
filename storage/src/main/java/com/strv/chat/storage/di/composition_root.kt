@file:JvmName("CloudStorageCompositionRoot")

package com.strv.chat.storage.di

import com.google.firebase.storage.FirebaseStorage
import com.strv.chat.storage.client.CloudStorageMediaClient
import com.strv.chat.core.domain.client.MediaClient

/**
 * Returns a MediaClient instance implemented above Firebase storage.
 *
 * @param firebaseStore FirebaseStorage instance.
 *
 * @return MediaClient.
 */
fun cloudStorageMediaClient(firebaseStore: FirebaseStorage): MediaClient =
    CloudStorageMediaClient(firebaseStore)