package com.strv.chat.library.business.common

interface Observable<T> {

    fun registerListener(listener: T)

    fun unregisterListener(listener: T)
}

abstract class ObservableComponent<T>(
): Observable<T> {

    private val listeners: HashSet<T> = hashSetOf()

    override fun registerListener(listener: T) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: T) {
        listeners.remove(listener)
    }

    fun listeners() = listeners.toSet()
}