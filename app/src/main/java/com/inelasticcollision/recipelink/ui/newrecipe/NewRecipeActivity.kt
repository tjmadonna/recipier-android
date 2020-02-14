package com.inelasticcollision.recipelink.ui.newrecipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.common.extensions.visible
import com.inelasticcollision.recipelink.di.NewRecipeComponentImpl
import com.inelasticcollision.recipelink.ui.newrecipe.adapter.NewRecipeAdapter
import com.inelasticcollision.recipelink.ui.newrecipe.state.NewRecipeState
import kotlinx.android.synthetic.main.activity_new_recipe.*
import kotlinx.android.synthetic.main.fragment_recipes.*

class NewRecipeActivity : AppCompatActivity() {

    private val component = NewRecipeComponentImpl(this, "https://google.com")

    private val viewModel by lazy {
        return@lazy component.viewModel
    }

    private lateinit var newRecipeAdapter: NewRecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_recipe)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is NewRecipeState.Loading -> handleLoadingState()
                is NewRecipeState.SetRecipe -> handleSetRecipeState(state)
                is NewRecipeState.Finish -> handleFinishState()
            }
        })
    }

    private fun setupRecyclerView() {
        newRecipeAdapter = NewRecipeAdapter { list ->
            val focusedView = rv_new_recipe.children.filter { view -> view.isFocused }
            val focusedIndex = focusedView.firstOrNull()?.let {
                recipes_recycler.indexOfChild(it)
            } ?: run { -1 }
            viewModel.setState(mapListToState(list))
        }

        val recyclerView = rv_new_recipe
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = newRecipeAdapter
    }

    private fun handleLoadingState() {
        toolbar_new_recipe.menu.findItem(R.id.action_save).isEnabled = false
        rv_new_recipe.visible = false
        pb_new_recipe.show()
    }

    private fun handleSetRecipeState(state: NewRecipeState.SetRecipe) {
        rv_new_recipe.visible = true
        pb_new_recipe.hide()

        Log.d("NewActivity", "===== handleSetRecipeState")
        newRecipeAdapter.submitState(mapStateToList(state))
    }

    private fun handleFinishState() {
        finish()
    }

    private fun mapStateToList(state: NewRecipeState.SetRecipe): List<String> {
        return mutableListOf<String>().apply {
            add(state.imageUrl)
            add(state.title)
            add(state.notes)
            addAll(state.tags)
            add("")
        }
    }

    private fun mapListToState(list: List<String>): NewRecipeState.SetRecipe {
        return NewRecipeState.SetRecipe(
                title = list.getOrNull(1) ?: "",
                imageUrl = list.getOrNull(0) ?: "",
                favorite = false,
                notes = list.getOrNull(2) ?: "",
                tags = list.subList(3, list.lastIndex + 1).filter { it.isNotEmpty() },
                animate = false
        )
    }
}
