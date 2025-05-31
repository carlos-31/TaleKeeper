package com.carlosreads.talekeeper.viewmodels;

import android.content.Context;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.models.User;
import com.carlosreads.talekeeper.repositories.UserRepository;

public class SignupViewModel extends ViewModel {
    private UserRepository userRepository;
    private MutableLiveData<Integer> resultMessage = new MutableLiveData<>();

    public SignupViewModel() {
        userRepository = new UserRepository();
    }

    public MutableLiveData<Integer> getResultMessage() {
        return resultMessage;
    }

    public void validateAndRegister(Context context, String name, String email, String password, String password2) {
        resultMessage.setValue(null);

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            resultMessage.setValue(R.string.validation_invalid_email);
            return;
        }
        if (!password.equals(password2) || password.isEmpty()) {
            resultMessage.setValue(R.string.validation_passwords_no_match);
            return;
        }

        User user = new User(name, email);
        userRepository.registerUser(user, password, resultMessage);

    }
    
}
