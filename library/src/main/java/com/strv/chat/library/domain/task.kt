package com.strv.chat.library.domain

import com.strv.chat.library.domain.ObservableTask.ObservableTaskImpl
import com.strv.chat.library.domain.Task.TaskImpl

interface Disposable {

    fun dispose()
}

sealed class Task<R, E> : Disposable {

    abstract fun onSuccess(callback: (result: R) -> Unit): Task<R, E>

    abstract fun onError(callback: (error: E) -> Unit): Task<R, E>

    class TaskImpl<R, E> : Task<R, E>() {

        private val successCallbacks = mutableListOf<(R) -> Unit>()
        private val errorCallbacks = mutableListOf<(E) -> Unit>()

        override fun onSuccess(callback: (result: R) -> Unit) = apply {
            successCallbacks.add(callback)
        }

        override fun onError(callback: (error: E) -> Unit) = apply {
            errorCallbacks.add(callback)
        }

        fun invokeSuccess(result: R) {
            successCallbacks.forEach { it.invoke(result) }
        }

        fun invokeError(error: E) {
            errorCallbacks.forEach { it.invoke(error) }
        }

        override fun dispose() {
            successCallbacks.removeAll(successCallbacks)
            errorCallbacks.removeAll(errorCallbacks)
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

fun <R, E> task(runnable: TaskImpl<R, E>.() -> Unit): Task<R, E> =
    TaskImpl<R, E>().apply(runnable)

fun <R, E> observableTask(
    onDispose: (() -> Unit)?,
    runnable: ObservableTaskImpl<R, E>.() -> Unit
): ObservableTask<R, E> =
    ObservableTaskImpl<R, E>(onDispose).apply(runnable)

fun <R, E, V> Task<R, E>.map(mapper: (R) -> V) =
    task<V, E> {
        this@map.onSuccess { result ->
            invokeSuccess(mapper(result))
        }

        this@map.onError { error ->
            invokeError(error)
        }
    }

fun <R, E, V> ObservableTask<R, E>.map(mapper: (R) -> V) =
    when (this) {
        is ObservableTaskImpl -> {
            observableTask<V, E>(onDispose) {
                this@map.onNext { result ->
                    invokeNext(mapper(result))
                }
            }
        }
    }


//class ProgressTask<R, E> : Task<R, E>() {
//    private var progress: Int = 0
//    private val progressCallbacks = mutableListOf<(Int) -> Unit>()
//
//    fun onProgress(callback: (progress: Int) -> Unit) = this.apply {
//        progressCallbacks.add(callback)
//    }
//
//    fun invokeProgress(progress: Int) {
//        this.progress = progress
//        progressCallbacks.forEach { it.invoke(progress) }
//    }
//}

//fun <R, E> progressTask(runnable: ProgressTask<R, E>.() -> Unit) =
//    ProgressTask<R, E>().apply(runnable)
