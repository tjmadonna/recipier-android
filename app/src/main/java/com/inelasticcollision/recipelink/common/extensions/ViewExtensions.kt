package com.inelasticcollision.recipelink.common.extensions

import android.view.View

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) = when (value) {
        true -> visibility = View.VISIBLE
        false -> visibility = View.GONE
    }