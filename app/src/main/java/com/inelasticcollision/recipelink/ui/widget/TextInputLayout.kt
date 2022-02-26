package com.inelasticcollision.recipelink.ui.widget

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.children
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.databinding.ItemTagsTextInputBinding
import com.inelasticcollision.recipelink.util.textString
import kotlinx.coroutines.CoroutineScope
import java.util.*

class TextInputLayout : LinearLayout, DebounceTextContainer.OnContentChangeListener {

    interface OnTextInputChangeListener {
        fun onTextInputChanged(text: List<String>)
    }

    lateinit var coroutineScope: CoroutineScope

    var editable: Boolean = true

    var onTextInputChangedListener: OnTextInputChangeListener? = null

    private val recycledViewQueue: Queue<DebounceTextContainer> = LinkedList()

    @Suppress("UNCHECKED_CAST")
    private val childrenList: List<EditText>
        get() = children.toList() as List<EditText>

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
                val container = getViewContainer()
                container.editText.setText(inputString)
                initializeListenersForContainer(container)
                addView(container.editText)
            }
            val container = getViewContainer()
            initializeListenersForContainer(container)
            addView(container.editText)
        }
    }

    private fun getViewContainer(): DebounceTextContainer {
        return recycledViewQueue.poll() ?: createViewContainer()
    }

    private fun initializeListenersForContainer(container: DebounceTextContainer) {
        container.onContentChangeListener = this
    }

    private fun createViewContainer(): DebounceTextContainer {
        val binding = ItemTagsTextInputBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        )
        return DebounceTextContainer(binding.root, coroutineScope)
    }

    private fun recycleViewContainer(container: DebounceTextContainer) {
        container.onContentChangeListener = null
        container.editText.textString = ""
        recycledViewQueue.add(container)
    }

    private fun getTextInputStrings(): List<String> {
        return childrenList
            .mapNotNull { view -> (view as? EditText)?.textString }
            .filter { input -> input.isNotEmpty() }
    }

    private fun notifyTextInputChanged() {
        if (onTextInputChangedListener != null) {
            val inputs = getTextInputStrings()
            onTextInputChangedListener?.onTextInputChanged(inputs)
        }
    }

    // DebounceTextContainer.OnTextChangeListener

    override fun onTextChange(container: DebounceTextContainer, text: String?) {
        notifyTextInputChanged()
        addLastTextInputIfNecessary()
    }

    override fun onFocusChange(container: DebounceTextContainer, isFocused: Boolean) {
        if (!isFocused) {
            val inputView = container.editText
            val childrenList = childrenList
            if (childrenList.size <= 1) {
                return
            }
            val viewIndex = childrenList.indexOf(inputView)
            if (viewIndex != childrenList.lastIndex && inputView.textString.isNullOrEmpty()) {
                removeView(inputView)
                recycleViewContainer(container)
            }
        }
    }

    private fun addLastTextInputIfNecessary() {
        childrenList.lastOrNull()?.let { inputView ->
            val text = inputView.text
            if (text != null && text.isNotEmpty()) {
                val newInputView = getViewContainer()
                initializeListenersForContainer(newInputView)
                addView(newInputView.editText)
            }
        } ?: run {
            val newInputView = getViewContainer()
            initializeListenersForContainer(newInputView)
            addView(newInputView.editText)
        }
    }
}