package com.strv.chat.library.domain.client.observer

import com.strv.chat.library.domain.model.MessageModel

interface ClientObserver {

    fun onNext(list: List<MessageModel>)

    fun onError(error: Throwable)
}
