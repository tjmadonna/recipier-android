package com.inelasticcollision.recipelink.ui.fragment.learn

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.databinding.FragmentLearnAddingRecipesBinding

class LearnAddingRecipesFragment : Fragment(R.layout.fragment_learn_adding_recipes) {

    // Properties

    private var binding: FragmentLearnAddingRecipesBinding? = null

    // Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLearnAddingRecipesBinding.bind(view)
        setupViews()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupViews() {
        loadGif()
        binding?.finishButton?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadGif() {
        val imageLoader = ImageLoader.Builder(requireContext())
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
        binding?.gifImageView?.load("file:///android_asset/share-recipe.gif", imageLoader)
    }
}