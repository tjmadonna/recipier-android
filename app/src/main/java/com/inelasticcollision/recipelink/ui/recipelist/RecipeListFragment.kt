package com.inelasticcollision.recipelink.ui.recipelist

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.common.extensions.visible
import com.inelasticcollision.recipelink.data.models.Recipe
import com.inelasticcollision.recipelink.ui.common.filtertype.RecipeFilterType
import com.inelasticcollision.recipelink.ui.recipelist.adapter.RecipeListAdapter
import kotlinx.android.synthetic.main.fragment_recipe_list.*

class RecipeListFragment : Fragment(R.layout.fragment_recipe_list) {

    private lateinit var viewModel: RecipeListViewModel

    private lateinit var recipeListAdapter: RecipeListAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
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
        viewModel.filterType.observe(viewLifecycleOwner, Observer { filterType ->
            when (filterType) {
                is RecipeFilterType.All -> toolbar_recipe_list.title = getString(R.string.title_recipes_all)
                is RecipeFilterType.Favorite -> toolbar_recipe_list.title = getString(R.string.title_recipes_favorite)
                is RecipeFilterType.Collection -> toolbar_recipe_list.title = filterType.collectionName
            }
        })

        viewModel.recipes.observe(viewLifecycleOwner, Observer { recipes ->
            recipeListAdapter.submitRecipes(recipes)
            v_empty_recipe_list.visible = recipes.isEmpty()
        })
    }

    private fun handleRecipeListItemClick(recipe: Recipe) {

    }

}