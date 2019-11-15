package com.strv.chat.component.di.module

import com.strv.chat.component.business.FileProviderImpl
import com.strv.chat.component.business.ImageLoaderImpl
import com.strv.chat.core.domain.ImageLoader
import com.strv.chat.core.domain.provider.FileProvider
import com.strv.chat.firestore.di.firestoreChatClient
import com.strv.chat.firestore.di.firestoreConversationClient
import com.strv.chat.firestore.di.firestoreMemberClient
import com.strv.chat.storage.di.cloudStorageMediaClient
import org.koin.dsl.module

private val directoryName = "Chat_component"

val appModule = module {
    single<ImageLoader> { ImageLoaderImpl() }
    single<FileProvider> { FileProviderImpl(directoryName) }
    single { (currentUserId: String) -> firestoreMemberClient(get(), currentUserId) }
    single { firestoreChatClient(get()) }
    single { firestoreConversationClient(get()) }
    single { cloudStorageMediaClient(get()) }
}