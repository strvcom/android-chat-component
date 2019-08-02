package com.strv.chat.library.business

import com.strv.chat.library.business.common.Observable
import com.strv.chat.library.chat.domain.ChatItem

interface ChatClient : Observable<ChatClient.Listener> {

    interface Listener {
        fun onChatChanged(chatItems: List<ChatItem>)
    }
}