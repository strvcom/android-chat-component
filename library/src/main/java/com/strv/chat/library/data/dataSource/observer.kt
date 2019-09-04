package com.strv.chat.library.data.dataSource

import com.strv.chat.library.data.entity.SourceEntity

interface ListSourceObserver<Entity : SourceEntity> {

    fun onSuccess(list: List<Entity>)

    fun onError(error: Throwable)
}