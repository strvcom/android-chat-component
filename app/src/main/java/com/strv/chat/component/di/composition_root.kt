package com.strv.chat.component.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.strv.chat.component.business.MediaProviderImpl
import com.strv.chat.component.business.MemberProviderImpl
import com.strv.chat.core.domain.client.ChatClient
import com.strv.chat.core.domain.client.ConversationClient
import com.strv.chat.core.domain.client.MediaClient
import com.strv.chat.core.domain.client.MemberClient
import com.strv.chat.core.domain.provider.MediaProvider
import com.strv.chat.core.domain.provider.MemberProvider
import com.strv.chat.firestore.di.firestoreChatClient
import com.strv.chat.firestore.di.firestoreConversationClient
import com.strv.chat.firestore.di.firestoreMemberClient
import com.strv.chat.storage.di.cloudStorageMediaClient

class CompositionRoot {

    private var mediaProvider: MediaProvider? = null
    private var memberProvider: MemberProvider? = null

    fun mediaProvider(): MediaProvider {
        if (mediaProvider == null) {
            mediaProvider = MediaProviderImpl()
        }

        return mediaProvider!!
    }

    fun memberProvider(): MemberProvider {
        if (memberProvider == null) {
            memberProvider = MemberProviderImpl()
        }

        return memberProvider!!
    }

    fun chatClient(firebaseDb: FirebaseFirestore): ChatClient = firestoreChatClient(firebaseDb)

    fun conversationClient(firebaseDb: FirebaseFirestore): ConversationClient =
        firestoreConversationClient(firebaseDb)

    fun memberClient(firebaseDb: FirebaseFirestore, currentUserId: String): MemberClient =
        firestoreMemberClient(firebaseDb, currentUserId)

    fun mediaClient(firebaseStore: FirebaseStorage): MediaClient =
        cloudStorageMediaClient(firebaseStore)
}