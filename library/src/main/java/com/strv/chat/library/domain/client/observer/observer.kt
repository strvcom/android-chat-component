package com.strv.chat.library.domain.client.observer

interface Observer<T> {

    fun onSuccess(response: T)

    fun onError(error: Throwable)
}

inline fun <T, R> Observer<T>.convert(crossinline transform: (R) -> T): Observer<R> {
    return object : Observer<R> {
        override fun onSuccess(response: R) {
            this@convert.onSuccess(transform(response))
        }

        override fun onError(error: Throwable) {
            this@convert.onError(error)
        }
    }
}