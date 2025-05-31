package com.carlosreads.talekeeper.viewmodels;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.models.User;
import com.carlosreads.talekeeper.repositories.UserRepository;

public class SignupViewModel extends ViewModel {
    private UserRepository userRepository;
//    private MutableLiveData<Boolean> isRegistered = new MutableLiveData<>();
    private MutableLiveData<String> resultMessage = new MutableLiveData<>();

    public SignupViewModel() {
        userRepository = new UserRepository();
    }

    public MutableLiveData<String> getResultMessage() {
        return resultMessage;
    }

    //    public LiveData<String> getValidationMessage() {
//        return validationMessage;
//    }
//
//    public LiveData<Boolean> getRegistrationStatus() {
//        return isRegistered;
//    }

    public void validateAndRegister(String name, String email, String password, String password2) {
        resultMessage.setValue(null);

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            resultMessage.setValue("The email is not valid");
            return;
        }
        if (!password.equals(password2) || password.isEmpty()) {
            resultMessage.setValue("The passwords don't match");
            return;
        }

        User user = new User(name, email);
        userRepository.registerUser(user, password, resultMessage);

    }
    
}
