package com.strv.chat.component.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.strv.chat.component.business.FileProviderImpl
import com.strv.chat.component.business.ChatMemberProviderImpl
import com.strv.chat.core.domain.client.ChatClient
import com.strv.chat.core.domain.client.ConversationClient
import com.strv.chat.core.domain.client.MediaClient
import com.strv.chat.core.domain.client.MemberClient
import com.strv.chat.core.domain.provider.FileProvider
import com.strv.chat.core.domain.provider.ChatMemberProvider
import com.strv.chat.firestore.di.firestoreChatClient
import com.strv.chat.firestore.di.firestoreConversationClient
import com.strv.chat.firestore.di.firestoreMemberClient
import com.strv.chat.storage.di.cloudStorageMediaClient

class CompositionRoot {

    private var fileProvider: FileProvider? = null
    private var chatMemberProvider: ChatMemberProvider? = null

    fun mediaProvider(): FileProvider {
        if (fileProvider == null) {
            fileProvider = FileProviderImpl()
        }

        return fileProvider!!
    }

    fun memberProvider(): ChatMemberProvider {
        if (chatMemberProvider == null) {
            chatMemberProvider = ChatMemberProviderImpl()
        }

        return chatMemberProvider!!
    }

    fun chatClient(firebaseDb: FirebaseFirestore): ChatClient = firestoreChatClient(firebaseDb)

    fun conversationClient(firebaseDb: FirebaseFirestore): ConversationClient =
        firestoreConversationClient(firebaseDb)

    fun memberClient(firebaseDb: FirebaseFirestore, currentUserId: String): MemberClient =
        firestoreMemberClient(firebaseDb, currentUserId)

    fun mediaClient(firebaseStore: FirebaseStorage): MediaClient =
        cloudStorageMediaClient(firebaseStore)
}