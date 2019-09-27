package com.strv.chat.component.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.strv.chat.component.business.MediaProviderImpl
import com.strv.chat.component.business.MemberProviderImpl
import com.strv.chat.library.cloudStorage.di.cloudStorageMediaClient
import com.strv.chat.library.domain.client.ChatClient
import com.strv.chat.library.domain.client.ConversationClient
import com.strv.chat.library.domain.client.MediaClient
import com.strv.chat.library.domain.provider.MediaProvider
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.firestore.di.firestoreChatClient
import com.strv.chat.library.firestore.di.firestoreConversationClient

class CompositionRoot {

    fun mediaProvider(): MediaProvider = MediaProviderImpl()

    fun memberProvider(): MemberProvider = MemberProviderImpl()

    fun chatClient(firebaseDb: FirebaseFirestore): ChatClient = firestoreChatClient(firebaseDb)

    fun conversationClient(firebaseDb: FirebaseFirestore): ConversationClient =
        firestoreConversationClient(firebaseDb)

    fun mediaClient(firebaseStore: FirebaseStorage): MediaClient =
        cloudStorageMediaClient(firebaseStore)
}