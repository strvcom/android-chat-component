package com.strv.chat.core.domain

import com.strv.chat.core.domain.ObservableTask.ObservableTaskImpl
import com.strv.chat.core.domain.ProgressTask.ProgressTaskImpl
import com.strv.chat.core.domain.Task.TaskImpl
import java.util.LinkedList

interface Disposable {

    fun dispose()
}

sealed class Task<R, E> {

    abstract fun onSuccess(callback: (result: R) -> Unit): Task<R, E>

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

        open fun invokeSuccess(result: R) {
            completed = true
            this.result = result

            successCallbacks.collect { it.invoke(result) }
        }

        fun invokeError(error: E) {
            completed = true
            this.error = error

            errorCallbacks.collect { it.invoke(error) }
        }
    }
}

sealed class ProgressTask<R, E> : TaskImpl<R, E>() {

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

        fun invokeProgress(progress: Int) {
            progressCallbacks.forEach { it.invoke(progress) }
        }
    }
}

sealed class ObservableTask<R, E> : Disposable {

    abstract fun onNext(callback: (result: R) -> Unit): ObservableTask<R, E>

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

        fun invokeNext(item: R) {
            onNextCallbacks.forEach { it.invoke(item) }
        }

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

inline fun <R, E> task(runnable: TaskImpl<R, E>.() -> Unit): Task<R, E> =
    TaskImpl<R, E>().apply(runnable)

inline fun <R, E> observableTask(
    noinline onDispose: (() -> Unit)?,
    runnable: ObservableTaskImpl<R, E>.() -> Unit
): ObservableTask<R, E> =
    ObservableTaskImpl<R, E>(onDispose).apply(runnable)

inline fun <R, E> progressTask(
    runnable: ProgressTaskImpl<R, E>.() -> Unit
): ProgressTask<R, E> =
    ProgressTaskImpl<R, E>().apply(runnable)

inline fun <R, E, V> Task<R, E>.map(crossinline transform: (R) -> V) =
    task<V, E> {
        this@map.onSuccess { result ->
            invokeSuccess(transform(result))
        }

        this@map.onError { error ->
            invokeError(error)
        }
    }

inline fun <R, E, V> ProgressTask<R, E>.map(crossinline transform: (R) -> V) =
    progressTask<V, E> {
        this@map.onSuccess { result ->
            invokeSuccess(transform(result))
        }

        this@map.onError { error ->
            invokeError(error)
        }

        this@map.onProgress { progress ->
            invokeProgress(progress)
        }
    }

fun <R, E, V> Task<R, E>.flatMap(transform: (R) -> Task<V, E>): Task<V, E> =
    task {

        this@flatMap.onSuccess { result ->
            transform(result).onSuccess {
                this.invokeSuccess(it)
            }.onError { error ->
                this.invokeError(error)
            }
        }

        this@flatMap.onError { error ->
            this.invokeError(error)
        }
    }

inline fun <R, E, V> ObservableTask<R, E>.map(crossinline transform: (R) -> V) =
    when (this) {
        is ObservableTaskImpl -> {
            observableTask<V, E>(onDispose) {
                this@map.onNext { result ->
                    invokeNext(transform(result))
                }

                this@map.onError { error ->
                    invokeError(error)
                }
            }
        }
    }

internal fun <R, E, V> ObservableTask<List<R>, E>.mapIterable(transform: (R) -> V) =
    when (this) {
        is ObservableTaskImpl -> {
            observableTask<List<V>, E>(onDispose) {
                this@mapIterable.onNext { rResult ->

                    invokeNext(rResult.map(transform))
                }

                this@mapIterable.onError { error ->
                    invokeError(error)
                }
            }
        }
    }

internal fun <T, R : Comparable<R>, E> ObservableTask<List<T>, E>.sortedBy(transform: (T) -> R?) =
    when (this) {
        is ObservableTaskImpl -> {
            observableTask<List<T>, E>(onDispose) {
                this@sortedBy.onNext { result ->
                    invokeNext(result.sortedBy(transform))
                }

                this@sortedBy.onError { error ->
                    invokeError(error)
                }
            }
        }
    }

