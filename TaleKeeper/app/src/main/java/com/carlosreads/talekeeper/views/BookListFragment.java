package com.carlosreads.talekeeper.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.adapters.BookAdapter;
import com.carlosreads.talekeeper.databinding.FragmentLibraryBinding;
import com.carlosreads.talekeeper.models.Book;
import com.carlosreads.talekeeper.viewmodels.BookListViewModel;

import java.util.ArrayList;

public class BookListFragment extends Fragment implements BookAdapter.OnItemClickListener {
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
        bookAdapter.setOnItemClickListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(bookAdapter);


        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        //removes the back arrow from the toolbar, and sets the title to the genre sent in the bundle
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        Bundle args = getArguments();
        //gets the key of what will be shown
        if (args != null) {
            if (args.containsKey("content")) {
                content = args.getString("content", "null");
                viewModel.loadBooksByGenre(content);
                if (activity.getSupportActionBar() != null) {
                    activity.getSupportActionBar().setTitle(content);
                }
            } else if (args.containsKey("listType")) {
                viewModel.loadBooksByList(args.getString("listType").toLowerCase());
                if (activity.getSupportActionBar() != null) {
                    activity.getSupportActionBar().setTitle(args.getString("listType"));
                }
            }
        }

        viewModel.getBooks().observe(getViewLifecycleOwner(),
                books -> bookAdapter.setBooks(books));


        //manually handle back button press to ensure correct behavior
//        requireActivity().getOnBackPressedDispatcher().addCallback(
//                getViewLifecycleOwner(),
//                new OnBackPressedCallback(true) {
//                    @Override
//                    public void handleOnBackPressed() {
//                        NavController navController = NavHostFragment
//                                .findNavController(BookListFragment.this);
//                        navController.navigate(R.id.navigation_discover);
//                    }
//                });

        return root;
    }

    @Override
    public void onItemClick(Book book) {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);
        Bundle bundle = new Bundle();
        bundle.putString("isbn13", book.getIsbn13());
        navController.navigate(R.id.bookDetail, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}