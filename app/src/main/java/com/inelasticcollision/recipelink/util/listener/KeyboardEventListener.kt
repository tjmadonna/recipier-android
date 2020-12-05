package com.inelasticcollision.recipelink.util.listener

import android.view.ViewTreeObserver
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.inelasticcollision.recipelink.util.isKeyboardOpen


// Adapted from "How to detect if the android keyboard is open" by Ian Alexander
// https://proandroiddev.com/how-to-detect-if-the-android-keyboard-is-open-269b255a90f5)
class KeyboardEventListener(
    private val fragment: Fragment,
    private val callback: (isOpen: Boolean) -> Unit
) : LifecycleObserver {

    private val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
        private var lastState: Boolean = fragment.isKeyboardOpen()

        override fun onGlobalLayout() {
            val isOpen = fragment.isKeyboardOpen()
            if (isOpen == lastState) {
                return
            } else {
                dispatchKeyboardEvent(isOpen)
                lastState = isOpen
            }
        }
    }

    init {
        // Dispatch the current state of the keyboard
        fragment.lifecycle.addObserver(this)
        registerKeyboardListener()
    }

    private fun registerKeyboardListener() {
        fragment.view?.viewTreeObserver?.addOnGlobalLayoutListener(listener)
    }

    private fun dispatchKeyboardEvent(isOpen: Boolean) {
        when {
            isOpen -> callback(true)
            !isOpen -> callback(false)
        }
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
    @CallSuper
    fun onLifecyclePause() {
        unregisterKeyboardListener()
    }

    private fun unregisterKeyboardListener() {
        fragment.view?.viewTreeObserver?.removeOnGlobalLayoutListener(listener)
    }
}