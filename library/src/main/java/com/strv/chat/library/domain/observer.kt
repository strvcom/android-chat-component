package com.strv.chat.library.domain

import com.strv.chat.library.domain.model.ChatItem

interface MessagesObserver {

    fun onComplete(list: List<ChatItem>)

    fun onNext(list: List<ChatItem>)

    fun onError(error: Throwable)
}
