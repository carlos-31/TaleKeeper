package com.carlosreads.talekeeper.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.repositories.UserRepository;

public class LoginViewModel extends ViewModel {
    private UserRepository userRepository;
    private MutableLiveData<Integer> resutlMessage = new MutableLiveData<>();

    public LoginViewModel() {
        userRepository = new UserRepository();
    }

    public MutableLiveData<Integer> getResutlMessage() {
        return resutlMessage;
    }

    public void login(String email, String password) {
        userRepository.loginUser(email, password, resutlMessage);
    }

}
