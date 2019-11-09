package com.strv.chat.core.domain.task

import com.strv.chat.core.domain.collect
import com.strv.chat.core.domain.task.ObservableTask.ObservableTaskImpl
import com.strv.chat.core.domain.task.ProgressTask.ProgressTaskImpl
import com.strv.chat.core.domain.task.Task.TaskImpl
import java.util.LinkedList

/**
 * Represents a disposable resource.
 */
interface Disposable {

    /**
     * Dispose the resource.
     */
    fun dispose()
}

/**
 * Represents a promise that a computation will be done.
 * It is a wrapper around a single result of an asynchronous call.
 *
 * @param R result type in case of success
 * @param E result type in case of error
 */
sealed class Task<R, E> {

    /**
     * Specifies a callback to be run on success.
     *
     * @param callback the operation which will be performed if the task resolves successfully.
     */
    abstract fun onSuccess(callback: (result: R) -> Unit): Task<R, E>

    /**
     * Specifies a callback to be run on error.
     *
     * @param callback the operation which will be performed if the task resolves with en error.
     */
    abstract fun onError(callback: (error: E) -> Unit): Task<R, E>

    open class TaskImpl<R, E> : Task<R, E>() {

        private var result: R? = null
        private var error: E? = null
        private var completed = false
        private val successful get() = completed && error == null

        private val successCallbacks = LinkedList<(R) -> Unit>()
        private val errorCallbacks = LinkedList<(E) -> Unit>()

        override fun onSuccess(callback: (result: R) -> Unit) = apply {
            if (completed && successful && result != null) {
                callback(result!!)
            } else {
                successCallbacks.add(callback)
            }
        }

        override fun onError(callback: (error: E) -> Unit) = apply {
            if (completed && !successful && error != null) {
                callback(error!!)
            } else {
                errorCallbacks.add(callback)
            }
        }

        /**
         * Marks the task as successful passing a result as the return value of an asynchronous call.
         *
         * @param result result of the asynchronous call.
         */
        open fun invokeSuccess(result: R) {
            completed = true
            this.result = result

            successCallbacks.collect { it.invoke(result) }
        }

        /**
         * Marks the task as failed passing an error that occurred during an asynchronous call.
         *
         * @param error error that occurred during the asynchronous call.
         */
        fun invokeError(error: E) {
            completed = true
            this.error = error

            errorCallbacks.collect { it.invoke(error) }
        }
    }
}

/**
 * Represents a promise that a computation will be done.
 * It is a wrapper around a single result of an asynchronous call that periodically notifies about its progress.
 *
 * @param R result type in case of success.
 * @param E result type in case of error.
 */
sealed class ProgressTask<R, E> : TaskImpl<R, E>() {

    /**
     * Specifies a callback to be run when the progress value changes.
     *
     * @param callback the operation which will be performed when the task progress changes.
     */
    abstract fun onProgress(callback: (progress: Int) -> Unit): ProgressTask<R, E>

    class ProgressTaskImpl<R, E> : ProgressTask<R, E>() {

        private val progressCallbacks = mutableListOf<(Int) -> Unit>()

        override fun onProgress(callback: (progress: Int) -> Unit) = this.apply {
            progressCallbacks.add(callback)
        }

        override fun invokeSuccess(result: R) {
            super.invokeSuccess(result)

            progressCallbacks.removeAll(progressCallbacks)
        }

        /**
         * Emits a progress value of the task.
         *
         * @param progress value of the task progress.
         */
        fun invokeProgress(progress: Int) {
            progressCallbacks.forEach { it.invoke(progress) }
        }
    }
}

/**
 * Represents a promise that a computation will be done.
 * It is a wrapper around a stream that notifies about each change.
 *
 * @param R result type in case of success.
 * @param E result type in case of error.
 */
sealed class ObservableTask<R, E> : Disposable {

    /**
     * Specifies a callback to be run when an item is emitted.
     *
     * @param callback the operation which will be performed an item is emitted.
     */
    abstract fun onNext(callback: (result: R) -> Unit): ObservableTask<R, E>

    /**
     * Specifies a callback to be run on error.
     *
     * @param callback the operation which will be performed if the task resolves with en error.
     */
    abstract fun onError(callback: (error: E) -> Unit): ObservableTask<R, E>


    class ObservableTaskImpl<R, E>(
        var onDispose: (() -> Unit)?
    ) : ObservableTask<R, E>() {

        private val onNextCallbacks = mutableListOf<(R) -> Unit>()
        private val errorCallbacks = mutableListOf<(E) -> Unit>()

        override fun onNext(callback: (result: R) -> Unit) = apply {
            onNextCallbacks.add(callback)
        }

        override fun onError(callback: (error: E) -> Unit) = apply {
            errorCallbacks.add(callback)
        }

        /**
         * Emits a item.
         *
         * @param item to emit to the stream.
         */
        fun invokeNext(item: R) {
            onNextCallbacks.forEach { it.invoke(item) }
        }

        /**
         * Marks the task as failed passing an error that occurred during the stream processing.
         *
         * @param error error that occurred during the stream processing.
         */
        fun invokeError(error: E) {
            errorCallbacks.forEach { it.invoke(error) }
        }

        override fun dispose() {
            onNextCallbacks.removeAll(onNextCallbacks)

            onDispose?.invoke()
            onDispose = null
        }
    }
}

/**
 * Type-safe builder that allows converting an existing callback-based API into [Task] in a semi-declarative way.
 *
 * @param runnable function with [TaskImpl] receiver where [TaskImpl.invokeSuccess] and [TaskImpl.invokeError] can be called.
 *
 * @return [Task]
 */
inline fun <R, E> task(runnable: TaskImpl<R, E>.() -> Unit): Task<R, E> =
    TaskImpl<R, E>().apply(runnable)

/**
 * Type-safe builder that allows converting an existing callback-based API into [ObservableTask] in a semi-declarative way.
 *
 * @param onDispose function that defines how to dispose the source.
 * @param runnable function with [ObservableTaskImpl] receiver where [ObservableTaskImpl.invokeNext] and [ObservableTaskImpl.invokeError] can be called.
 *
 * @return [ObservableTask]
 */
inline fun <R, E> observableTask(
    noinline onDispose: (() -> Unit)?,
    runnable: ObservableTaskImpl<R, E>.() -> Unit
): ObservableTask<R, E> =
    ObservableTaskImpl<R, E>(onDispose).apply(runnable)

/**
 * Type-safe builder that allows converting an existing callback-based API into [ProgressTask] in a semi-declarative way.
 *
 * @param runnable function with [ProgressTaskImpl] receiver where [ProgressTaskImpl.invokeProgress], [ProgressTaskImpl.invokeSuccess] and [ObservableTaskImpl.invokeError] can be called.
 *
 * @return [ProgressTask]
 */
inline fun <R, E> progressTask(
    runnable: ProgressTaskImpl<R, E>.() -> Unit
): ProgressTask<R, E> =
    ProgressTaskImpl<R, E>().apply(runnable)

