package com.strv.chat.library.business.common

interface Observable<Listener> {

    fun registerListener(listener: Listener)

    fun unregisterListener(listener: Listener)
}

abstract class ObservableComponent<Listener>(
) : Observable<Listener> {

    private val listeners: HashSet<Listener> = hashSetOf()

    override fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun notify(action: Listener.() -> Unit) = listeners.forEach(action)
}