package com.inelasticcollision.recipelink.ui.fragment.editrecipe

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.databinding.FragmentEditRecipeBinding
import com.inelasticcollision.recipelink.ui.fragment.imagepicker.ImagePickerFragment
import com.inelasticcollision.recipelink.ui.widget.DebouncedEditText
import com.inelasticcollision.recipelink.ui.widget.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditRecipeFragment : Fragment(R.layout.fragment_edit_recipe) {

    // Properties

    private var binding: FragmentEditRecipeBinding? = null

    private val viewModel: EditRecipeViewModel by viewModels()

    // Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditRecipeBinding.bind(view)
        setupToolbar()
        setupObservers()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    // Setup

    private fun setupToolbar() {
        binding?.toolbar?.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_favorite -> toggleFavorite()
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }

        binding?.toolbar?.setNavigationOnClickListener {
            close()
        }
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is EditRecipeState.Data -> renderDataState(state.viewData)
                is EditRecipeState.Error -> renderErrorState()
                is EditRecipeState.Saved -> close()
            }
        }

        // Get image url from image picker
        val savedStateHandle =
            findNavController().getBackStackEntry(R.id.editRecipeFragment).savedStateHandle

        savedStateHandle.getLiveData<String>(ImagePickerFragment.IMAGE_PICKER_SAVED_STATE)
            .observe(viewLifecycleOwner) { value ->
                savedStateHandle.remove<String>(ImagePickerFragment.IMAGE_PICKER_SAVED_STATE)
                if (value != null) {
                    viewModel.setIntent(EditRecipeIntent.ChangeImage(value))
                }
            }
    }

    // Render

    private fun renderDataState(viewData: EditRecipeViewData) {
        binding?.recipeImageView?.load(viewData.selectedImageUrl) {
            listener(
                onStart = {

                },
                onSuccess = { _, _ ->

                },
                onError = { _, error ->
                    Log.e("AddRecipe", "===== ${error.localizedMessage}", error)
                },
                onCancel = {

                }
            )
        }

        binding?.titleEditText?.textString = viewData.title
        binding?.titleEditText?.editable = false

        binding?.notesEditText?.textString = viewData.notes
        binding?.notesEditText?.editable = false

        binding?.tagsTextInputLayout?.setTextInput(viewData.tags ?: emptyList())
        binding?.tagsTextInputLayout?.editable = false

        if (viewData.favorite) {
            binding?.toolbar?.menu?.findItem(R.id.menu_favorite)?.setIcon(R.drawable.ic_favorite)
        } else {
            binding?.toolbar?.menu?.findItem(R.id.menu_favorite)
                ?.setIcon(R.drawable.ic_favorite_outline)
        }

        val imageUrls = viewData.imageUrls
        setupViewListeners(imageUrls)

        binding?.changeImageButton?.isVisible = imageUrls != null && imageUrls.size > 1
    }

    private fun renderErrorState() {

    }

    private fun setupViewListeners(imageUrls: List<String>?) {
        binding?.titleEditText?.onTextChangeListener =
            object : DebouncedEditText.OnTextChangeListener {
                override fun onTextChange(text: String?) {
                    viewModel.setIntent(EditRecipeIntent.ChangeTitle(text))
                }
            }

        binding?.notesEditText?.onTextChangeListener =
            object : DebouncedEditText.OnTextChangeListener {
                override fun onTextChange(text: String?) {
                    viewModel.setIntent(EditRecipeIntent.ChangeNotes(text))
                }
            }

        binding?.tagsTextInputLayout?.onTextInputChangedListener =
            object : TextInputLayout.OnTextInputChangeListener {
                override fun onTextInputChanged(text: List<String>) {
                    viewModel.setIntent(EditRecipeIntent.ChangeTags(text))
                }
            }

        binding?.saveFab?.setOnClickListener {
            if (binding?.titleEditText?.textString != null) {
                saveRecipe()
            } else {
                Toast.makeText(requireContext(), "Recipes have to have a title", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        if (imageUrls != null && imageUrls.size > 1) {
            binding?.changeImageButton?.setOnClickListener {
                navigateToChooseImage(imageUrls)
            }
        }
    }

    private fun saveRecipe() {
        viewModel.setIntent(EditRecipeIntent.SaveRecipe)
    }

    private fun close() {
        findNavController().popBackStack()
    }

    private fun navigateToChooseImage(imageUrls: List<String>) {
        val direction =
            EditRecipeFragmentDirections.actionEditRecipeFragmentToImagePickerFragment(imageUrls.toTypedArray())
        findNavController().navigate(direction)
    }

    private fun toggleFavorite() {
        viewModel.setIntent(EditRecipeIntent.ToggleFavorite)
    }
}