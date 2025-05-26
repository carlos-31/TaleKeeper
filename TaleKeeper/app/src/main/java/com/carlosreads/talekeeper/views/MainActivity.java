package com.carlosreads.talekeeper.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        if (sharedPref.getBoolean("darkMode", false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set toolbar as the ActionBar
        setSupportActionBar(binding.toolbar);

        // defining with the IDs of the destinations which are top-level
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.home_flow, R.id.discover_flow, R.id.profile_flow)
                .build();

        // navController handles the navigation between fragments
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


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
}