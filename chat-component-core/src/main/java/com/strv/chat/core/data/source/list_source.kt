package com.strv.chat.core.data.source

import com.strv.chat.core.data.entity.SourceEntity
import com.strv.chat.core.domain.task.ObservableTask
import com.strv.chat.core.domain.task.Task

interface ListQuerySource<Entity : SourceEntity> {

    fun get(): Task<List<Entity>, Throwable>

    fun subscribe(): ObservableTask<List<Entity>, Throwable>

    fun unsubscribe()
}