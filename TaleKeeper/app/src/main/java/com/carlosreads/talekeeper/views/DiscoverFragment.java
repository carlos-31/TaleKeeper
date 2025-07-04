package com.carlosreads.talekeeper.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
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

            //toggles visibility depending on if the search found any books or not
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
        genreButtons.put(binding.fictionBtn, "General Fiction");
        genreButtons.put(binding.nonFictionBtn, "Nonfiction");

        //iterates through the entries in the hashmap, gets the key (button)
        // and sets the content in the bundle to the string corresponding to that button
        for (Map.Entry<View, String> entry : genreButtons.entrySet()) {
            entry.getKey().setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(v);
                Bundle bundle = new Bundle();
                bundle.putString("content", entry.getValue());
                navController.navigate(R.id.action_discover_to_bookList_in_discover, bundle);
            });
        }

        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = Objects.requireNonNull(binding.search.getText()).toString();
                if (!searchQuery.isEmpty()) {
                    viewModel.search(searchQuery.trim());

                    Activity activity = getActivity();
                    if (activity != null) {
                        //gets the InputMethodManager system service which handles inputs like the keyboard
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        View view = activity.getCurrentFocus();

                        //hides the keyboard from the screen
                        if (view != null) {
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            // clears focus from the search bar
                            binding.search.clearFocus();
                        }
                    }
                } else {
                    binding.resutsRecyclerVIew.setVisibility(View.GONE);
                    binding.noResultsCard.setVisibility(View.GONE);
                }
            }
        });

        binding.bookRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireView());
                Bundle bundle = new Bundle();
                bundle.putString("action", "bookRequest");
                navController.navigate(R.id.action_discover_to_user_actions_in_discover, bundle);
            }
        });

        binding.spanishBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            Bundle bundle = new Bundle();
            bundle.putString("content", "es");
            navController.navigate(R.id.action_discover_to_bookList_in_discover, bundle);
        });

        binding.englishBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            Bundle bundle = new Bundle();
            bundle.putString("content", "en");
            navController.navigate(R.id.action_discover_to_bookList_in_discover, bundle);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Book book) {
        NavController navController = NavHostFragment.findNavController(this);
        Bundle bundle = new Bundle();
        bundle.putString("isbn13", book.getIsbn13());
        navController.navigate(R.id.action_discover_to_discover_bookDetail, bundle);
    }

    @Override
    public void onResume() {
        viewModel.checkUser();
        super.onResume();
    }
}