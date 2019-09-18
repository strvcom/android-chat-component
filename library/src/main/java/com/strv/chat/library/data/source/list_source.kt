package com.strv.chat.library.data.source

import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.domain.ObservableTask
import com.strv.chat.library.domain.Task

interface ListSource<Entity : SourceEntity> {

    fun get(): Task<List<Entity>, Throwable>

    fun subscribe(): ObservableTask<List<Entity>, Throwable>

    fun unsubscribe()
}