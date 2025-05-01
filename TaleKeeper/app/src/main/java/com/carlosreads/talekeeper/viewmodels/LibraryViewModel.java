package com.carlosreads.talekeeper.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.models.Book;
import com.carlosreads.talekeeper.repositories.BookRepository;

import java.util.List;

public class LibraryViewModel extends ViewModel {
    private final BookRepository bookRepository;
    private final MutableLiveData<List<Book>> bookLiveData = new MutableLiveData<>();

    public LibraryViewModel() {
        bookRepository = new BookRepository();
        loadBooks();
    }

    public LiveData<List<Book>> getBooks() {
        return bookLiveData;
    }

    private void loadBooks() {
        bookRepository.getAllBooks(bookLiveData);
    }


}