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

import com.bumptech.glide.Glide;
import com.carlosreads.talekeeper.databinding.FragmentHomeBinding;
import com.carlosreads.talekeeper.models.Book;
import com.carlosreads.talekeeper.viewmodels.HomeViewModel;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private Book currentBook;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.setViewModel(homeViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        homeViewModel.getSpotlightData().observe(getViewLifecycleOwner(), new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                currentBook = book;

                //loading the cover of the book into the ImageView from the url in the database
                Glide.with(getContext())
                        .load(book.getCover_url())
                        .override(630, (int) (630 * 1.6))
                        .fitCenter()
                        .into(binding.coverImg);
            }
        });

        binding.libraryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "library", Toast.LENGTH_SHORT).show();
                // will send user to library fragment
            }
        });

        binding.spotlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "ISBN: " + currentBook.getIsbn13(), Toast.LENGTH_SHORT).show();
                //this sends you to the detail for the book, will be implemented later
            }
        });

        return root;
    }


}