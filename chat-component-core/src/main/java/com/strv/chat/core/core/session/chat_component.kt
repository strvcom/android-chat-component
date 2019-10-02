package com.strv.chat.core.core.session

import android.app.Application
import com.strv.chat.core.core.session.config.Configuration
import com.strv.chat.core.core.ui.ChatComponentResourceProvider
import com.strv.chat.core.core.ui.chat.data.ChatItemView
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatAdapter
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatViewHolderProvider
import com.strv.chat.core.core.ui.chat.messages.style.ChatRecyclerViewStyle
import com.strv.chat.core.core.ui.conversation.adapter.ConversationAdapter
import com.strv.chat.core.core.ui.conversation.adapter.ConversationViewHolderProvider
import com.strv.chat.core.core.ui.conversation.data.ConversationItemView
import com.strv.chat.core.core.ui.conversation.style.ConversationRecyclerViewStyle
import com.strv.chat.core.core.ui.extensions.OnClickAction

object ChatComponent {

    private lateinit var configuration: Configuration
    private lateinit var resourceProvider: ChatComponentResourceProvider

    fun init(app: Application, configuration: Configuration) {
        ChatComponent.configuration = configuration
        ChatComponent.resourceProvider = ChatComponentResourceProvider(app)
    }

    internal fun chatClient() = configuration.chatClient
    internal fun conversationClient() = configuration.conversationClient
    internal fun mediaClient() = configuration.mediaClient

    internal fun memberProvider() = configuration.memberProvider

    internal fun channelId() = configuration.serviceConfig.channelId
    internal fun largeIconRes() = configuration.serviceConfig.largeIconRes
    internal fun smallIconProgressRes() = configuration.serviceConfig.smallIconProgressRes
    internal fun smallIconSuccessRes() = configuration.serviceConfig.smallIconSuccessRes
    internal fun smallIconErrorRes() = configuration.serviceConfig.smallIconErrorRes

    internal fun chatAdapter(chatViewHolderProvider: ChatViewHolderProvider, onClickAction: OnClickAction<ChatItemView>, style: ChatRecyclerViewStyle?) = ChatAdapter(chatViewHolderProvider, onClickAction, style)
    internal fun chatViewHolderProvider() = ChatViewHolderProvider()

    internal fun conversationAdapter(
        conversationViewHolderProvider: ConversationViewHolderProvider,
        onClickAction: OnClickAction<ConversationItemView>,
        style: ConversationRecyclerViewStyle?) =
        ConversationAdapter(conversationViewHolderProvider, onClickAction, style)

    internal fun conversationViewHolderProvider() = ConversationViewHolderProvider()

    internal fun string(resId: Int) = resourceProvider.string[resId]
}