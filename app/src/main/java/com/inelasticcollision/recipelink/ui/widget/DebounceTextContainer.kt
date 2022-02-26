package com.inelasticcollision.recipelink.ui.widget

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebounceTextContainer(
    val editText: EditText,
    private val coroutineScope: CoroutineScope
) : TextWatcher, View.OnFocusChangeListener {

    companion object {
        private const val debounceDelay: Long = 250 // ms
    }

    interface OnContentChangeListener {
        fun onTextChange(container: DebounceTextContainer, text: String?)
        fun onFocusChange(container: DebounceTextContainer, isFocused: Boolean)
    }

    var onContentChangeListener: OnContentChangeListener? = null

    private var textChangeJob: Job? = null

    private var focusChangeJob: Job? = null

    init {
        editText.addTextChangedListener(this)
        editText.onFocusChangeListener = this
    }

    // TextWatcher

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        textChangeJob?.cancel()
        textChangeJob = coroutineScope.launch {
            delay(debounceDelay)
            onContentChangeListener?.onTextChange(this@DebounceTextContainer, s?.toString())
        }
    }

    override fun afterTextChanged(s: Editable?) = Unit

    // View.OnFocusChangeListener

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        focusChangeJob?.cancel()
        focusChangeJob = coroutineScope.launch {
            delay(debounceDelay)
            onContentChangeListener?.onFocusChange(this@DebounceTextContainer, hasFocus)
        }
    }
}