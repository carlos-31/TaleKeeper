package com.carlosreads.talekeeper.views;

import android.graphics.PaintFlagsDrawFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

        Bundle args = getArguments();
        if (args != null)
            loggedIn = args.getBoolean("userStatus", false);
        else
            loggedIn = false;

        if (loggedIn)
            binding.accountSettings.setVisibility(View.VISIBLE);
        else
            binding.accountSettings.setVisibility(View.GONE);

        return binding.getRoot();
    }

}