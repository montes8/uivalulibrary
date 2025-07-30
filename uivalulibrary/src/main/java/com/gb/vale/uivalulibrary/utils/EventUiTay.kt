package com.gb.vale.uivalulibrary.utils

import androidx.lifecycle.Observer

open class EventUiTay<out T>(private val content: T) {
    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}

class EventUiTayObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<EventUiTay<T>> {
    override fun onChanged(event: EventUiTay<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}
