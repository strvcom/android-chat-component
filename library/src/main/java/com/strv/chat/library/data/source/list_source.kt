package com.strv.chat.library.data.source

import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.data.source.observer.ListSourceObserver

interface ListSource<Entity : SourceEntity> {

    fun get(observer: ListSourceObserver<Entity>)

    fun subscribe(observer: ListSourceObserver<Entity>): ListSource<Entity>

    fun unsubscribe()
}