package com.strv.chat.library.data.source.observers

import com.strv.chat.library.data.entity.SourceEntity

interface SourceObserver<Entity : SourceEntity> {

    fun onSuccess(item: Entity?)

    fun onError(error: Throwable)
}

interface ListSourceObserver<Entity : SourceEntity> {

    fun onSuccess(list: List<Entity>)

    fun onError(error: Throwable)
}