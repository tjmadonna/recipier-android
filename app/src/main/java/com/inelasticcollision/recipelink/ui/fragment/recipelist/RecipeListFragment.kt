package com.inelasticcollision.recipelink.ui.fragment.recipelist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.data.model.Recipe
import com.inelasticcollision.recipelink.databinding.FragmentRecipeListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : Fragment(R.layout.fragment_recipe_list) {

    // Properties

    private var binding: FragmentRecipeListBinding? = null

    private val viewModel: RecipeListViewModel by viewModels()

    private val adapter = RecipeListAdapter()

    // Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipeListBinding.bind(view)
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
                R.id.menu_search -> navigateToSearchRecipe()
                R.id.menu_filter_all -> filterRecipes(RecipeFilterType.All)
                R.id.menu_filter_favorites -> filterRecipes(RecipeFilterType.Favorites)
                R.id.menu_settings -> navigateToSettings()
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun setupViews() {
        binding?.recyclerView?.adapter = adapter
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RecipeListState.AllData -> renderAllDataState(state.recipes)
                is RecipeListState.FavoritesData -> renderFavoriteDataState(state.recipes)
                is RecipeListState.AllNoData -> renderAllNoDataState()
                is RecipeListState.FavoritesNoDate -> renderFavoriteNoDataState()
                is RecipeListState.Error -> renderErrorState()
            }
        }
    }

    private fun renderAllDataState(recipes: List<Recipe>) {
        adapter.submitRecipeList(recipes)
        binding?.toolbar?.setTitle(R.string.all)
        binding?.toolbar?.menu?.findItem(R.id.menu_filter_all)?.isChecked = true
        binding?.noRecipesImageView?.visibility = View.GONE
        binding?.noRecipesTextView?.visibility = View.GONE
    }

    private fun renderFavoriteDataState(recipes: List<Recipe>) {
        adapter.submitRecipeList(recipes)
        binding?.toolbar?.setTitle(R.string.favorites)
        binding?.toolbar?.menu?.findItem(R.id.menu_filter_favorites)?.isChecked = true
        binding?.noRecipesImageView?.visibility = View.GONE
        binding?.noRecipesTextView?.visibility = View.GONE
    }

    private fun renderAllNoDataState() {
        adapter.submitRecipeList(emptyList())
        binding?.toolbar?.setTitle(R.string.all)
        binding?.toolbar?.menu?.findItem(R.id.menu_filter_all)?.isChecked = true
        binding?.noRecipesImageView?.visibility = View.VISIBLE
        binding?.noRecipesTextView?.visibility = View.VISIBLE
    }

    private fun renderFavoriteNoDataState() {
        adapter.submitRecipeList(emptyList())
        binding?.toolbar?.setTitle(R.string.favorites)
        binding?.toolbar?.menu?.findItem(R.id.menu_filter_favorites)?.isChecked = true
        binding?.noRecipesImageView?.visibility = View.VISIBLE
        binding?.noRecipesTextView?.visibility = View.VISIBLE
    }

    private fun renderErrorState() {
        adapter.submitRecipeList(emptyList())
    }

    private fun navigateToSearchRecipe() {
        val direction =
            RecipeListFragmentDirections.actionRecipeListFragmentToSearchRecipeFragment()
        findNavController().navigate(direction)
    }

    private fun navigateToSettings() {
        val direction =
            RecipeListFragmentDirections.actionRecipeListFragmentToSettingsFragment()
        findNavController().navigate(direction)
    }

    private fun filterRecipes(filterType: RecipeFilterType) {
        viewModel.setIntent(RecipeListIntent.SetFilter(filterType))
    }
}