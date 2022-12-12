package com.inelasticcollision.recipelink.ui.activity.main

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.inelasticcollision.recipelink.R
import com.inelasticcollision.recipelink.ui.fragment.recipelist.RecipeListFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        private const val PREF_FIRST_LAUNCH = "first_launch"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
        checkFirstLaunch()
    }

    private fun checkFirstLaunch() {
        // Show the instruction fragment if it's the first time launching the app
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        if (sharedPref.getBoolean(PREF_FIRST_LAUNCH, true)) {
            lifecycleScope.launch {
                delay(1000)
                navigateToInstruction()
                sharedPref.edit {
                    putBoolean(PREF_FIRST_LAUNCH, false)
                }
            }
        }
    }

    private fun navigateToInstruction() {
        val navFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val direction =
            RecipeListFragmentDirections.actionRecipeListFragmentToLearnAddingRecipesFragment()
        navFragment.navController.navigate(direction)
    }
}