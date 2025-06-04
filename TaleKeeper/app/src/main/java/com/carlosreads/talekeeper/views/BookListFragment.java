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
        viewModel = new ViewModelProvider(this).get(BookListViewModel.class);

        binding = FragmentBookListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bookAdapter = new BookAdapter(new ArrayList<>());
        bookAdapter.setOnItemClickListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(bookAdapter);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();

        Bundle args = getArguments();
        //gets the key of what will be shown
        //also handles getting the correct title to display on the fragment
        // depending on the data shown and the language the user chose
        if (args != null) {
            if (args.containsKey("content")) {
                content = args.getString("content", "null");
                if (content.equalsIgnoreCase("es")) {
                    viewModel.loadBooksByLanguage(content);
                    if (activity.getSupportActionBar() != null) {
                        activity.getSupportActionBar().setTitle(R.string.spanish);
                    }
                } else if (content.equalsIgnoreCase("en")) {
                    viewModel.loadBooksByLanguage(content);
                    if (activity.getSupportActionBar() != null) {
                        activity.getSupportActionBar().setTitle(R.string.english);
                    }
                } else {
                    viewModel.loadBooksByGenre(content);
                    if (activity.getSupportActionBar() != null) {
                        int genreTitle = 0;
                        switch (content.toLowerCase()) {
                            case "fantasy":
                                genreTitle = R.string.fantasy;
                                break;
                            case "sci-fi":
                                genreTitle = R.string.sci_fi;
                                break;
                            case "mystery":
                                genreTitle = R.string.mystery;
                                break;
                            case "horror":
                                genreTitle = R.string.horror;
                                break;
                            case "speculative":
                                genreTitle = R.string.speculative;
                                break;
                            case "anthology":
                                genreTitle = R.string.anthology;
                                break;
                            case "general fiction":
                                genreTitle = R.string.fiction;
                                break;
                            case "nonfiction":
                                genreTitle = R.string.nonfiction;
                                break;
                        }
                        if (genreTitle != 0) {
                            activity.getSupportActionBar().setTitle(genreTitle);
                        } else {
                            activity.getSupportActionBar().setTitle(""); //default to an empty label
                        }
                    }
                }
            } else if (args.containsKey("listType")) {
                String listType = args.getString("listType", "null").toLowerCase();
                viewModel.loadBooksByList(listType);

                if (activity.getSupportActionBar() != null) {
                    int listTitle = 0;
                    switch (listType) {
                        case "tbr":
                            listTitle = R.string.tbr_title;
                            break;
                        case "favourites":
                            listTitle = R.string.favourites;
                            break;
                        case "read":
                            listTitle = R.string.read;
                            break;
                        case "reading":
                            listTitle = R.string.reading;
                            break;
                    }

                    if (listTitle != 0) {
                        activity.getSupportActionBar().setTitle(listTitle);
                    } else {
                        activity.getSupportActionBar().setTitle("");
                    }
                }
            }
        }

        viewModel.getBooks().observe(getViewLifecycleOwner(), books -> {
            //toggles visibility if depending on weather theres books to show or not
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
        //sends the user to the detail with the isbn in the bundle
        navController.navigate(R.id.action_bookList_in_discover_to_discover_bookDetail, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}