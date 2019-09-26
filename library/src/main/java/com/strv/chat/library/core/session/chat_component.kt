package com.strv.chat.library.core.session

import com.strv.chat.library.core.session.config.Configuration
import com.strv.chat.library.core.ui.chat.messages.adapter.DefaultChatItemBinder
import com.strv.chat.library.core.ui.chat.messages.adapter.StyleableChatItemBinder
import com.strv.chat.library.core.ui.chat.messages.style.ChatRecyclerViewStyle

object ChatComponent {

    internal lateinit var configuration: Configuration

    fun init(configuration: Configuration) {
        ChatComponent.configuration = configuration
    }

    internal fun chatClient() = configuration.chatClient
    internal fun conversationClient() = configuration.conversationClient
    internal fun mediaClient() = configuration.mediaClient

    internal fun channelId() = configuration.serviceConfig.channelId
    internal fun largeIconRes() = configuration.serviceConfig.largeIconRes
    internal fun smallIconProgressRes() = configuration.serviceConfig.smallIconProgressRes
    internal fun smallIconSuccessRes() = configuration.serviceConfig.smallIconSuccessRes
    internal fun smallIconErrorRes() = configuration.serviceConfig.smallIconErrorRes

    internal fun defaultChatItemBinder() = DefaultChatItemBinder()
    internal fun styleableChatItemBinder(style: ChatRecyclerViewStyle) = StyleableChatItemBinder(style)
}