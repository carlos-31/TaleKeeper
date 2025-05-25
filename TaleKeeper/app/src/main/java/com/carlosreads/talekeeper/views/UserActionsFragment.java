package com.carlosreads.talekeeper.views;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.databinding.FragmentProfileBinding;
import com.carlosreads.talekeeper.databinding.FragmentUserActionsBinding;
import com.carlosreads.talekeeper.viewmodels.ProfileViewModel;
import com.carlosreads.talekeeper.viewmodels.UserActionsViewModel;

public class UserActionsFragment extends Fragment {
    private UserActionsViewModel viewModel;
    private FragmentUserActionsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(UserActionsViewModel.class);

        binding = FragmentUserActionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        return root;
    }


}