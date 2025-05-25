package com.carlosreads.talekeeper.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.repositories.UserRepository;
import com.carlosreads.talekeeper.views.SettingsFragment;

public class SettingsViewModel extends ViewModel {
    private UserRepository userRepository;

    public SettingsViewModel(){
        userRepository = new UserRepository();

    }

    private void checkDarkMode() {

    }
}