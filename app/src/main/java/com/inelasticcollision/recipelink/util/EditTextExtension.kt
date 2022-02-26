package com.inelasticcollision.recipelink.util

import android.widget.EditText

var EditText.textString: String?
    get() = text?.toString()
    set(value) {
        if (value != text.toString()) {
            setText(value)
        }
    }