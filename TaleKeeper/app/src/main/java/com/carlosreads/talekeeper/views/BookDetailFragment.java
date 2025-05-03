package com.carlosreads.talekeeper.views;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.AdapterView;
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

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            activity.getSupportActionBar().setTitle("A great book");
        }

        if (getArguments() != null) {
            String isbn = getArguments().getString("isbn13");
            viewModel.loadBook(isbn);
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
        }

        binding.bookStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedStatus = parentView.getItemAtPosition(position).toString();

                if (position == parentView.getCount() - 1 || position == 0) {
                    binding.bookStatusSpinner.setSelection(0);

                } else
                    viewModel.updateBookStatus(selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        binding.favBtn.setImageResource(R.drawable.ic_home_black_24dp);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });


        return root;
    }

}