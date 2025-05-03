package com.carlosreads.talekeeper.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.models.Book;
import com.carlosreads.talekeeper.repositories.BookRepository;

public class BookDetailViewModel extends ViewModel {
    private BookRepository bookRepository;
    private MutableLiveData<Book> bookLiveData = new MutableLiveData<>();
    private MutableLiveData<String> bookStatus = new MutableLiveData<>();

    public BookDetailViewModel() {
        bookRepository = new BookRepository();
        bookStatus.setValue("Add Book");
    }

    public MutableLiveData<Book> getBookLiveData() {
        return bookLiveData;
    }

    public void loadBook(String isbn) {
        bookRepository.getBookByIsbn(isbn, bookLiveData);
    }

    public void updateBookStatus (String status){
        bookStatus.setValue(status);
    }

    public MutableLiveData<String> getBookStatus() {
        return bookStatus;
    }
}
