package com.carlosreads.talekeeper.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);

        String language = sharedPref.getString("language", "en");
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(language);
        AppCompatDelegate.setApplicationLocales(appLocale);

        if (sharedPref.getBoolean("darkMode", false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set toolbar as the ActionBar
        setSupportActionBar(binding.toolbar);

        // defining with the IDs of the destinations which are top-level
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.home_flow, R.id.discover_flow, R.id.profile_flow)
                .build();

        // navController handles the navigation between fragments
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        if (savedInstanceState == null) {
            handleIntent(getIntent());
        }

        binding.navView.setOnItemReselectedListener(item -> {
            int reselectedMenuItemId = item.getItemId();
            int currentFragmentId = navController.getCurrentDestination() != null ?
                    navController.getCurrentDestination().getId() : 0;

            if (reselectedMenuItemId == R.id.home_flow) {
                if (currentFragmentId != R.id.navigation_home) {
                    navController.popBackStack(R.id.navigation_home, false);
                }
            } else if (reselectedMenuItemId == R.id.discover_flow) {
                if (currentFragmentId != R.id.navigation_discover) {
                    navController.popBackStack(R.id.navigation_discover, false);
                }
            } else if (reselectedMenuItemId == R.id.profile_flow) {
                if (currentFragmentId != R.id.navigation_profile) {
                    navController.popBackStack(R.id.navigation_profile, false);
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        //uses the NavigationUI to navigate up in the apps navigation hierarchy
        //the superclass method works as a fallback
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Handle the new intent, e.g., to navigate to the profile tab
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        // Post the action to ensure the UI is ready. making sure with using the post method that
        // the activity and its ui is fully set up before taking the user to the profile so the app doesn't crash
        binding.getRoot().post(() -> {
            if (intent != null && intent.hasExtra("navigateTo")) {
                String destination = intent.getStringExtra("navigateTo");
                if ("profile".equalsIgnoreCase(destination)) {
                    binding.navView.setSelectedItemId(R.id.profile_flow);
                }
                intent.removeExtra("navigateTo");
            }
        });
    }
}