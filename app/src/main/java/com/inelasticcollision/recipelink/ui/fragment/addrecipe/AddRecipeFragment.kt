package com.inelasticcollision.recipelink.ui.fragment.addrecipe

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.databinding.FragmentAddRecipeBinding
import com.inelasticcollision.recipelink.ui.fragment.imagepicker.ImagePickerFragment
import com.inelasticcollision.recipelink.ui.widget.DebouncedEditText
import com.inelasticcollision.recipelink.ui.widget.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRecipeFragment : Fragment(R.layout.fragment_add_recipe) {

    // Properties

    private var binding: FragmentAddRecipeBinding? = null

    private val viewModel: AddRecipeViewModel by activityViewModels()

    // Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddRecipeBinding.bind(view)
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
                is AddRecipeState.Loading -> renderLoadingState()
                is AddRecipeState.Data -> renderDataState(state.viewData)
                is AddRecipeState.Error -> renderErrorState()
                is AddRecipeState.Saved -> close()
            }
        }

        // Get image url from image picker
        val savedStateHandle =
            findNavController().getBackStackEntry(R.id.addRecipeFragment).savedStateHandle

        savedStateHandle.getLiveData<String>(ImagePickerFragment.IMAGE_PICKER_SAVED_STATE)
            .observe(viewLifecycleOwner) { value ->
                savedStateHandle.remove<String>(ImagePickerFragment.IMAGE_PICKER_SAVED_STATE)
                if (value != null) {
                    viewModel.setIntent(AddRecipeIntent.ChangeImage(value))
                }
            }
    }

    // Render

    private fun renderLoadingState() {
        binding?.toolbar?.menu?.findItem(R.id.menu_favorite)?.isVisible = false
        binding?.mainContent?.isVisible = false
        binding?.saveFab?.hide()
        binding?.mainProgressBar?.show()
    }

    private fun renderDataState(viewData: AddRecipeViewData) {
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
        binding?.toolbar?.menu?.findItem(R.id.menu_favorite)?.isVisible = true
        binding?.mainContent?.isVisible = true
        binding?.saveFab?.show()
        binding?.mainProgressBar?.hide()
    }

    private fun renderErrorState() {

    }

    private fun setupViewListeners(imageUrls: List<String>?) {
        binding?.titleEditText?.onTextChangeListener =
            object : DebouncedEditText.OnTextChangeListener {
                override fun onTextChange(text: String?) {
                    viewModel.setIntent(AddRecipeIntent.ChangeTitle(text))
                }
            }

        binding?.notesEditText?.onTextChangeListener =
            object : DebouncedEditText.OnTextChangeListener {
                override fun onTextChange(text: String?) {
                    viewModel.setIntent(AddRecipeIntent.ChangeNotes(text))
                }
            }

        binding?.tagsTextInputLayout?.onTextInputChangedListener =
            object : TextInputLayout.OnTextInputChangeListener {
                override fun onTextInputChanged(text: List<String>) {
                    viewModel.setIntent(AddRecipeIntent.ChangeTags(text))
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
        viewModel.setIntent(AddRecipeIntent.SaveRecipe)
    }

    private fun close() {
        activity?.finish()
    }

    private fun navigateToChooseImage(imageUrls: List<String>) {
        val direction =
            AddRecipeFragmentDirections.actionAddRecipeFragmentToImagePickerFragment2(imageUrls.toTypedArray())
        findNavController().navigate(direction)
    }

    private fun toggleFavorite() {
        viewModel.setIntent(AddRecipeIntent.ToggleFavorite)
    }
}