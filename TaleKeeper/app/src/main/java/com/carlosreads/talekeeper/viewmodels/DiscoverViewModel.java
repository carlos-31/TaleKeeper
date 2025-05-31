package com.carlosreads.talekeeper.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.models.Book;
import com.carlosreads.talekeeper.models.User;
import com.carlosreads.talekeeper.repositories.BookRepository;
import com.carlosreads.talekeeper.repositories.UserRepository;

import java.util.List;

public class DiscoverViewModel extends ViewModel {
    private BookRepository bookRepository;
    private MutableLiveData<List<Book>> searchLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loggedIn = new MutableLiveData<>();

    public DiscoverViewModel() {
        this.bookRepository = new BookRepository();
        checkUser();
    }

    public MutableLiveData<List<Book>> getSearchLiveData() {
        return searchLiveData;
    }

    public void search(String query) {
        bookRepository.searchBooks(query, searchLiveData);
    }

    public MutableLiveData<Boolean> getLoggedIn() {
        return loggedIn;
    }

    public void checkUser() {
        new UserRepository().checkLogin(loggedIn, new MutableLiveData<User>());
    }
}