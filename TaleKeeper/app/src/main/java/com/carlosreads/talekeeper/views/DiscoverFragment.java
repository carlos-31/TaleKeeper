package com.carlosreads.talekeeper.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.adapters.BookAdapter;
import com.carlosreads.talekeeper.databinding.FragmentDiscoverBinding;
import com.carlosreads.talekeeper.models.Book;
import com.carlosreads.talekeeper.viewmodels.DiscoverViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DiscoverFragment extends Fragment implements BookAdapter.OnItemClickListener {
    private DiscoverViewModel viewModel;
    private FragmentDiscoverBinding binding;
    private BookAdapter bookAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);

        binding = FragmentDiscoverBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bookAdapter = new BookAdapter(new ArrayList<>());
        bookAdapter.setOnItemClickListener(this);
        binding.resutsRecyclerVIew.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.resutsRecyclerVIew.setAdapter(bookAdapter);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        setClickListeners();
        handleSearchResults();

        return root;
    }

    private void handleSearchResults() {
        viewModel.getSearchLiveData().observe(getViewLifecycleOwner(), books -> {
            bookAdapter.setBooks(books);

            if (books.isEmpty()) {
                binding.resutsRecyclerVIew.setVisibility(View.GONE);
                binding.noResultsCard.setVisibility(View.VISIBLE);
            } else {
                binding.resutsRecyclerVIew.setVisibility(View.VISIBLE);
                binding.noResultsCard.setVisibility(View.GONE);
            }
        });
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


        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.search(Objects.requireNonNull(binding.search.getText()).toString().trim());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Book book) {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);
        Bundle bundle = new Bundle();
        bundle.putString("isbn13", book.getIsbn13());
        navController.navigate(R.id.bookDetail, bundle);
    }
}