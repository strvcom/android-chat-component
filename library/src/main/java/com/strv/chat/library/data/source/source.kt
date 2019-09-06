package com.strv.chat.library.data.source

import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.data.source.observers.SourceObserver

interface Source<Entity : SourceEntity> {

    fun get(observer: SourceObserver<Entity>)

    fun subscribe(observer: SourceObserver<Entity>): Source<Entity>

    fun unsubscribe()
}