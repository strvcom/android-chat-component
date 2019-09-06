package com.strv.chat.library.domain.client

import com.strv.chat.library.domain.model.MessageModel

interface ChatObserver {

    fun onNext(list: List<MessageModel>)

    fun onError(error: Throwable)
}
