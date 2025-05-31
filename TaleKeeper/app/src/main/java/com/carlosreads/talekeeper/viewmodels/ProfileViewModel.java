package com.carlosreads.talekeeper.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.models.User;
import com.carlosreads.talekeeper.repositories.BookRepository;
import com.carlosreads.talekeeper.repositories.UserRepository;

public class ProfileViewModel extends ViewModel {
    private UserRepository userRepository;
    private BookRepository bookRepository;
    private MutableLiveData<User> userLiveData;
    private MutableLiveData<Boolean> loggedIn;
    private MutableLiveData<Integer> favouritesCount = new MutableLiveData<>(0);
    private MutableLiveData<Integer> readCount = new MutableLiveData<>(0);
    private MutableLiveData<Integer> readingCount = new MutableLiveData<>(0);
    private MutableLiveData<Integer> tbrCount = new MutableLiveData<>(0);

    public ProfileViewModel() {
        userRepository = new UserRepository();
        bookRepository = new BookRepository();
        userLiveData = new MutableLiveData<>();
        loggedIn = new MutableLiveData<>();
        setUpFragment();
    }

    public MutableLiveData<Boolean> getLoggedIn() {
        return loggedIn;
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Integer> getFavouritesCount() {
        return favouritesCount;
    }

    public MutableLiveData<Integer> getReadCount() {
        return readCount;
    }

    public MutableLiveData<Integer> getReadingCount() {
        return readingCount;
    }

    public MutableLiveData<Integer> getTbrCount() {
        return tbrCount;
    }

    private void fetchListsCounts() {
        // gets the book count for each list
        userRepository.getFavouritesCount(favouritesCount);
        userRepository.getReadCount(readCount);
        userRepository.getReadingCount(readingCount);
        userRepository.getTbrCount(tbrCount);
    }

    private void checkLogin() {
        //checks if logged in, and if so, gets the users data
        userRepository.checkLogin(loggedIn, userLiveData);
    }

    public void logoutUser() {
        //logs out the user, and calls checkLogin so fragment shows correct layout
        userRepository.logoutUser();
        checkLogin();
    }

    public void setUpFragment() {
        checkLogin();
        fetchListsCounts();
    }
}