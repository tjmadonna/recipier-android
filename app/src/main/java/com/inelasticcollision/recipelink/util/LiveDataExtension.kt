package com.inelasticcollision.recipelink.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun <R, S, T> LiveData<R>.combine(otherSource: LiveData<S>, onChanged: (R, S) -> T): MediatorLiveData<T> {
    val mediatorLiveData = MediatorLiveData<T>()
    mediatorLiveData.addSource(this) { value1 ->
        otherSource.value?.let { otherValue ->
            mediatorLiveData.value = onChanged.invoke(value1, otherValue)
        }
    }
    mediatorLiveData.addSource(otherSource) { value2 ->
        this.value?.let { thisValue ->
            mediatorLiveData.value = onChanged.invoke(thisValue, value2)
        }
    }
    return mediatorLiveData
}