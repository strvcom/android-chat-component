package com.strv.chat.core.core.session.config

import com.strv.chat.core.domain.ImageLoader
import com.strv.chat.core.domain.client.ChatClient
import com.strv.chat.core.domain.client.ConversationClient
import com.strv.chat.core.domain.client.MediaClient
import com.strv.chat.core.domain.client.MemberClient

class Configuration(
    internal val chatClient: ChatClient,
    internal val conversationClient: ConversationClient,
    internal val memberClient: MemberClient,
    internal val mediaClient: MediaClient,
    internal val imageLoader: ImageLoader,
    internal val serviceConfig: ServiceConfig
)