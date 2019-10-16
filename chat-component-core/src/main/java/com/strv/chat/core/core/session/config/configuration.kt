package com.strv.chat.core.core.session.config

import com.strv.chat.core.domain.client.ChatClient
import com.strv.chat.core.domain.client.ConversationClient
import com.strv.chat.core.domain.client.MediaClient
import com.strv.chat.core.domain.client.MemberClient
import com.strv.chat.core.domain.provider.ChatMemberProvider

class Configuration(
    internal val chatClient: ChatClient,
    internal val conversationClient: ConversationClient,
    internal val memberClient: MemberClient,
    internal val mediaClient: MediaClient,
    internal val serviceConfig: ServiceConfig
)