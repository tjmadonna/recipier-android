package com.inelasticcollision.recipelink.ui.recipelist

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.common.extensions.visible
import com.inelasticcollision.recipelink.ui.common.filtertype.RecipeFilterType
import com.inelasticcollision.recipelink.ui.recipelist.adapter.RecipeListAdapter
import com.inelasticcollision.recipelink.ui.recipelist.state.RecipeListState
import kotlinx.android.synthetic.main.fragment_recipe_list.*

class RecipeListFragment : Fragment(R.layout.fragment_recipe_list) {

    private lateinit var viewModel: RecipeListViewModel

    private lateinit var recipeListAdapter: RecipeListAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recipeListAdapter = RecipeListAdapter { recipe ->

        }

        val recyclerView = rv_recipe_list
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = recipeListAdapter
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is RecipeListState.Data -> handleDateState(state)
                is RecipeListState.EmptyData -> toolbar_recipe_list.title = getString(R.string.title_recipes_favorite)
            }
        })
    }

    private fun handleDateState(state: RecipeListState.Data) {
        setTitle(state.filterType)
        recipeListAdapter.submitRecipes(state.recipes)
        v_empty_recipe_list.visible = false
    }

    private fun handleNoDateState(state: RecipeListState.EmptyData) {
        setTitle(state.filterType)
        recipeListAdapter.submitRecipes(emptyList())
        v_empty_recipe_list.visible = true
    }

    private fun setTitle(filterType: RecipeFilterType) = when (filterType) {
        is RecipeFilterType.All -> toolbar_recipe_list.title = getString(R.string.title_recipes_all)
        is RecipeFilterType.Favorite -> toolbar_recipe_list.title = getString(R.string.title_recipes_favorite)
        is RecipeFilterType.Collection -> toolbar_recipe_list.title = filterType.collectionName
    }
}