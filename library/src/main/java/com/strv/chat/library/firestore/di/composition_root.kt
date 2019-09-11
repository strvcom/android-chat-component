package com.strv.chat.library.firestore.di

import com.strv.chat.library.domain.client.ConversationClient
import com.strv.chat.library.firestore.client.FirestoreChatClient
import com.strv.chat.library.firestore.client.FirestoreConversationClient

fun firestoreChatClient(init: FirestoreChatClient.Builder.() -> Unit) =
    FirestoreChatClient.Builder().apply(init).build()

fun firestoreConversationClient(init: FirestoreConversationClient.Builder.() -> Unit): ConversationClient =
    FirestoreConversationClient.Builder().apply(init).build()