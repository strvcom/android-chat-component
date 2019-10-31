package com.strv.chat.core.core.session.config

import com.strv.chat.core.domain.client.ChatClient
import com.strv.chat.core.domain.client.ConversationClient
import com.strv.chat.core.domain.client.MediaClient
import com.strv.chat.core.domain.client.MemberClient

/**
 * Container holding SDK configuration.
 *
 * @constructor Creates a new configuration.
 * @property chatClient Provides interactions with the message source of data.
 * @property conversationClient Provides interactions with the conversation source of data.
 * @property memberClient Provides information about the current user and other members of a conversation.
 * @property mediaClient Provides interaction with a storage service.
 * @property serviceConfig Container holding configuration for a service.
 */
class Configuration(
    internal val chatClient: ChatClient,
    internal val conversationClient: ConversationClient,
    internal val memberClient: MemberClient,
    internal val mediaClient: MediaClient,
    internal val serviceConfig: ServiceConfig
)