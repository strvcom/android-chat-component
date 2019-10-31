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
import com.strv.chat.core.domain.ImageLoader

/**
 * Root component of the SDK.
 *
 * Allows entry configuration of the SDK and acts like a service locator.
 */
class ChatComponent private constructor(
    private val configuration: Configuration,
    private val resourceProvider: ChatComponentResourceProvider
) {

    companion object {

        internal lateinit var chatComponent: ChatComponent

        private fun setInstance(instance: ChatComponent) {
            chatComponent = instance
        }

        /**
         * Initializes the SDK.
         *
         * Must be called so that the SDK works correctly.
         *
         * @param app Application.
         * @param configuration Wrapper for all components that the library uses.
         */
        fun init(app: Application, configuration: Configuration) {
            setInstance(ChatComponent(configuration, ChatComponentResourceProvider(app)))
        }
    }

    //method shortcuts
    internal val currentUserId = memberClient().currentUserId()

    internal fun chatClient() = configuration.chatClient
    internal fun conversationClient() = configuration.conversationClient
    internal fun memberClient() = configuration.memberClient
    internal fun mediaClient() = configuration.mediaClient

    internal fun channelId() = configuration.serviceConfig.channelId
    internal fun largeIconRes() = configuration.serviceConfig.largeIconRes
    internal fun smallIconProgressRes() = configuration.serviceConfig.smallIconProgressRes
    internal fun smallIconSuccessRes() = configuration.serviceConfig.smallIconSuccessRes
    internal fun smallIconErrorRes() = configuration.serviceConfig.smallIconErrorRes

    internal fun string(resId: Int) = resourceProvider.string[resId]

    //DI
    internal fun chatAdapter(
        chatViewHolderProvider: ChatViewHolderProvider,
        imageLoader: ImageLoader?,
        onClickAction: OnClickAction<ChatItemView>?,
        style: ChatRecyclerViewStyle?
    ) = ChatAdapter(chatViewHolderProvider, imageLoader, onClickAction, style)

    internal fun chatViewHolderProvider() = ChatViewHolderProvider()

    internal fun conversationAdapter(
        conversationViewHolderProvider: ConversationViewHolderProvider,
        imageLoader: ImageLoader?,
        onClickAction: OnClickAction<ConversationItemView>?,
        style: ConversationRecyclerViewStyle?
    ) =
        ConversationAdapter(conversationViewHolderProvider, imageLoader, onClickAction, style)

    internal fun conversationViewHolderProvider() = ConversationViewHolderProvider()
}