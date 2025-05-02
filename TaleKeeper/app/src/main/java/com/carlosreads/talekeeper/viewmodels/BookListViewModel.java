package com.carlosreads.talekeeper.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.models.Book;
import com.carlosreads.talekeeper.repositories.BookRepository;

import java.util.List;

public class BookListViewModel extends ViewModel {
    private final BookRepository bookRepository;
    private final MutableLiveData<List<Book>> bookLiveData = new MutableLiveData<>();

    public BookListViewModel() {
        bookRepository = new BookRepository();
    }

    public LiveData<List<Book>> getBooks() {
        return bookLiveData;
    }

    public void loadBooks(String genre) {
        bookRepository.getBooksByGenre(bookLiveData, genre);
    }


}