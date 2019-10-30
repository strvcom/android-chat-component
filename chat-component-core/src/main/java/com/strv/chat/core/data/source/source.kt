package com.strv.chat.core.data.source

import com.strv.chat.core.data.entity.SourceEntity
import com.strv.chat.core.domain.task.ObservableTask
import com.strv.chat.core.domain.task.Task

interface DocumentSource<Entity : SourceEntity> {

    fun get(): Task<Entity, Throwable>

    fun subscribe(): ObservableTask<Entity, Throwable>

    fun unsubscribe()
}