package com.carlosreads.talekeeper.views;

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
import com.carlosreads.talekeeper.databinding.FragmentLibraryBinding;
import com.carlosreads.talekeeper.models.Book;
import com.carlosreads.talekeeper.viewmodels.LibraryViewModel;

import java.util.ArrayList;

public class LibraryFragment extends Fragment implements BookAdapter.OnItemClickListener {
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
        //this sets the fragment as the click listener for the adapter
        bookAdapter.setOnItemClickListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(bookAdapter);

        libraryViewModel.getBooks().observe(getViewLifecycleOwner(),
                books -> bookAdapter.setBooks(books));

        return root;
    }

    //implements the onItemClick from BookAdapter.OnItemClickListener
    @Override
    public void onItemClick(Book book) {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);
        Bundle bundle = new Bundle();
        bundle.putString("isbn13", book.getIsbn13());
        navController.navigate(R.id.action_library_to_home_bookDetail, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}