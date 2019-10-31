package com.strv.chat.core.domain.task

/**
 * Returns a [Task] containing the results of applying the given [transform] function
 * to the result of the original [Task].
 *
 * @param transform function that transforms the result.
 */
inline fun <R, E, V> Task<R, E>.map(crossinline transform: (R) -> V) =
    task<V, E> {
        this@map.onSuccess { result ->
            invokeSuccess(transform(result))
        }

        this@map.onError { error ->
            invokeError(error)
        }
    }

/**
 * Returns a [Task] that reacts on changes of the original [Task] and retransmits a transformed result,
 * specified by the given [transform] function, of the original [Task].
 *
 * @param transform function that transforms the result into a [Task].
 */
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

/**
 * Returns a [ProgressTask] containing the results of applying the given [transform] function
 * to the result of the original [ProgressTask].
 *
 * @param transform function that transforms the result.
 */
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

/**
 * Returns a [ObservableTask] containing the results of applying the given [transform] function
 * to the result of the original [ObservableTask].
 *
 * @param transform function that transforms the result.
 */
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

/**
 * Returns a [ObservableTask] containing the results of applying the given [transform] function
 * to each element of the result of the origin [ObservableTask].
 *
 * @param transform function that transforms the result.
 */
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

/**
 * Returns a [ObservableTask] with all elements sorted according to natural sort order of the value
 * returned by specified [selector] function.
 *
 * @param selector function that defines the way how to sort items.
 */
internal fun <T, R : Comparable<R>, E> ObservableTask<List<T>, E>.sortedBy(selector: (T) -> R?) =
    when (this) {
        is ObservableTask.ObservableTaskImpl -> {
            observableTask<List<T>, E>(onDispose) {
                this@sortedBy.onNext { result ->
                    invokeNext(result.sortedBy(selector))
                }

                this@sortedBy.onError { error ->
                    invokeError(error)
                }
            }
        }
    }