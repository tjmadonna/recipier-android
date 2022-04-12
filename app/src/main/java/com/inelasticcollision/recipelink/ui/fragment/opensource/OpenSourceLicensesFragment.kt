package com.inelasticcollision.recipelink.ui.fragment.opensource

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.databinding.FragmentOpenSourceLicensesBinding
import com.inelasticcollision.recipelink.databinding.FragmentRecipeListBinding
import com.inelasticcollision.recipelink.ui.widget.BottomOffsetDecoration
import com.inelasticcollision.recipelink.util.capitalizeWords
import org.xmlpull.v1.XmlPullParser

class OpenSourceLicensesFragment : Fragment(R.layout.fragment_open_source_licenses) {

    // Properties

    private var binding: FragmentOpenSourceLicensesBinding? = null

    private val adapter = OpenSourceLicensesAdapter()

    // Lifecycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOpenSourceLicensesBinding.bind(view)
        setupGestureNavInsets(binding)
        setupToolbar()
        setupViews()
        setLicenses()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupGestureNavInsets(binding: FragmentOpenSourceLicensesBinding?) {
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
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        binding?.recyclerView?.adapter = adapter
    }

    private fun setLicenses() {
        val licenses = loadLicenses()
        adapter.submitOpenSourceLicense(licenses)
    }

    private fun loadLicenses(): List<OpenSourceLicense> {
        val licenses = mutableListOf<OpenSourceLicense>()

        val parser = resources.getXml(R.xml.open_source)
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG && parser.name == "entry") {
                val title = parser.getAttributeValue(0)
                    .replace("_", " ")
                    .capitalizeWords()
                parser.next()
                val value = parser.text
                    .trim()
                    .split("|")
                val type = value[0]
                val url = value[1]

                val license = OpenSourceLicense(title, type, url)
                licenses.add(license)
            }
        }
        return licenses
    }
}

data class OpenSourceLicense(
    val title: String,
    val type: String,
    val url: String
)