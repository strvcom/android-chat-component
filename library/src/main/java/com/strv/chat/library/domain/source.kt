package com.strv.chat.library.domain

import com.strv.chat.library.data.model.SourceEntity

interface ListSource<T : SourceEntity> {

    fun get(onSuccess: (List<T>) -> Unit, onError: (Throwable) -> Unit)

    fun remove()
}