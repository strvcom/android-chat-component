package com.strv.chat.library.data.source

import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.domain.client.observer.Observer

interface ListSource<Entity : SourceEntity> {

    fun get(observer: Observer<List<Entity>>)

    fun subscribe(observer: Observer<List<Entity>>): ListSource<Entity>

    fun unsubscribe()
}