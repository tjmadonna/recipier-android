package com.inelasticcollision.recipelink.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.inelasticcollision.recipelink.R
import kotlinx.coroutines.*

class DebouncedEditText : AppCompatEditText {

    interface OnTextChangeListener {
        fun onTextChange(text: String?)
    }

    private var debounceDelay: Long = 250 // ms

    private var job: Job? = null

    private val coroutineScope: CoroutineScope? = CoroutineScope(Dispatchers.Main + SupervisorJob())

    var editable: Boolean = true

    var onTextChangeListener: OnTextChangeListener? = null

    var textString: String?
        get() = text?.toString()
        set(value) {
            if (value != text.toString()) {
                setText(value)
            }
        }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.DebouncedEditText, defStyle, 0
        )

        debounceDelay = a.getInt(
            R.styleable.DebouncedEditText_debounceDelay,
            debounceDelay.toInt()
        ).toLong()

        a.recycle()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        if (editable) {
            super.setText(text, type)
        }
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        job?.cancel()
        job = coroutineScope?.launch {
            delay(debounceDelay)
            onTextChangeListener?.onTextChange(textString)
        }
    }
}
