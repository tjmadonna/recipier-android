package com.inelasticcollision.recipelink.ui.widget

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.children
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.databinding.ItemTagsTextInputBinding
import kotlinx.coroutines.*
import java.util.*

class TextInputLayout : LinearLayout, DebouncedEditText.OnTextChangeListener,
    View.OnFocusChangeListener {

    interface OnTextInputChangeListener {
        fun onTextInputChanged(text: List<String>)
    }

    private var viewFocusDelay: Long = 250 // ms

    private var job: Job? = null

    private val coroutineScope: CoroutineScope? = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val recycledViewQueue: Queue<DebouncedEditText> = LinkedList()

    var editable: Boolean = true

    var onTextInputChangedListener: OnTextInputChangeListener? = null

    fun setOnTextInputChangeListener(listener: ((List<String>) -> Unit)?) {
        if (listener == null) {
            onTextInputChangedListener = null
            return
        }

        onTextInputChangedListener = object : OnTextInputChangeListener {
            override fun onTextInputChanged(text: List<String>) {
                listener.invoke((text))
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private val childrenList: List<DebouncedEditText>
        get() = children.toList() as List<DebouncedEditText>

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
            attrs, R.styleable.TextInputLayout, defStyle, 0
        )

        a.recycle()

        orientation = VERTICAL
        layoutTransition = LayoutTransition()
    }

    fun setTextInput(inputStrings: List<String>) {
        if (editable) {
            for (inputString in inputStrings) {
                val inputView = getView()
                inputView.setText(inputString)
                initializeListenersOnView(inputView)
                addView(inputView)
            }
            val inputView = getView()
            initializeListenersOnView(inputView)
            addView(inputView)
        }
    }

    private fun getView(): DebouncedEditText {
        return recycledViewQueue.poll() ?: createView()
    }

    private fun initializeListenersOnView(view: DebouncedEditText) {
        view.onTextChangeListener = this
        view.onFocusChangeListener = this
    }

    private fun createView(): DebouncedEditText {
        val binding = ItemTagsTextInputBinding.inflate(LayoutInflater.from(context), this, false)
        return binding.root
    }

    private fun recycleView(view: DebouncedEditText) {
        view.onTextChangeListener = null
        view.onFocusChangeListener = null
        view.textString = ""
        recycledViewQueue.add(view)
    }

    private fun getTextInputStrings(): List<String> {
        return childrenList
            .mapNotNull { view -> (view as? DebouncedEditText)?.textString }
            .filter { input -> input.isNotEmpty() }
    }

    private fun notifyTextInputChanged() {
        if (onTextInputChangedListener != null) {
            val inputs = getTextInputStrings()
            onTextInputChangedListener?.onTextInputChanged(inputs)
        }
    }

    // DebouncedEditText.OnTextChangeListener

    override fun onTextChange(text: String?) {
        notifyTextInputChanged()
        addLastTextInputIfNecessary()
    }

    // View.OnFocusChangeListener

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            (view as? DebouncedEditText)?.let { inputView ->
                val childrenList = childrenList
                if (childrenList.size <= 1) {
                    return@let
                }
                val viewIndex = childrenList.indexOf(inputView)
                if (viewIndex != childrenList.lastIndex && inputView.textString.isNullOrEmpty()) {
                    removeView(inputView)
                    recycleView(inputView)
                }
            }
        } else {
            view?.let { focusedView ->
                job?.cancel()
                job = coroutineScope?.launch {
                    // This fixes a bug where the edit text will come unfocused when a view in the
                    // layout is deleted. Looking for a more reliable solution
                    delay(viewFocusDelay)
                    if (focusedView != focusedChild) {
                        focusedView.requestFocus()
                    }
                }
            }
        }
    }

    private fun addLastTextInputIfNecessary() {
        childrenList.lastOrNull()?.let { inputView ->
            val text = inputView.text
            if (text != null && text.isNotEmpty()) {
                val newInputView = getView()
                initializeListenersOnView(newInputView)
                addView(newInputView)
            }
        } ?: run {
            val newInputView = getView()
            initializeListenersOnView(newInputView)
            addView(newInputView)
        }
    }
}