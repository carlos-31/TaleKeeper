package com.carlosreads.talekeeper.viewmodels;

import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.repositories.UserRepository;

public class SettingsViewModel extends ViewModel {
    private UserRepository userRepository;

    public SettingsViewModel() {
        userRepository = new UserRepository();
    }

}