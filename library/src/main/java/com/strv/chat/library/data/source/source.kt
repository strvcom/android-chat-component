package com.strv.chat.library.data.source

import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.domain.ObservableTask
import com.strv.chat.library.domain.Task

interface Source<Entity : SourceEntity> {

    fun get(): Task<Entity?, Throwable>

    fun subscribe(): ObservableTask<Entity?, Throwable>

    fun unsubscribe()
}