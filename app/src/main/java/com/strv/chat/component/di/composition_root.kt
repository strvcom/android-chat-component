package com.strv.chat.component.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.strv.chat.component.business.MediaProviderImpl
import com.strv.chat.component.business.MemberProviderImpl
import com.strv.chat.core.domain.client.ChatClient
import com.strv.chat.core.domain.client.ConversationClient
import com.strv.chat.core.domain.client.MediaClient
import com.strv.chat.core.domain.provider.MediaProvider
import com.strv.chat.core.domain.provider.MemberProvider
import com.strv.chat.firestore.di.firestoreChatClient
import com.strv.chat.firestore.di.firestoreConversationClient
import com.strv.chat.storage.di.cloudStorageMediaClient

class CompositionRoot {

    fun mediaProvider(): MediaProvider = MediaProviderImpl()

    fun memberProvider(): MemberProvider = MemberProviderImpl()

    fun chatClient(firebaseDb: FirebaseFirestore): ChatClient = firestoreChatClient(firebaseDb)

    fun conversationClient(firebaseDb: FirebaseFirestore): ConversationClient =
        firestoreConversationClient(firebaseDb)

    fun mediaClient(firebaseStore: FirebaseStorage): MediaClient =
        cloudStorageMediaClient(firebaseStore)
}