package com.inelasticcollision.recipelink.ui.fragment.imagepicker

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.databinding.FragmentImagePickerBinding

class ImagePickerFragment : Fragment(R.layout.fragment_image_picker) {

    companion object {
        const val IMAGE_PICKER_SAVED_STATE = "imagePickerSavedState"
    }

    // Properties

    private var binding: FragmentImagePickerBinding? = null

    private val args: ImagePickerFragmentArgs by navArgs()

    private val adapter = ImagePickerAdapter()

    // Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentImagePickerBinding.bind(view)
        setupToolbar()
        setupViews()
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
        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding?.recyclerView?.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            binding?.recyclerView?.layoutManager = GridLayoutManager(requireContext(), 4)
        }

        binding?.recyclerView?.adapter = adapter

        adapter.submitImageUrls(args.imageUrls.toList())
    }
}