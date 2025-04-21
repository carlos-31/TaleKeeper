package com.carlosreads.talekeeper.viewmodels;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.models.User;
import com.carlosreads.talekeeper.repositories.UserRepository;

public class SignupViewModel extends ViewModel {
    private UserRepository userRepository;
    private MutableLiveData<Boolean> isRegistered = new MutableLiveData<>();
    private MutableLiveData<String> validationMessage = new MutableLiveData<>();

    public SignupViewModel() {
        userRepository = new UserRepository();
    }

    public LiveData<String> getValidationMessage() {
        return validationMessage;
    }

    public LiveData<Boolean> getRegistrationStatus() {
        return isRegistered;
    }

    public void validateAndRegister(String name, String email, String password, String password2) {
        validationMessage.setValue(null);

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            validationMessage.setValue("The email is not valid");
            Log.d(TAG, "email not valid");
            return;
        }
        if (!password.equals(password2) || password.isEmpty()) {
            validationMessage.setValue("The passwords don't match");
            Log.d(TAG, "password not same");
            return;
        }

        User user = new User(name, email);
        userRepository.registerUser(user, password).observeForever(success -> {
            isRegistered.setValue(success);
        });



    }


}
