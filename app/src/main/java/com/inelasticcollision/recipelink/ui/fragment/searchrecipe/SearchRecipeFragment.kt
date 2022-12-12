package com.inelasticcollision.recipelink.ui.fragment.searchrecipe

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.data.model.Recipe
import com.inelasticcollision.recipelink.databinding.FragmentSearchRecipeBinding
import com.inelasticcollision.recipelink.ui.widget.BottomOffsetDecoration
import com.inelasticcollision.recipelink.ui.widget.DebounceTextContainer
import com.inelasticcollision.recipelink.util.closeKeyboard
import com.inelasticcollision.recipelink.util.listener.KeyboardEventListener
import com.inelasticcollision.recipelink.util.openKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchRecipeFragment : Fragment(R.layout.fragment_search_recipe),
    DebounceTextContainer.OnContentChangeListener {

    // Properties

    private var binding: FragmentSearchRecipeBinding? = null

    private val viewModel: SearchRecipeViewModel by viewModels()

    private val adapter = SearchRecipeAdapter()

    private var searchTextContainer: DebounceTextContainer? = null

    // Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchRecipeBinding.bind(view)
        setupGestureNavInsets(binding)
        setupToolbar()
        setupViews()
        setupObservers()
    }

    override fun onPause() {
        super.onPause()
        closeKeyboard(binding?.searchEditText)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    // Setup

    private fun setupGestureNavInsets(binding: FragmentSearchRecipeBinding?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && binding != null) {
            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = insets.top
                    leftMargin = insets.left
                    rightMargin = insets.right
                }
                val bottomOffsetDecoration = BottomOffsetDecoration(insets.bottom)
                binding.recyclerView.addItemDecoration(bottomOffsetDecoration)
                WindowInsetsCompat.CONSUMED
            }
        }
    }

    private fun setupToolbar() {
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupViews() {
        binding?.recyclerView?.adapter = adapter
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        binding?.searchEditText?.let {
            searchTextContainer = DebounceTextContainer(it, lifecycleScope)
            searchTextContainer?.onContentChangeListener = this
        }
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchRecipeState.Data -> renderDataState(state.recipes, state.keyboardShowing)
                is SearchRecipeState.NoData -> renderNoDataState(state.keyboardShowing)
                is SearchRecipeState.Error -> renderErrorState()
            }
        }

        KeyboardEventListener(this) { isShowing ->
            if (isShowing) {
                binding?.searchEditText?.requestFocus()
            } else {
                binding?.searchEditText?.clearFocus()
            }
            viewModel.setIntent(SearchRecipeIntent.ChangeKeyboardShowing(isShowing))
        }
    }

    private fun renderDataState(recipes: List<Recipe>, keyboardShowing: Boolean) {
        adapter.submitRecipeList(recipes)
        binding?.noRecipesImageView?.visibility = View.GONE
        binding?.noRecipesTextView?.visibility = View.GONE
        setKeyboardShowing(keyboardShowing)
    }

    private fun renderNoDataState(keyboardShowing: Boolean) {
        adapter.submitRecipeList(emptyList())
        binding?.noRecipesImageView?.visibility = View.VISIBLE
        binding?.noRecipesTextView?.visibility = View.VISIBLE
        setKeyboardShowing(keyboardShowing)
    }

    private fun setKeyboardShowing(isShowing: Boolean) {
        if (isShowing) {
            openKeyboard(binding?.searchEditText)
        } else {
            closeKeyboard(binding?.searchEditText)
        }
    }

    private fun renderErrorState() {
        adapter.submitRecipeList(emptyList())
    }

    // DebounceTextContainer.OnContentChangeListener

    override fun onTextChange(container: DebounceTextContainer, text: String?) {
        viewModel.setIntent(SearchRecipeIntent.Search(text))
    }

    override fun onFocusChange(container: DebounceTextContainer, isFocused: Boolean) = Unit
}
