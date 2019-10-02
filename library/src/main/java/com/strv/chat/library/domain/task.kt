package com.strv.chat.library.domain

import com.strv.chat.library.domain.ObservableTask.ObservableTaskImpl
import com.strv.chat.library.domain.ProgressTask.ProgressTaskImpl
import com.strv.chat.library.domain.Task.TaskImpl

interface Disposable {

    fun dispose()
}

sealed class Task<R, E> : Disposable {

    abstract fun onSuccess(callback: (result: R) -> Unit): Task<R, E>

    abstract fun onError(callback: (error: E) -> Unit): Task<R, E>

    open class TaskImpl<R, E> : Task<R, E>() {

        private var result: R? = null
        private var error: E? = null
        private var completed = false
        private val successful get() = completed && error == null

        private val successCallbacks = mutableListOf<(R) -> Unit>()
        private val errorCallbacks = mutableListOf<(E) -> Unit>()

        override fun onSuccess(callback: (result: R) -> Unit) = apply {
            successCallbacks.add(callback)

            if (completed && successful && result != null)
                callback(result!!)
        }

        override fun onError(callback: (error: E) -> Unit) = apply {
            errorCallbacks.add(callback)

            if (completed && !successful && error != null)
                callback(error!!)
        }

        fun invokeSuccess(result: R) {
            completed = true
            this.result = result

            successCallbacks.forEach { it.invoke(result) }
        }

        fun invokeError(error: E) {
            completed = true
            this.error = error

            errorCallbacks.forEach { it.invoke(error) }
        }

        override fun dispose() {
            result = null
            error = null
            successCallbacks.removeAll(successCallbacks)
            errorCallbacks.removeAll(errorCallbacks)
        }
    }
}

sealed class ProgressTask<R, E> : TaskImpl<R, E>(), Disposable {

    abstract fun onProgress(callback: (progress: Int) -> Unit): ProgressTask<R, E>

    class ProgressTaskImpl<R, E>(
        var onDispose: (() -> Unit)?
    ) : ProgressTask<R, E>() {

        private val progressCallbacks = mutableListOf<(Int) -> Unit>()

        override fun onProgress(callback: (progress: Int) -> Unit) = this.apply {
            progressCallbacks.add(callback)
        }

        fun invokeProgress(progress: Int) {
            progressCallbacks.forEach { it.invoke(progress) }
        }

        override fun dispose() {
            super.dispose()
            progressCallbacks.removeAll(progressCallbacks)

            onDispose?.invoke()
            onDispose = null
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
    noinline onDispose: (() -> Unit)? = null,
    runnable: ProgressTaskImpl<R, E>.() -> Unit
): ProgressTask<R, E> =
    ProgressTaskImpl<R, E>(onDispose).apply(runnable)

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


