package com.inelasticcollision.recipelink.ui.fragment.recipedetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.data.model.Recipe
import com.inelasticcollision.recipelink.databinding.FragmentRecipeDetailBinding
import com.inelasticcollision.recipelink.ui.fragment.confirmdelete.ConfirmDeleteDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailFragment : Fragment(R.layout.fragment_recipe_detail) {

    // Properties

    private var binding: FragmentRecipeDetailBinding? = null

    private val viewModel: RecipeDetailViewModel by viewModels()

    private val args: RecipeDetailFragmentArgs by navArgs()

    // Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipeDetailBinding.bind(view)
        setupToolbar()
        setupObservers()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupToolbar() {
        binding?.toolbar?.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_favorite -> toggleFavorite()
                R.id.menu_edit -> navigateToEditRecipe()
                R.id.menu_delete -> navigateToConfirmDelete()
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }

        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RecipeDetailState.Data -> renderDataState(state.recipe)
                is RecipeDetailState.Error -> renderErrorState()
                is RecipeDetailState.Deleted -> navigateToRecipeList()
            }
        }

        // Confirm delete live data observer
        val savedStateHandle =
            findNavController().getBackStackEntry(R.id.recipeDetailFragment).savedStateHandle

        savedStateHandle.getLiveData<Boolean>(ConfirmDeleteDialogFragment.CONFIRM_DELETE_SAVED_STATE)
            .observe(viewLifecycleOwner) { value ->
                savedStateHandle.remove<Boolean>(ConfirmDeleteDialogFragment.CONFIRM_DELETE_SAVED_STATE)
                if (value) {
                    viewModel.setIntent(RecipeDetailIntent.DeleteRecipe)
                }
            }
    }

    private fun renderDataState(recipe: Recipe) {
        binding?.recipeImageView?.load(recipe.imageUrl) {
            crossfade(true)
        }

        binding?.titleTextView?.text = recipe.title
        binding?.hostTextView?.text = recipe.host
        binding?.notesTextView?.text = recipe.notes
        binding?.tagsTextViewContainer?.setTextList(recipe.tags)

        if (recipe.favorite) {
            binding?.toolbar?.menu?.findItem(R.id.menu_favorite)?.setIcon(R.drawable.ic_favorite)
        } else {
            binding?.toolbar?.menu?.findItem(R.id.menu_favorite)
                ?.setIcon(R.drawable.ic_favorite_outline)
        }

        binding?.openFab?.setOnClickListener {
            // Open browser
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(recipe.url)
            startActivity(intent)
        }
    }

    private fun renderErrorState() {

    }

    private fun navigateToConfirmDelete() {
        val direction =
            RecipeDetailFragmentDirections.actionRecipeDetailFragmentToConfirmDeleteDialogFragment()
        findNavController().navigate(direction)
    }

    private fun navigateToEditRecipe() {
        val direction =
            RecipeDetailFragmentDirections.actionRecipeDetailFragmentToEditRecipeFragment(args.recipeId)
        findNavController().navigate(direction)
    }

    private fun navigateToRecipeList() {
        findNavController().popBackStack()
    }

    private fun toggleFavorite() {
        viewModel.setIntent(RecipeDetailIntent.ToggleFavorite)
    }
}