package com.carlosreads.talekeeper.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

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
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            activity.getSupportActionBar().setTitle("A great book");
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
                String selectedStatus = parentView.getItemAtPosition(position).toString();

                // Only proceed if it's not the initial load
                if (!isInitialSpinnerLoad) {
                    if (position == parentView.getCount() - 1 || position == 0) {
                        binding.bookStatusSpinner.setSelection(0);
                        viewModel.updateBookStatus("remove");

                    } else
                        viewModel.updateBookStatus(selectedStatus);
                } else {
                    isInitialSpinnerLoad = false; // Reset the flag after the first selection
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
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

        viewModel.getBookStatus().observe(getViewLifecycleOwner(), listStatus -> {
            if (listStatus != null) {
                String[] bookStatusOptions = getResources().getStringArray(R.array.book_status_options);
                int spinnerPosition = -1;
                // Goes through the array of the options for the spinner
                for (int i = 0; i < bookStatusOptions.length; i++) {
                    if (bookStatusOptions[i].equalsIgnoreCase(listStatus)) {
                        //when it finds the option recieved from the ViewModel, save its position
                        spinnerPosition = i;
                        break;
                    }
                }
                if (spinnerPosition != -1) {
                    //sets the correct option in the spinner to match the status of the book
                    binding.bookStatusSpinner.setSelection(spinnerPosition);
                } else {
                    binding.bookStatusSpinner.setSelection(0); // Default to "Add Book"
                }
            }
        });
    }

}