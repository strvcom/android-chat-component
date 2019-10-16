package com.strv.chat.component.di.module

import com.strv.chat.component.business.FileProviderImpl
import com.strv.chat.component.business.ImageLoaderImpl
import com.strv.chat.core.domain.ImageLoader
import com.strv.chat.core.domain.client.ChatClient
import com.strv.chat.core.domain.client.ConversationClient
import com.strv.chat.core.domain.client.MediaClient
import com.strv.chat.core.domain.client.MemberClient
import com.strv.chat.core.domain.provider.FileProvider
import com.strv.chat.firestore.client.FirestoreChatClient
import com.strv.chat.firestore.client.FirestoreConversationClient
import com.strv.chat.firestore.client.FirestoreMemberClient
import com.strv.chat.storage.client.CloudStorageMediaClient
import org.koin.dsl.module

private val directoryName = "Chat_component"

val appModule = module {
    single<ImageLoader> { ImageLoaderImpl() }
    single<FileProvider> { FileProviderImpl(directoryName) }
    single<MemberClient> { (currentUserId: String) -> FirestoreMemberClient(get(), currentUserId) }
    single<ChatClient> { FirestoreChatClient(get()) }
    single<ConversationClient> { FirestoreConversationClient(get()) }
    single<MediaClient> { CloudStorageMediaClient(get()) }
}