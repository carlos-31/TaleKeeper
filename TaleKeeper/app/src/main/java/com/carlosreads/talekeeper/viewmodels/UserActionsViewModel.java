package com.carlosreads.talekeeper.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.repositories.UserRepository;

public class UserActionsViewModel extends ViewModel {
    private UserRepository userRepository;
    private MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public UserActionsViewModel() {
        userRepository = new UserRepository();

    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }

    public void changePassword(String currentPass, String newPass1, String newPass2) {
        toastMessage.setValue(null);
        if (currentPass.isEmpty() || newPass1.isEmpty() || newPass2.isEmpty()) {
            toastMessage.setValue("Some information is missing");
        } else if (!newPass1.equals(newPass2)) {
            toastMessage.setValue("New passwords don't match");
        } else {
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
            userRepository.requestBook(title, author, isbn, toastMessage);
        else
            toastMessage.setValue("Please fill out the required fields");
    }
}