package com.carlosreads.talekeeper.views;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.adapters.BookAdapter;
import com.carlosreads.talekeeper.databinding.FragmentLibraryBinding;
import com.carlosreads.talekeeper.viewmodels.BookListViewModel;
import com.carlosreads.talekeeper.viewmodels.LibraryViewModel;

import java.util.ArrayList;

public class BookListFragment extends Fragment {
    private BookListViewModel viewModel;
    private FragmentLibraryBinding binding;
    private BookAdapter bookAdapter;
    private String content;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(BookListViewModel.class);

        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bookAdapter = new BookAdapter(new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(bookAdapter);

        //gets the content send through the bundle (the genre)
        Bundle args = getArguments();
        if (args != null) {
            content = args.getString("content", "null");
        }

        viewModel.loadBooks(content);
        viewModel.getBooks().observe(getViewLifecycleOwner(),
                books -> bookAdapter.setBooks(books));

        //removes the back arrow from the toolbar, and sets the title to the genre sent in the bundle
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            activity.getSupportActionBar().setTitle(content);
        }

        //manually handle back button press to ensure correct behavior
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        NavController navController = NavHostFragment
                                .findNavController(BookListFragment.this);
                        navController.navigate(R.id.navigation_discover);
                    }
                });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}