package com.inelasticcollision.recipelink.ui.fragment.imagepicker

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.databinding.FragmentImagePickerBinding
import com.inelasticcollision.recipelink.ui.widget.BottomOffsetDecoration

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
        setupGestureNavInsets(binding)
        setupToolbar()
        setupViews()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    // Setup

    private fun setupGestureNavInsets(binding: FragmentImagePickerBinding?) {
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
        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding?.recyclerView?.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            binding?.recyclerView?.layoutManager = GridLayoutManager(requireContext(), 4)
        }

        binding?.recyclerView?.adapter = adapter

        adapter.submitImageUrls(args.imageUrls.toList())
    }
}