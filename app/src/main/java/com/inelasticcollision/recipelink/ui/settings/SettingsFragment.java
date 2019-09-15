/*
 * SettingsFragment.java
 * RecipeLink-SQLBrite
 *
 * Created by Tyler Madonna on 10/1/2017.
 * Copyright © 2017 Tyler Madonna. All rights reserved.
 */

package com.inelasticcollision.recipelink.ui.settings;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.inelasticcollision.recipelink.BuildConfig;
import com.inelasticcollision.recipelink.R;

public class SettingsFragment extends PreferenceFragment {

    private static final String EMAIL_ADDRESS = "developer@madonnaapps.com";

    private static final String LICENSES_URL = "https://madonnaapps.com/recipelink/licenses-android.html";

    private static final String PLAY_URL_DETAILS = "details?id=com.inelasticcollision.recipelink";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);

        // Set version preference summary to version name from gradle file
        String versionName = BuildConfig.VERSION_NAME;
        Preference versionPreferences = findPreference("version");
        versionPreferences.setSummary(versionName);

        // Set rate preference's on preference listener to open play store entry in order to rate
        // the app
        Preference ratePreference = findPreference("rate");
        ratePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // Open play store to rate the app
                sendRating();
                return false;
            }
        });

        // Set licenses preference's on preference listener to open the browser to view the open
        // source licenses
        Preference licensesPreference = findPreference("licenses");
        licensesPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // Open the open source licenses web page
                openWebpage(LICENSES_URL);
                return false;
            }
        });

        // Set context preference's on preference listener to open email app in order to send email
        // to developer
        Preference contactPreference = findPreference("contact_developer");
        contactPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // Open email app to send email to developer
                sendEmail();
                return false;
            }
        });

    }

    // Open the play store so user can rate the app
    private void sendRating() {

        // Create uri to open app entry in play store
        Uri uri = Uri.parse("market://" + PLAY_URL_DETAILS);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        // Account for the play store back stack. Add flags to intent so that back button goes back
        // to recipe link
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }

        // Try to open the play store entry. If entry can't be opened, open the play store
        // in browser
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            openWebpage("https://play.google.com/store/apps/" + PLAY_URL_DETAILS);
            Log.e("SettingsFragment", e.getMessage(), e);
        }

    }

    // Open the email app so user can contact developer
    private void sendEmail() {

        // Create email intent and set the recipient, subject, and body
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{EMAIL_ADDRESS});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.format_email_subject));
        intent.putExtra(Intent.EXTRA_TEXT , getEmailBody());

        // Try to open the email intent. If an app doesn't exist to handle intent, display an error.
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            String message = getString(R.string.prompt_error_no_email);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            Log.e("SettingsFragment", e.getMessage(), e);
        }

    }

    // Create the email body that automatically includes information such as app version, android
    // version, and security patch.
    private String getEmailBody() {

        String format = getString(R.string.format_email_app_info);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return String.format(format, BuildConfig.VERSION_NAME, Build.VERSION.RELEASE, Build.VERSION.SECURITY_PATCH);
        } else {
            return String.format(format, BuildConfig.VERSION_NAME, Build.VERSION.RELEASE);
        }

    }

    // Open the given web page using an intent
    private void openWebpage(String url) {
        Uri webUri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, webUri));
    }

}
