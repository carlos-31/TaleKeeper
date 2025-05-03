package com.carlosreads.talekeeper.views;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.databinding.FragmentBookDetailBinding;
import com.carlosreads.talekeeper.databinding.FragmentHomeBinding;
import com.carlosreads.talekeeper.models.Book;
import com.carlosreads.talekeeper.viewmodels.BookDetailViewModel;
import com.carlosreads.talekeeper.viewmodels.HomeViewModel;

public class BookDetailFragment extends Fragment {
    private FragmentBookDetailBinding binding;
    private BookDetailViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(BookDetailViewModel.class);

        binding = FragmentBookDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());



        return root;
    }

}