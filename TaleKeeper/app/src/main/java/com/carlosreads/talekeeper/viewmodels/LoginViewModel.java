package com.carlosreads.talekeeper.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.repositories.UserRepository;

public class LoginViewModel extends ViewModel {
    private UserRepository userRepository;
    private MutableLiveData<String> resutlMessage = new MutableLiveData<>();
//    private MutableLiveData<Boolean> loginStatus = new MutableLiveData<>();

    public LoginViewModel(){
        userRepository = new UserRepository();
    }

//    public MutableLiveData<Boolean> getLoginStatus(){
//        return loginStatus;
//    }


    public MutableLiveData<String> getResutlMessage() {
        return resutlMessage;
    }

    public void login(Context context, String email, String password) {
        userRepository.loginUser(context, email, password, resutlMessage);
    }

//    public void login(String email, String password){
//        userRepository.loginUser(email, password).observeForever(result -> {
//            //logs in user and stores if result was seuccesful or not
//            loginStatus.setValue(result);
//        });
//
//    }

}
