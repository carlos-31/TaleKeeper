package com.carlosreads.talekeeper.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.models.Book;
import com.carlosreads.talekeeper.repositories.BookRepository;

public class HomeViewModel extends ViewModel {
    private final BookRepository bookRepository;
    private final MutableLiveData<Book> spotlightLiveData = new MutableLiveData<>();

    public HomeViewModel() {
        bookRepository = new BookRepository();
    }

    public LiveData<Book> getSpotlightData() {
        //get the book's data from BookRepository
        bookRepository.loadSpotlight(spotlightLiveData);
        return spotlightLiveData;
    }

}

