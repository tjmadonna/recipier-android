package com.inelasticcollision.recipelink.common.extensions

import android.view.View
import android.widget.EditText

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) = when (value) {
        true -> visibility = View.VISIBLE
        false -> visibility = View.GONE
    }

var EditText.textString: String?
    get() = text.toString()
    set(value) = setText(value)