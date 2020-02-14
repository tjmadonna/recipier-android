package com.inelasticcollision.recipelink.ui.common.widgets

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DebouncedEditText : TextInputEditText, CoroutineScope, TextWatcher {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var debounceJob: Job? = null

    var textChangeCallback: ((String?) -> Unit)? = null

    var delay: Long = 500

    constructor(
            context: Context
    ) : super(context) {
        setupView()
    }

    constructor(
            context: Context,
            attrs: AttributeSet?
    ) : super(context, attrs) {
        setupView()
    }

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        setupView()
    }

    private fun setupView() {
        addTextChangedListener(this)
    }

    override fun onDetachedFromWindow() {
        debounceJob?.cancel()
        debounceJob = null
        super.onDetachedFromWindow()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        debounceJob?.cancel()
        debounceJob = null
        debounceJob = launch {
            delay(delay)
            textChangeCallback?.invoke(text?.toString())
        }
    }

    override fun afterTextChanged(s: Editable?) {}

    var textString: String?
        get() = text.toString()
        set(value) {
            if (value != text.toString()) {
                setText(value)
            }
        }
}