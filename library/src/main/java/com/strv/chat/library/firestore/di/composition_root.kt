package com.strv.chat.library.firestore.di

import com.strv.chat.library.firestore.FirestoreChatClient

fun firestoreChatClient(init: FirestoreChatClient.Builder.() -> Unit) =
    FirestoreChatClient.Builder().apply(init).build()