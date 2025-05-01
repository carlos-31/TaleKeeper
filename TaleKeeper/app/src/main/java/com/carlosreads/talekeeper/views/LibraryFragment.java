package com.carlosreads.talekeeper.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.carlosreads.talekeeper.adapters.BookAdapter;
import com.carlosreads.talekeeper.databinding.FragmentLibraryBinding;
import com.carlosreads.talekeeper.viewmodels.LibraryViewModel;

import java.util.ArrayList;

public class LibraryFragment extends Fragment {
    private LibraryViewModel libraryViewModel;
    private FragmentLibraryBinding binding;
    private BookAdapter bookAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        libraryViewModel =
                new ViewModelProvider(this).get(LibraryViewModel.class);

        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bookAdapter = new BookAdapter(new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(bookAdapter);

        libraryViewModel.getBooks().observe(getViewLifecycleOwner(),
                                                books -> bookAdapter.setBooks(books));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}