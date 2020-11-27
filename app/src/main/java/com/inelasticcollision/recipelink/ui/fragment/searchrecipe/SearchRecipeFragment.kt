package com.inelasticcollision.recipelink.ui.fragment.searchrecipe

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.data.model.Recipe
import com.inelasticcollision.recipelink.databinding.FragmentSearchRecipeBinding
import com.inelasticcollision.recipelink.ui.widget.DebouncedEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchRecipeFragment : Fragment(R.layout.fragment_search_recipe) {

    // Properties

    private var binding: FragmentSearchRecipeBinding? = null

    private val viewModel: SearchRecipeViewModel by viewModels()

    private val adapter = SearchRecipeAdapter()

    // Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchRecipeBinding.bind(view)
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
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupViews() {
        binding?.recyclerView?.adapter = adapter
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        binding?.searchEditText?.onTextChangeListener =
            object : DebouncedEditText.OnTextChangeListener {
                override fun onTextChange(text: String?) {
                    viewModel.setIntent(SearchRecipeIntent.Search(text))
                }
            }
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchRecipeState.Data -> renderDataState(state.recipes)
                is SearchRecipeState.NoData -> renderNoDataState()
                is SearchRecipeState.Error -> renderErrorState()
            }
        }
    }

    private fun renderDataState(recipes: List<Recipe>) {
        adapter.submitRecipeList(recipes)
        binding?.toolbar?.setTitle(R.string.all)
        binding?.toolbar?.menu?.findItem(R.id.menu_filter_all)?.isChecked = true
        binding?.noRecipesImageView?.visibility = View.GONE
        binding?.noRecipesTextView?.visibility = View.GONE
    }

    private fun renderNoDataState() {
        adapter.submitRecipeList(emptyList())
        binding?.toolbar?.setTitle(R.string.all)
        binding?.toolbar?.menu?.findItem(R.id.menu_filter_all)?.isChecked = true
        binding?.noRecipesImageView?.visibility = View.VISIBLE
        binding?.noRecipesTextView?.visibility = View.VISIBLE
    }

    private fun renderErrorState() {
        adapter.submitRecipeList(emptyList())
    }
}
