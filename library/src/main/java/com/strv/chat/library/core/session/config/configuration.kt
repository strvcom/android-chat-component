package com.strv.chat.library.core.session.config

import com.strv.chat.library.domain.client.ChatClient
import com.strv.chat.library.domain.client.ConversationClient
import com.strv.chat.library.domain.client.MediaClient

class Configuration(
    internal val chatClient: ChatClient,
    internal val conversationClient: ConversationClient,
    internal val mediaClient: MediaClient,
    internal val serviceConfig: ServiceConfig
)