package com.carlosreads.talekeeper.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.models.Book;
import com.carlosreads.talekeeper.repositories.BookRepository;
import com.carlosreads.talekeeper.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class BookListViewModel extends ViewModel {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final MutableLiveData<List<Book>> bookLiveData = new MutableLiveData<>();
    private LiveData<List<String>> currentIsbnLiveData;
    private Observer<List<String>> currentIsbnObserver;

    public BookListViewModel() {
        bookRepository = new BookRepository();
        userRepository = new UserRepository();
    }

    public LiveData<List<Book>> getBooks() {
        return bookLiveData;
    }

    public void loadBooksByGenre(String genre) {
        bookRepository.getBooksByGenre(bookLiveData, genre);
    }

    public void loadBooksByList(String listNode) {
        // make sure nothing else is active or built
        if (currentIsbnLiveData != null && currentIsbnObserver != null) {
            currentIsbnLiveData.removeObserver(currentIsbnObserver);
            currentIsbnObserver = null;
            currentIsbnLiveData = null;
        }

        //getting the list of isbn of the books in the list
        currentIsbnLiveData = userRepository.getIsbnList(listNode);

        //an observer to detect changes on the list of isbns
        currentIsbnObserver = new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> isbns) {
                if (isbns != null) {
                    //get the info for each book
                    fetchBookDetails(isbns);
                } else {
                    // if the list is empty, set an empty list
                    bookLiveData.setValue(new ArrayList<>());
                }
            }
        };
        //observe the list with the observer created above
        currentIsbnLiveData.observeForever(currentIsbnObserver);
    }

    private void fetchBookDetails(List<String> isbns) {
        List<Book> fetchedBooks = new ArrayList<>();
        // in order to keep track of the books
        final int totalBooks = isbns.size();
        final MutableLiveData<Integer> booksLoadedCount = new MutableLiveData<>(0);

        if (isbns.isEmpty()) {
            bookLiveData.setValue(new ArrayList<>());
            return;
        }

        //iterate through the isbns in the list
        for (String isbn : isbns) {
            bookRepository.getBookByIsbn(isbn, new MutableLiveData<Book>() {
                @Override
                public void setValue(@Nullable Book value) {
                    super.setValue(value);
                    if (value != null) {
                        //add the book to the list
                        fetchedBooks.add(value);
                    }
                    booksLoadedCount.setValue(booksLoadedCount.getValue() + 1);
                    if (booksLoadedCount.getValue() == totalBooks) {
                        //if that was the last book of the list, sets the list of books into booksLiveData
                        bookLiveData.setValue(fetchedBooks);
                    }
                }
            });
        }
    }

    @Override
    protected void onCleared() {
        //this is called right before the ViewModel is destroyed
        super.onCleared();
        if (currentIsbnLiveData != null && currentIsbnObserver != null) {
            //should remove observers to avoid memory leaks
            currentIsbnLiveData.removeObserver(currentIsbnObserver);
        }
    }
}