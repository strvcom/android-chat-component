package com.strv.chat.core.data.source

import com.strv.chat.core.data.entity.SourceEntity
import com.strv.chat.core.domain.task.ObservableTask
import com.strv.chat.core.domain.task.Task

/**
 * A generic source of data that deals with responses of type [Entity].
 *
 * @param Entity the type of element contained in the response.
 */
interface Source<Entity : SourceEntity> {

    /**
     * Executes the source and returns the result wrapped in [Task].
     *
     * @return [Task]<Entity, Throwable>.
     */
    fun get(): Task<Entity, Throwable>

    /**
     * Starts listening to the source.
     *
     * @return [ObservableTask]<Entity, Throwable>.
     */
    fun subscribe(): ObservableTask<Entity, Throwable>

    /**
     * Stops listening to the source.
     */
    fun unsubscribe()
}