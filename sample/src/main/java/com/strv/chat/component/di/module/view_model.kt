package com.strv.chat.component.di.module

import com.strv.chat.component.ui.ChatViewModel
import com.strv.chat.component.ui.ImageDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (conversationId: String, otherMemberIds: List<String>) ->
        ChatViewModel(conversationId, get(), otherMemberIds, get())
    }
    viewModel { (url: String) ->
        ImageDetailViewModel(url)
    }
}