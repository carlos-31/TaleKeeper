package com.carlosreads.talekeeper.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.databinding.FragmentSettingsBinding;
import com.carlosreads.talekeeper.viewmodels.SettingsViewModel;

import java.nio.BufferUnderflowException;

public class SettingsFragment extends Fragment {
    private SettingsViewModel mViewModel;
    private FragmentSettingsBinding binding;
    private boolean loggedIn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        setUpUI();

        return binding.getRoot();
    }

    private void setUpUI() {
        Bundle args = getArguments();
        if (args != null)
            loggedIn = args.getBoolean("userStatus", false);
        else
            loggedIn = false;

        if (loggedIn)
            binding.accountSettings.setVisibility(View.VISIBLE);
        else
            binding.accountSettings.setVisibility(View.GONE);

        SharedPreferences prefs = requireActivity().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("darkMode", false);
        binding.themeSwitch.setChecked(isDarkMode);

        setUpListeners(prefs);
    }

    private void setUpListeners(SharedPreferences prefs) {
        binding.themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                prefs.edit().putBoolean("darkMode", isChecked).apply();
            }
        });

        binding.changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireView());
                Bundle bundle = new Bundle();
                bundle.putString("action", "passwordChange");
                navController.navigate(R.id.action_settings_to_user_actions_in_profile, bundle);
            }
        });

        binding.deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireView());
                Bundle bundle = new Bundle();
                bundle.putString("action", "deleteAccount");
                navController.navigate(R.id.action_settings_to_user_actions_in_profile, bundle);
            }
        });
    }


}