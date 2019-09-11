package com.strv.chat.library.domain

import com.strv.chat.library.domain.model.ChatItem
import com.strv.chat.library.common.Observable

/*
    TODO
    The project has dependencies on both - Firestore and Firebase +- Retrofit (btw do we really need it)???
    Is it possible to add dependencies based on the current implementation? Multimodule? Artifacts?
 */

interface ChatClient : Observable<ChatClient.Listener> {

    interface Listener {
        fun onMessagesChanged(chatItems: List<ChatItem>)
        fun onMessagesFetchFailed(exception: Throwable)
    }

    fun getMessages()

    fun sendMessage()
}