package com.inelasticcollision.recipelink.ui.fragment.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.inelasticcollision.recipelink.BuildConfig
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.databinding.FragmentSettingsBinding
import com.inelasticcollision.recipelink.ui.widget.BottomOffsetDecoration

class SettingsParentFragment : Fragment(R.layout.fragment_settings) {

    // Properties

    private var binding: FragmentSettingsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        setupGestureNavInsets(binding)
        setupToolbar()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupGestureNavInsets(binding: FragmentSettingsBinding?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && binding != null) {
            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = insets.top
                }
                val fragment = binding.fragmentContainer.getFragment<PreferenceFragmentCompat>()
                val bottomOffsetDecoration = BottomOffsetDecoration(insets.bottom)
                fragment.listView.addItemDecoration(bottomOffsetDecoration)
                WindowInsetsCompat.CONSUMED
            }
        }
    }

    private fun setupToolbar() {
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        setupContactDeveloperPreference()
        setupAddingRecipesPreference()
        setupRatePreference()
        setupLicensesPreference()
        setupVersionPreference()
    }

    private fun setupContactDeveloperPreference() {
        // Set context preference's on preference listener to open email app in order to send email
        // to developer
        findPreference<Preference>("contact_developer")?.setOnPreferenceClickListener {
            // Open email app to send email to developer
            sendEmail()
            false
        }
    }

    private fun setupAddingRecipesPreference() {
        // Set adding recipes preference's on preference listener to open the learn adding recipes
        // fragment
        findPreference<Preference>("adding_recipes")?.setOnPreferenceClickListener {
            val direction =
                SettingsParentFragmentDirections.actionSettingsFragmentToLearnAddingRecipesFragment()
            findNavController().navigate(direction)
            true
        }
    }

    private fun setupRatePreference() {
        // Set rate preference's on preference listener to open play store entry in order to rate
        // the app
        findPreference<Preference>("rate")?.setOnPreferenceClickListener {
            val uri = Uri.parse(getString(R.string.rate_play_store_url))
            val intent = Intent(Intent.ACTION_VIEW, uri)

            // Account for the play store back stack. Add flags to intent so that back button goes back
            // to recipe link
            intent.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )

            startActivity(intent)
            true
        }
    }

    private fun setupLicensesPreference() {
        // Set licenses preference's on preference listener to open the browser to view the open
        // source licenses
        findPreference<Preference>("licenses")?.setOnPreferenceClickListener {
            val direction =
                SettingsParentFragmentDirections.actionSettingsFragmentToOpenSourceLicensesFragment()
            findNavController().navigate(direction)
            true
        }
    }

    private fun setupVersionPreference() {
        // Set version preference summary to version name from gradle file
        findPreference<Preference>("version")?.summary = BuildConfig.VERSION_NAME
    }

    private fun sendEmail() {
        // Open the email app so user can contact developer

        // Create email intent and set the recipient, subject, and body
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.contact_email_address)))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_email_subject))

        val emailBody = String.format(
            getString(R.string.contact_email_body),
            BuildConfig.VERSION_NAME,
            Build.VERSION.RELEASE
        )
        intent.putExtra(Intent.EXTRA_TEXT, emailBody)

        // Try to open the email intent. If an app doesn't exist to handle intent, display an error.
        startActivity(intent)
    }
}