package com.inelasticcollision.recipelink.ui.recipelist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.common.extensions.visible
import com.inelasticcollision.recipelink.data.models.Recipe
import kotlinx.android.synthetic.main.list_item_recipe.view.*

class RecipeListAdapter(
        private val itemSelectionCallback: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Recipe>() {

        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.url == newItem.url &&
                    oldItem.imageUrl == newItem.imageUrl &&
                    oldItem.isFavorite == newItem.isFavorite
        }

    })

    fun submitRecipes(recipes: List<Recipe>) {
        differ.submitList(recipes)
    }

    // RecipeListAdapter methods

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_recipes, parent, false),
                itemSelectionCallback
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    // View Holder

    class ViewHolder(
            itemView: View,
            private val selectionCallback: (Recipe) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Recipe) = with(itemView) {
            setOnClickListener { selectionCallback(item) }

            Glide.with(itemView.context)
                    .load(item.imageUrl)
                    .centerCrop()
                    .dontAnimate()
                    .dontTransform()
                    .crossFade()
                    .into(itemView.iv_recipe_item_image)

            itemView.tv_recipe_item_title.text = item.title
            itemView.tv_recipe_item_host.text = item.url
            itemView.iv_recipe_item_favorite.visible = item.isFavorite
        }
    }
}