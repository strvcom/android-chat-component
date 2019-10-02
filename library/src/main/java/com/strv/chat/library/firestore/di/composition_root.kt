@file:JvmName("FirestoreCompositionRoot")

package com.strv.chat.library.firestore.di

import com.google.firebase.firestore.FirebaseFirestore
import com.strv.chat.library.domain.client.ChatClient
import com.strv.chat.library.domain.client.ConversationClient
import com.strv.chat.library.firestore.client.FirestoreChatClient
import com.strv.chat.library.firestore.client.FirestoreConversationClient

fun firestoreChatClient(firebaseDb: FirebaseFirestore): ChatClient =
    FirestoreChatClient(firebaseDb)

fun firestoreConversationClient(firebaseDb: FirebaseFirestore): ConversationClient =
    FirestoreConversationClient(firebaseDb)