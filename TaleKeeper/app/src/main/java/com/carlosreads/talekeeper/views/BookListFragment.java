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
import com.carlosreads.talekeeper.databinding.FragmentBookListBinding;
import com.carlosreads.talekeeper.models.Book;
import com.carlosreads.talekeeper.viewmodels.BookListViewModel;

import java.util.ArrayList;

public class BookListFragment extends Fragment implements BookAdapter.OnItemClickListener {
    private BookListViewModel viewModel;
    private FragmentBookListBinding binding;
    private BookAdapter bookAdapter;
    private String content;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(BookListViewModel.class);

        binding = FragmentBookListBinding.inflate(inflater, container, false);
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
                    if (args.getString("listType").equalsIgnoreCase("tbr"))
                        //ensure that it uses "tbr" for the node in the database, but displays "To be read"
                        activity.getSupportActionBar().setTitle("To be read");
                    else
                        activity.getSupportActionBar().setTitle(args.getString("listType"));
                }
            }
        }

        viewModel.getBooks().observe(getViewLifecycleOwner(), books -> {
            if (books.isEmpty())
                binding.emptyListCard.setVisibility(View.VISIBLE);
            else
                binding.emptyListCard.setVisibility(View.GONE);
            bookAdapter.setBooks(books);
        });

        return root;
    }

    @Override
    public void onItemClick(Book book) {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);
        Bundle bundle = new Bundle();
        bundle.putString("isbn13", book.getIsbn13());
        navController.navigate(R.id.action_bookList_in_discover_to_discover_bookDetail, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}