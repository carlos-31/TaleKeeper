package com.carlosreads.talekeeper.views;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.carlosreads.talekeeper.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.carlosreads.talekeeper.databinding.ActivityMainBinding;


//public class MainActivity extends AppCompatActivity {
//
//    private ActivityMainBinding binding;
//    private NavController navController;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        setSupportActionBar(binding.toolbar);
//
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_discover, R.id.navigation_profile)
//                .build();
//
//        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//
//        // Custom BottomNavigationView Item Selection Handling
//        binding.navView.setOnItemSelectedListener(item -> {
//            NavOptions.Builder builder = new NavOptions.Builder()
//                    .setLaunchSingleTop(true);
//
//            if ((item.getOrder() & Menu.CATEGORY_SECONDARY) == 0) {
//                builder.setPopUpTo(navController.getGraph().getStartDestinationId(), false, false);
//            }
//        });
//
//        binding.navView.setOnItemReselectedListener(item -> {
//            NavOptions navOptions = new NavOptions.Builder()
//                    .setLaunchSingleTop(true)
//                    .setPopUpTo(item.getItemId(), false)
//                    .build();
//            navController.navigate(item.getItemId(), null, navOptions);
//        });
//
//        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//            Menu menu = binding.navView.getMenu();

//            for (int i = 0; i < menu.size(); i++) {
//                MenuItem menuItem = menu.getItem(i);
//                if (menuItem.getItemId() == destination.getId()) {
//                    menuItem.setChecked(true);
//                    break;
//                }
//            }
//        });
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        // Ensure AppBarConfiguration is passed to navigateUp
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_discover, R.id.navigation_profile)
//                .build();
//        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
//    }
//}


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set toolbar as the ActionBar
        setSupportActionBar(binding.toolbar);

        // defining with the IDs of the destinations which are top-level
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.home_flow, R.id.discover_flow, R.id.navigation_profile)
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
                // Start destination of home_flow is R.id.navigation_home
                if (currentFragmentId != R.id.navigation_home) {
                    navController.popBackStack(R.id.navigation_home, false);
                }
            } else if (reselectedMenuItemId == R.id.discover_flow) {
                // Start destination of discover_flow is R.id.navigation_discover
                if (currentFragmentId != R.id.navigation_discover) {
                    navController.popBackStack(R.id.navigation_discover, false);
                }
            } else if (reselectedMenuItemId == R.id.navigation_profile) {
                // Start destination of profile_flow is R.id.navigation_profile
                if (currentFragmentId != R.id.navigation_profile) {
                    navController.popBackStack(R.id.navigation_profile, false);
                }
            }
        });

    }
}


//public class MainActivity extends AppCompatActivity {
//
//    private ActivityMainBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // set toolbar as the ActionBar
//        setSupportActionBar(binding.toolbar);
//
//        // defining with the IDs of the destinations
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_discover, R.id.navigation_profile)
//                .build();
//
//        // navController handles the navigation between fragments
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);
//    }
//
//}