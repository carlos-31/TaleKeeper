package com.carlosreads.talekeeper.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.databinding.FragmentDiscoverBinding;
import com.carlosreads.talekeeper.viewmodels.DiscoverViewModel;

import java.util.HashMap;
import java.util.Map;

public class DiscoverFragment extends Fragment {
    private DiscoverViewModel viewModel;
    private FragmentDiscoverBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);

        binding = FragmentDiscoverBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), binding.search.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        setClickListeners();

        return root;
    }

    private void setClickListeners() {
        //creating a hashmap so i can just use a for loop to set the listeners to all the buttons
        Map<View, String> genreButtons = new HashMap<>();
        genreButtons.put(binding.fantasyBtn, "Fantasy");
        genreButtons.put(binding.scifiBtn, "Sci-Fi");
        genreButtons.put(binding.mysteryBtn, "Mystery");
        genreButtons.put(binding.horrorBtn, "Horror");
        genreButtons.put(binding.speculativeBtn, "Speculative");
        genreButtons.put(binding.anthologyBtn, "Anthology");

        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);

        //iterates through the entries in the hashmap, gets the key (button)
        // and sets the content in the bundle to the string corresponding to that button
        for (Map.Entry<View, String> entry : genreButtons.entrySet()) {
            entry.getKey().setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("content", entry.getValue());
                navController.navigate(R.id.action_to_book_list, bundle);
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}