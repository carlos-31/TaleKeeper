package com.carlosreads.talekeeper.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.databinding.FragmentProfileBinding;
import com.carlosreads.talekeeper.viewmodels.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private boolean loggedInStatus;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //the menu provider handles the creation and actions
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.profile_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                NavController navController = Navigation.findNavController(requireView());
                Bundle bundle = new Bundle();
                bundle.putBoolean("userStatus", loggedInStatus);
                navController.navigate(R.id.action_profile_to_settings, bundle);
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED); //attaches the menu to the fragment properly

        setClickListeners();
        setObserverCounts();

        // shows the correct layout depending on user logged in status
        viewModel.getLoggedIn().observe(getViewLifecycleOwner(), loggedIn -> {
            if (loggedIn != null) {
                if (loggedIn) {
                    //toggles both to ensure it only shows what should be visible
                    loggedInStatus = true;
                    binding.LoggedInLayout.setVisibility(View.VISIBLE);
                    binding.notLoggedInLayout.setVisibility(View.GONE);
                } else {
                    loggedInStatus = false;
                    binding.LoggedInLayout.setVisibility(View.GONE);
                    binding.notLoggedInLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        return root;
    }

    private void setObserverCounts() {
        viewModel.getFavouritesCount().observe(getViewLifecycleOwner(), count -> {
            String countText;
            if (count != null) {
                if (count == 0)
                    //displays correct string for 0 books
                    countText = getString(R.string.no_favs);
                else
                    countText = getResources().getQuantityString(R.plurals.favourite_books, count, count);
                binding.favCount.setText(countText);
            }

        });
        viewModel.getReadCount().observe(getViewLifecycleOwner(), count -> {
            String countText;
            if (count != null) {
                if (count == 0)
                    countText = getString(R.string.no_read_books);
                else
                    countText = getResources().getQuantityString(R.plurals.read_books, count, count);
                binding.readCount.setText(countText);
            }
        });
        viewModel.getReadingCount().observe(getViewLifecycleOwner(), count -> {
            String countText;
            if (count != null) {
                if (count == 0)
                    countText = getString(R.string.no_reading_books);
                else
                    countText = getResources().getQuantityString(R.plurals.reading_books, count, count);
                binding.readingCount.setText(countText);
            }

        });
        viewModel.getTbrCount().observe(getViewLifecycleOwner(), count -> {
            String countText;
            if (count != null) {
                if (count == 0)
                    countText = getString(R.string.no_tbr);
                else
                    countText = getResources().getQuantityString(R.plurals.tbr_books, count, count);
                binding.tbrCount.setText(countText);
            }

        });
    }

    private void setClickListeners() {
        //sets up the listeners for all interactable items on screen
//        binding.settingsFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavController navController = Navigation.findNavController(requireView());
//                Bundle bundle = new Bundle();
//                bundle.putBoolean("userStatus", loggedInStatus);
//                navController.navigate(R.id.action_profile_to_settings, bundle);
//            }
//        });

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
                navController.navigate(R.id.action_profile_to_profile_booklist, bundle);
            }
        });

        binding.readCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireView());
                Bundle bundle = new Bundle();
                bundle.putString("listType", "Read");
                navController.navigate(R.id.action_profile_to_profile_booklist, bundle);
            }
        });

        binding.readingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireView());
                Bundle bundle = new Bundle();
                bundle.putString("listType", "Reading");
                navController.navigate(R.id.action_profile_to_profile_booklist, bundle);
            }
        });

        binding.tbrCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireView());
                Bundle bundle = new Bundle();
                bundle.putString("listType", "tbr");
                navController.navigate(R.id.action_profile_to_profile_booklist, bundle);
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

    @Override
    public void onResume() {
        super.onResume();
        viewModel.setUpFragment();
    }
}