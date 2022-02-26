package com.inelasticcollision.recipelink.ui.fragment.addrecipe

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.databinding.FragmentAddRecipeBinding
import com.inelasticcollision.recipelink.ui.fragment.imagepicker.ImagePickerFragment
import com.inelasticcollision.recipelink.ui.widget.DebounceTextContainer
import com.inelasticcollision.recipelink.ui.widget.TextInputLayout
import com.inelasticcollision.recipelink.util.textString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRecipeFragment : Fragment(R.layout.fragment_add_recipe),
    DebounceTextContainer.OnContentChangeListener,
    TextInputLayout.OnTextInputChangeListener {

    // Properties

    private var binding: FragmentAddRecipeBinding? = null

    private val viewModel: AddRecipeViewModel by activityViewModels()

    private var titleContainer: DebounceTextContainer? = null

    private var notesContainer: DebounceTextContainer? = null

    // Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddRecipeBinding.bind(view)
        setupToolbar()
        setupViews()
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

    private fun setupViews() {
        binding?.titleEditText?.let {
            titleContainer = DebounceTextContainer(it, lifecycleScope)
            titleContainer?.onContentChangeListener = this
        }

        binding?.notesEditText?.let {
            notesContainer = DebounceTextContainer(it, lifecycleScope)
            notesContainer?.onContentChangeListener = this
        }

        binding?.tagsTextInputLayout?.coroutineScope = lifecycleScope
        binding?.tagsTextInputLayout?.onTextInputChangedListener = this

        binding?.saveFab?.setOnClickListener {
            if (binding?.titleEditText?.textString != null) {
                saveRecipe()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Recipes have to have a title", Toast.LENGTH_SHORT
                )
                    .show()
            }
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

        binding?.notesEditText?.textString = viewData.notes

        binding?.tagsTextInputLayout?.setTextInput(viewData.tags ?: emptyList())
        binding?.tagsTextInputLayout?.editable = false

        if (viewData.favorite) {
            binding?.toolbar?.menu?.findItem(R.id.menu_favorite)
                ?.setIcon(R.drawable.ic_favorite)
        } else {
            binding?.toolbar?.menu?.findItem(R.id.menu_favorite)
                ?.setIcon(R.drawable.ic_favorite_outline)
        }

        val imageUrls = viewData.imageUrls
        setupImageButtonListener(imageUrls)

        binding?.changeImageButton?.isVisible = !imageUrls.isNullOrEmpty()
        binding?.toolbar?.menu?.findItem(R.id.menu_favorite)?.isVisible = true
        binding?.mainContent?.isVisible = true
        binding?.saveFab?.show()
        binding?.mainProgressBar?.hide()
    }

    private fun renderErrorState() {

    }

    private fun setupImageButtonListener(imageUrls: List<String>?) {
        if (!imageUrls.isNullOrEmpty()) {
            binding?.changeImageButton?.setOnClickListener {
                navigateToChooseImage(imageUrls)
            }
        } else {
            binding?.changeImageButton?.setOnClickListener(null)
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

    // DebounceTextContainer.OnContentChangeListener

    override fun onTextChange(container: DebounceTextContainer, text: String?) {
        if (container.editText == binding?.titleEditText) {
            viewModel.setIntent(AddRecipeIntent.ChangeTitle(text))
        } else if (container.editText == binding?.notesEditText) {
            viewModel.setIntent(AddRecipeIntent.ChangeNotes(text))
        }
    }

    override fun onFocusChange(container: DebounceTextContainer, isFocused: Boolean) = Unit

    // TextInputLayout.OnTextInputChangeListener

    override fun onTextInputChanged(text: List<String>) {
        viewModel.setIntent(AddRecipeIntent.ChangeTags(text))
    }
}