package com.carlosreads.talekeeper.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.repositories.UserRepository;

public class UserActionsViewModel extends ViewModel {
    private UserRepository userRepository;
    private MutableLiveData<Integer> toastMessage = new MutableLiveData<>();

    public UserActionsViewModel() {
        userRepository = new UserRepository();
    }

    public MutableLiveData<Integer> getToastMessage() {
        return toastMessage;
    }

    public void changePassword(String currentPass, String newPass1, String newPass2) {
        toastMessage.setValue(null);
        if (currentPass.isEmpty() || newPass1.isEmpty() || newPass2.isEmpty()) {
            //checks all fields were filled
            toastMessage.setValue(R.string.validation_missing_info);
        } else if (!newPass1.equals(newPass2)) {
            //checks the new passwords match
            toastMessage.setValue(R.string.validation_new_passwords_no_match);
        } else {
            //if both checks pass, it proceeds with the password change
            userRepository.changePassword(currentPass, newPass1, toastMessage);
        }
    }

    public void deleteAccount() {
        toastMessage.setValue(null);
        userRepository.deleteAccount(toastMessage);
    }

    public void requestBook(String title, String author, String isbn) {
        toastMessage.setValue(null);
        if (!title.isEmpty() && !author.isEmpty())
            //checks that mandatory data was filled
            userRepository.requestBook(title, author, isbn, toastMessage);
        else
            toastMessage.setValue(R.string.validation_required_fields);
    }
}