package com.inelasticcollision.recipelink.ui.newrecipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.common.extensions.visible
import kotlinx.android.synthetic.main.list_item_new_recipe_content.view.*

class NewRecipeAdapter(
        private val listChangeCallback: (List<String>) -> Unit
) : RecyclerView.Adapter<NewRecipeAdapter.NewRecipeViewHolder>() {

    private var currentState: List<String> = emptyList()

    fun submitState(state: List<String>) {
        if (currentState.isNotEmpty()) {
            val oldState = currentState
            currentState = state
            val diffs = DiffUtil.calculateDiff(DiffCallback(oldState, state))
            diffs.dispatchUpdatesTo(object : ListUpdateCallback {
                override fun onChanged(position: Int, count: Int, payload: Any?) = Unit

                override fun onMoved(fromPosition: Int, toPosition: Int) = notifyItemMoved(fromPosition, toPosition)

                override fun onInserted(position: Int, count: Int) = notifyItemInserted(position)

                override fun onRemoved(position: Int, count: Int) {
                    if (position == 3)
                        notifyItemChanged(4)
                    notifyItemRemoved(position)
                }
            })
        } else {
            currentState = state
            notifyDataSetChanged()
        }
    }

    // NewRecipeAdapter methods

    override fun getItemCount(): Int = currentState.size

    override fun getItemViewType(position: Int): Int = if (position == 0) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewRecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> NewRecipeViewHolder.ImageViewHolder(
                    inflater.inflate(R.layout.list_item_new_recipe_image, parent, false)
            )
            else -> NewRecipeViewHolder.TextViewHolder(
                    inflater.inflate(R.layout.list_item_new_recipe_content, parent, false)
            ) { text, index ->
                textChangeListener(text, index)
            }
        }
    }

    private fun textChangeListener(text: String?, index: Int) {
        if (currentState[index] == text)
            return

        val newList = currentState.toMutableList()
        newList[index] = text ?: ""
        currentState = newList
        listChangeCallback(newList)
    }

    override fun onBindViewHolder(holder: NewRecipeViewHolder, position: Int) {
        holder.bind(currentState[position])
    }

    class DiffCallback(
            private val oldState: List<String>,
            private val newState: List<String>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldState[oldItemPosition] == newState[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldState[oldItemPosition] == newState[newItemPosition]

        override fun getOldListSize(): Int = oldState.size

        override fun getNewListSize(): Int = newState.size

    }

    // View Holder

    sealed class NewRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun bind(text: String?)

        class ImageViewHolder(itemView: View) : NewRecipeViewHolder(itemView) {

            override fun bind(text: String?) {

            }
        }

        class TextViewHolder(
                itemView: View,
                private val textChangeCallback: (String?, Int) -> Unit
        ) : NewRecipeViewHolder(itemView) {

            override fun bind(text: String?) = with(itemView) {
                when (adapterPosition) {
                    1 -> iv_new_recipe_icon.setImageResource(R.drawable.ic_title_black_24dp)
                    2 -> iv_new_recipe_icon.setImageResource(R.drawable.ic_short_text_black_24dp)
                    3 -> iv_new_recipe_icon.setImageResource(R.drawable.ic_collections_black_24dp)
                }
                iv_new_recipe_icon.visible = adapterPosition < 4
                et_new_recipe_text.textChangeCallback = null
                et_new_recipe_text.textString = text
                et_new_recipe_text.textChangeCallback = { text ->
                    textChangeCallback.invoke(text, adapterPosition)
                }
            }
        }
    }
}