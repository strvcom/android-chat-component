package com.strv.chat.core.data.source

import com.strv.chat.core.data.entity.SourceEntity
import com.strv.chat.core.domain.task.ObservableTask
import com.strv.chat.core.domain.task.Task

/**
 * A generic source of data that deals with responses of type List<[Entity]>.
 *
 * @param Entity the type of elements contained in the [List] response.
 */
interface ListSource<Entity : SourceEntity> {

    /**
     * Executes the source and returns the result wrapped in [Task].
     *
     * @return [Task]<List<Entity, Throwable>>.
     */
    fun get(): Task<List<Entity>, Throwable>

    /**
     * Starts listening to the source.
     *
     * @return [ObservableTask]<List<Entity, Throwable>>.
     */
    fun subscribe(): ObservableTask<List<Entity>, Throwable>

    /**
     * Stops listening to the source.
     */
    fun unsubscribe()
}