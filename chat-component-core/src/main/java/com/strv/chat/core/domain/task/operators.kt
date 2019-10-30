package com.strv.chat.core.domain.task

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
        is ObservableTask.ObservableTaskImpl -> {
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
        is ObservableTask.ObservableTaskImpl -> {
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
        is ObservableTask.ObservableTaskImpl -> {
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