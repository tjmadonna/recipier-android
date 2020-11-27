package com.inelasticcollision.recipelink.ui.fragment.imagepicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.inelasticcollision.recipelink.databinding.ItemImagePickerBinding

class ImagePickerAdapter : RecyclerView.Adapter<ImagePickerAdapter.ViewHolder>() {

    private var imageUrls: List<String> = emptyList()

    fun submitImageUrls(imageUrls: List<String>) {
        this.imageUrls = imageUrls
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemImagePickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindRecipe(imageUrls[position])
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    // ViewHolder
    class ViewHolder(
        private val binding: ItemImagePickerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindRecipe(imageUrl: String?) {
            // Load image
            binding.imageView.load(imageUrl) {
                crossfade(true)
            }

            binding.imageView.setOnClickListener { view ->
                val handle = view.findNavController().previousBackStackEntry?.savedStateHandle
                handle?.set(ImagePickerFragment.IMAGE_PICKER_SAVED_STATE, imageUrl)
                view.findNavController().popBackStack()
            }
        }
    }
}