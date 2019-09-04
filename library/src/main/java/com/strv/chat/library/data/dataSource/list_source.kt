package com.strv.chat.library.data.dataSource

import com.strv.chat.library.data.entity.SourceEntity

interface ListSource<Entity : SourceEntity> {

    fun get(observer: ListSourceObserver<Entity>)

    fun subscribe(observer: ListSourceObserver<Entity>): ListSource<Entity>

    fun unsubscribe()
}