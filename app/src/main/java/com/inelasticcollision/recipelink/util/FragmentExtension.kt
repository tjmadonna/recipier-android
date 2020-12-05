package com.inelasticcollision.recipelink.util

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import kotlin.math.roundToInt

fun Fragment.isKeyboardOpen(): Boolean {
    val visibleBounds = Rect()
    requireView().getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = requireView().height - visibleBounds.height()
    val marginOfError = requireContext().convertDpToPx(50F).roundToInt()
    return heightDiff > marginOfError
}

fun Fragment.isKeyboardClosed(): Boolean {
    return !this.isKeyboardOpen()
}

fun Fragment.openKeyboard(view: View?) {
    view?.requestFocus()
    if (isKeyboardClosed()) {
        val inputManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputManager?.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }
}

fun Fragment.closeKeyboard(view: View?) {
    view?.clearFocus()
    if (isKeyboardOpen() && view != null) {
        val inputManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}