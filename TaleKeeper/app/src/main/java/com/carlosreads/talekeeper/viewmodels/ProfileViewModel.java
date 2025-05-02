package com.carlosreads.talekeeper.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.models.Book;
import com.carlosreads.talekeeper.models.User;
import com.carlosreads.talekeeper.repositories.BookRepository;
import com.carlosreads.talekeeper.repositories.UserRepository;

public class ProfileViewModel extends ViewModel {
    private UserRepository userRepository;
    private BookRepository bookRepository;
    private MutableLiveData<User> userLiveData;
    private MutableLiveData<Boolean> loggedIn;

    public ProfileViewModel(){
        userRepository = new UserRepository();
        bookRepository = new BookRepository();
        userLiveData = new MutableLiveData<>();
        loggedIn = new MutableLiveData<>();
        checkLogin();
    }

    private void checkLogin() {
        userRepository.checkLogin(loggedIn, userLiveData);
    }

    public MutableLiveData<Boolean> getLoggedIn() {
        return loggedIn;
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }
}