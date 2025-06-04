package com.carlosreads.talekeeper.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.databinding.FragmentUserActionsBinding;
import com.carlosreads.talekeeper.viewmodels.UserActionsViewModel;

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
        binding.accountDeletionLayout.setVisibility(View.GONE);
        binding.bookRequestLayout.setVisibility(View.GONE);
    }

    private void setUpUI() {
        //sets up the fragment according to where the user came from (which btn)
        switch (actionString) {
            case "passwordChange":
                binding.passwordChangeLayout.setVisibility(View.VISIBLE);
                break;
            case "deleteAccount":
                binding.accountDeletionLayout.setVisibility(View.VISIBLE);
                break;
            case "bookRequest":
                binding.bookRequestLayout.setVisibility(View.VISIBLE);
                break;
            default:
                Toast.makeText(requireContext(), getString(R.string.something_went_wrong_please_try_again), Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.navigation_home);
        }

        setUpListeners(actionString);

        viewModel.getToastMessage().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer message) {
                if (message != null) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    if (message == R.string.delete_acc_success) {
                        NavController navController = Navigation.findNavController(requireView());
                        navController.navigate(R.id.navigation_profile);
                    } else if (message == R.string.change_pass_success) {
                        binding.currentPassword.setText("");
                        binding.newPassword.setText("");
                        binding.confirmNewPassword.setText("");

                    }
                }
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
                    }
                });
                break;
            case "deleteAccount":
                binding.confirmDeletionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.deleteAccount();
                    }
                });
                binding.cancelDeleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavController navController = Navigation.findNavController(requireView());
                        navController.navigateUp();
                    }
                });
                break;
            case "bookRequest":
                binding.requestBookBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.requestBook(binding.bookTitleInput.getText().toString().trim(),
                                binding.bookAuthorInput.getText().toString().trim(),
                                binding.bookIsbnInput.getText().toString().trim());
                    }
                });
        }
    }
}