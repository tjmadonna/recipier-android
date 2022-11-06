package com.inelasticcollision.recipelink.ui.fragment.editrecipe

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.databinding.FragmentEditRecipeBinding
import com.inelasticcollision.recipelink.ui.fragment.imagepicker.ImagePickerFragment
import com.inelasticcollision.recipelink.ui.widget.DebounceTextContainer
import com.inelasticcollision.recipelink.ui.widget.TextInputLayout
import com.inelasticcollision.recipelink.util.observeOnce
import com.inelasticcollision.recipelink.util.textString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditRecipeFragment : Fragment(R.layout.fragment_edit_recipe),
    DebounceTextContainer.OnContentChangeListener,
    TextInputLayout.OnTextInputChangeListener {

    // Properties

    private var binding: FragmentEditRecipeBinding? = null

    private val viewModel: EditRecipeViewModel by viewModels()

    private var titleContainer: DebounceTextContainer? = null

    private var notesContainer: DebounceTextContainer? = null

    // Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditRecipeBinding.bind(view)
        setupGestureNavInsets(binding)
        setupToolbar()
        setupViews()
        setupObservers()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    // Setup

    private fun setupGestureNavInsets(binding: FragmentEditRecipeBinding?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && binding != null) {
            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = insets.top
                    leftMargin = insets.left
                    rightMargin = insets.right
                }
                binding.saveFab.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin =
                        resources.getDimensionPixelSize(R.dimen.space_medium) + insets.bottom
                }
                binding.bottomMarginSpace.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    height = insets.bottom
                }

                WindowInsetsCompat.CONSUMED
            }
        }
    }

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

        viewModel.titleState.observeOnce(viewLifecycleOwner) { title ->
            binding?.titleEditText?.textString = title
        }

        viewModel.favoriteState.observe(viewLifecycleOwner) { favorite ->
            if (favorite) {
                binding?.toolbar?.menu?.findItem(R.id.menu_favorite)
                    ?.setIcon(R.drawable.ic_favorite)
            } else {
                binding?.toolbar?.menu?.findItem(R.id.menu_favorite)
                    ?.setIcon(R.drawable.ic_favorite_outline)
            }
        }

        viewModel.selectedImageState.observe(viewLifecycleOwner) { imageUrl ->
            binding?.recipeImageView?.load(imageUrl) {
                listener(
                    onStart = { },
                    onSuccess = { _, _ -> },
                    onError = { _, errorResult ->
                        val error = errorResult.throwable
                        Log.e("AddRecipe", error.localizedMessage, error)
                    },
                    onCancel = { }
                )
            }
        }

        viewModel.imagesState.observe(viewLifecycleOwner) { imageUrls ->
            if (!imageUrls.isNullOrEmpty()) {
                binding?.changeImageButton?.isVisible = true
                binding?.changeImageButton?.setOnClickListener {
                    navigateToChooseImage(imageUrls)
                }
            } else {
                binding?.changeImageButton?.isVisible = false
                binding?.changeImageButton?.setOnClickListener(null)
            }
        }

        viewModel.notesState.observeOnce(viewLifecycleOwner) { notes ->
            binding?.notesEditText?.textString = notes
        }

        viewModel.tagsState.observeOnce(viewLifecycleOwner) { tags ->
            binding?.tagsTextInputLayout?.setTextInput(tags)
        }

        viewModel.savedStatusState.observe(viewLifecycleOwner) {
            close()
        }

        viewModel.errorState.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "A problem occurred", Toast.LENGTH_SHORT).show()
        }

        // Get image url from image picker
        val savedStateHandle =
            findNavController().getBackStackEntry(R.id.editRecipeFragment).savedStateHandle
        savedStateHandle.getLiveData<String>(ImagePickerFragment.IMAGE_PICKER_SAVED_STATE)
            .observe(viewLifecycleOwner) { value ->
                savedStateHandle.remove<String>(ImagePickerFragment.IMAGE_PICKER_SAVED_STATE)
                viewModel.setSelectedImage(value)
            }
    }

    private fun saveRecipe() {
        viewModel.saveRecipe()
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
        viewModel.toggleFavorite()
    }

    // DebounceTextContainer.OnContentChangeListener

    override fun onTextChange(container: DebounceTextContainer, text: String?) {
        if (container.editText == binding?.titleEditText) {
            viewModel.setTitle(text)
        } else if (container.editText == binding?.notesEditText) {
            viewModel.setNotes(text)
        }
    }

    override fun onFocusChange(container: DebounceTextContainer, isFocused: Boolean) = Unit

    // TextInputLayout.OnTextInputChangeListener

    override fun onTextInputChanged(text: List<String>) {
        viewModel.setTags(text)
    }
}