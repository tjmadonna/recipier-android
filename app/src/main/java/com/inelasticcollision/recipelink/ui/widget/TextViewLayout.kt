package com.inelasticcollision.recipelink.ui.widget

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.databinding.ItemTagsTextViewBinding

class TextViewLayout : LinearLayout {

    @Suppress("UNCHECKED_CAST")
    private val childrenList: List<TextView>
        get() = children.toList() as List<TextView>

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
            attrs, R.styleable.TextViewLayout, defStyle, 0
        )

        a.recycle()

        orientation = VERTICAL
        layoutTransition = LayoutTransition()
    }

    fun setTextList(textList: List<String>?) {
        if (textList.isNullOrEmpty()) {
            updateViewForEmptyList()
        } else {
            updateViewForTextList(textList)
        }
    }

    private fun getView(index: Int): TextView {
        return (getChildAt(index) as? TextView) ?: run {
            val binding = ItemTagsTextViewBinding.inflate(LayoutInflater.from(context), this, false)
            addView(binding.root, index)
            binding.root
        }
    }

    private fun updateViewForEmptyList() {
        val firstView = getView(0)
        firstView.text = null

        if (childCount > 1) {
            removeViews(1, childCount - 1)
        }
    }

    private fun updateViewForTextList(textList: List<String>) {
        textList.mapIndexed { index, text ->
            getView(index).text = text
        }

        if (childCount > textList.size) {
            removeViews(textList.size, childCount - textList.size)
        }
    }
}