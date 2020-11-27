package com.inelasticcollision.recipelink.ui.fragment.confirmdelete

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.inelasticcollision.recipelink.R

class ConfirmDeleteDialogFragment : DialogFragment() {

    companion object {
        const val CONFIRM_DELETE_SAVED_STATE = "confirmDeleteSavedState"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_recipe_question))
            .setMessage(getString(R.string.delete_recipe_confirm))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                confirmDelete()
                dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                dismiss()
            }
            .create()
    }

    private fun confirmDelete() {
        val handle = findNavController().previousBackStackEntry?.savedStateHandle
        handle?.set(CONFIRM_DELETE_SAVED_STATE, true)
    }
}