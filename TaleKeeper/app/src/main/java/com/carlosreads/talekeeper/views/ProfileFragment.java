package com.carlosreads.talekeeper.views;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.databinding.FragmentProfileBinding;
import com.carlosreads.talekeeper.viewmodels.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        setClickListeners();
        setObserverCounts();

        // shows the correct layout depending on user logged in status
        viewModel.getLoggedIn().observe(getViewLifecycleOwner(), loggedIn -> {
            if (loggedIn != null) {
                if (loggedIn) {
                    //toggles both to ensure it only shows what should be visible
                    binding.LoggedInLayout.setVisibility(View.VISIBLE);
                    binding.notLoggedInLayout.setVisibility(View.GONE);
                } else {
                    binding.LoggedInLayout.setVisibility(View.GONE);
                    binding.notLoggedInLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        return root;
    }

    private void setObserverCounts() {
        viewModel.getFavouritesCount().observe(getViewLifecycleOwner(), count -> {
            Log.d(TAG, "book count: " + count.toString());
            String countText = getResources().getQuantityString(R.plurals.favourite_books, count, count);
            binding.favCount.setText(countText);
        });
    }

    private void setClickListeners() {
        //sets the listeners for all the buttons on the screen
        binding.settingsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.logoutUser();
            }
        });

        binding.favouritesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireView());
                Bundle bundle = new Bundle();
                bundle.putString("listType", "Favourites");
                navController.navigate(R.id.action_profile_to_List, bundle);
            }
        });

        binding.readCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireView());
                Bundle bundle = new Bundle();
                bundle.putString("listType", "Read");
                navController.navigate(R.id.action_profile_to_List, bundle);
            }
        });

        binding.readingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireView());
                Bundle bundle = new Bundle();
                bundle.putString("listType", "Reading");
                navController.navigate(R.id.action_profile_to_List, bundle);
            }
        });

        binding.tbrCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireView());
                Bundle bundle = new Bundle();
                bundle.putString("listType", "tbr");
                navController.navigate(R.id.action_profile_to_List, bundle);
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(requireContext(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(requireContext(), SignupActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}