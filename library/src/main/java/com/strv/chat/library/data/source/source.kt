package com.strv.chat.library.data.source

import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.domain.client.observer.Observer

interface Source<Entity : SourceEntity> {

    fun get(observer: Observer<Entity?>)

    fun subscribe(observer: Observer<Entity?>): Source<Entity>

    fun unsubscribe()
}