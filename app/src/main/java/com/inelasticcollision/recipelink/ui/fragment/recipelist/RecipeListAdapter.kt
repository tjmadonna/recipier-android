package com.inelasticcollision.recipelink.ui.fragment.recipelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.inelasticcollision.recipelink.data.model.Recipe
import com.inelasticcollision.recipelink.databinding.ItemRecipeListBinding

class RecipeListAdapter : RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    companion object {

        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Recipe> =
            object : DiffUtil.ItemCallback<Recipe>() {
                override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                    return oldItem.id == newItem.id &&
                            oldItem.title == newItem.title &&
                            oldItem.favorite == newItem.favorite &&
                            oldItem.host == newItem.host &&
                            oldItem.imageUrl == newItem.imageUrl
                }
            }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitRecipeList(recipeList: List<Recipe>?) {
        differ.submitList(recipeList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecipeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindRecipe(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // ViewHolder
    class ViewHolder(
        private val binding: ItemRecipeListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindRecipe(recipe: Recipe) {
            // Load image
            binding.recipeImageView.load(recipe.imageUrl) {
                crossfade(true)
            }

            // Set text and favorite indicator
            binding.titleTextView.text = recipe.title
            binding.hostTextView.text = recipe.host
            binding.favoriteIndicatorView.visibility =
                if (recipe.favorite) View.VISIBLE else View.GONE

            // Set click listener to go to details
            binding.clickContainer.setOnClickListener { view ->
                val direction =
                    RecipeListFragmentDirections.actionRecipeListFragmentToRecipeDetailFragment(
                        recipe.id
                    )
                view.findNavController().navigate(direction)
            }
        }
    }
}