package com.carlosreads.talekeeper.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.databinding.FragmentBookDetailBinding;
import com.carlosreads.talekeeper.models.Book;
import com.carlosreads.talekeeper.viewmodels.BookDetailViewModel;

public class BookDetailFragment extends Fragment {
    private FragmentBookDetailBinding binding;
    private BookDetailViewModel viewModel;
    private String bookIsbn;
    private boolean isInitialSpinnerLoad = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(BookDetailViewModel.class);

        binding = FragmentBookDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(""); // ensure title on toolbar remains empty
        }

        if (getArguments() != null) {
            //gets ibsn of the book sent through the bundle
            bookIsbn = getArguments().getString("isbn13");
            viewModel.loadBook(bookIsbn);
            observeViewModel();
        }

        setUpListeners();

        return root;
    }

    private void setUpListeners() {
        binding.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.toggleFavourite(bookIsbn);
            }
        });

        binding.bookStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Only proceed if it's not the initial load
                    //so loading the fragment doesn't make a call to the viewmodel & repo
                if (!isInitialSpinnerLoad) {
                    String[] bookStatusKeys = getResources().getStringArray(R.array.book_status_keys);
                    String selectedKey = bookStatusKeys[position];

                    //uses string from the keys array instead of the string from the options
                    viewModel.updateBookStatus(selectedKey);
                } else {
                    isInitialSpinnerLoad = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    private void observeViewModel() {
        //observe if the user is logged in, to show or hide the buttons for favourite and the spinner
        viewModel.getUserLoggedIn().observe(getViewLifecycleOwner(), user -> {
            if (user != null && user)
                binding.userBtnBox.setVisibility(View.VISIBLE);
            else
                binding.userBtnBox.setVisibility(View.GONE);
        });

        viewModel.getBookLiveData().observe(getViewLifecycleOwner(), new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                int width = 570;
                Glide.with(getContext())
                        .load(book.getCover_url())
                        .override(width, (int) (width * 1.6))
                        .fitCenter()
                        .into(binding.bookCover);
            }
        });

        viewModel.getIsFavouriteLiveData().observe(getViewLifecycleOwner(), isFav -> {
            //sets the correct icon for the book if it's in teh favourites list or not
            if (isFav != null) {
                if (isFav)
                    binding.favBtn.setImageResource(R.drawable.ic_fav);
                else
                    binding.favBtn.setImageResource(R.drawable.ic_fav_outline);
            }
        });

        viewModel.getBookStatus().observe(getViewLifecycleOwner(), listStatusKey -> {
            if (listStatusKey != null) {
                String[] bookStatusKeys = getResources().getStringArray(R.array.book_status_keys);
                int spinnerPosition = -1;

                //find the position of the key in our key array
                for (int i = 0; i < bookStatusKeys.length; i++) {
                    if (bookStatusKeys[i].equals(listStatusKey)) {
                        spinnerPosition = i;
                        break;
                    }
                }
                if (spinnerPosition != -1) {
                    binding.bookStatusSpinner.setSelection(spinnerPosition);
                } else {
                    //if the user click 'remove', make it show 'add boo' again instead
                    binding.bookStatusSpinner.setSelection(0);
                }
            }
        });
    }
}