package com.carlosreads.talekeeper.views;

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

public class SettingsFragment extends Fragment {
    private SettingsViewModel mViewModel;
    private FragmentSettingsBinding binding;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());


        return binding.getRoot();
    }

}