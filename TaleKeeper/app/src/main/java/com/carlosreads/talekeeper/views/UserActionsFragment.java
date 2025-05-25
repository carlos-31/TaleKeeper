package com.carlosreads.talekeeper.views;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.databinding.FragmentProfileBinding;
import com.carlosreads.talekeeper.databinding.FragmentUserActionsBinding;
import com.carlosreads.talekeeper.viewmodels.ProfileViewModel;
import com.carlosreads.talekeeper.viewmodels.UserActionsViewModel;
import com.google.android.material.tabs.TabLayout;

public class UserActionsFragment extends Fragment {
    private UserActionsViewModel viewModel;
    private FragmentUserActionsBinding binding;
    private String actionString;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(UserActionsViewModel.class);

        binding = FragmentUserActionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        Bundle args = getArguments();
        if (args != null)
            actionString = args.getString("action", "null");

        emptyUI();
        setUpUI();

        return root;
    }

    private void emptyUI() {
        binding.passwordChangeLayout.setVisibility(View.GONE);
    }

    private void setUpUI(){
        switch (actionString) {
            case "passwordChange":
                binding.passwordChangeLayout.setVisibility(View.VISIBLE);
                setUpListeners(actionString);
            case "accountDeletion":
            case "bookRequest":
            default:

        }

        viewModel.getToastMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null)
                    Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpListeners(String actionString) {
        switch (actionString) {
            case "passwordChange":
                binding.confirmPassBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.changePassword(binding.currentPassword.getText().toString().trim(),
                                                 binding.newPassword.getText().toString().trim(),
                                                 binding.confirmNewPassword.getText().toString().trim());
//                        if (binding.currentPassword.getText().toString().trim().isEmpty()
//                         || binding.newPassword.getText().toString().trim().isEmpty()
//                         || binding.confirmNewPassword.getText().toString().trim().isEmpty())
//                            Toast.makeText(requireContext(), "All fields must be filled in", Toast.LENGTH_SHORT).show();
//                        else
//                            viewModel.
                    }
                });
            case "accountDeletion":
            case "bookRequest":
            default:

        }
    }


}