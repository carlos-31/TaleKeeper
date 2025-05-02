package com.carlosreads.talekeeper.repositories;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.carlosreads.talekeeper.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UserRepository {
    private final FirebaseAuth mAuth;
    private DatabaseReference usersInfoRef;
    private MutableLiveData<Boolean> loginStatus = new MutableLiveData<>();


    public UserRepository() {
        this.mAuth = FirebaseAuth.getInstance();
        this.usersInfoRef = FirebaseDatabase.getInstance().getReference("user_info");

    }

    public LiveData<Boolean> registerUser(User user, String password) {
        MutableLiveData<Boolean> registrationStatus = new MutableLiveData<>();

        // create the new user
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", user.getName());
                        userMap.put("email", user.getEmail());

                        // if successful, saves the user's info into the table "user_info"
                        usersInfoRef.child(userId)
                                    .setValue(userMap)
                                    .addOnCompleteListener(databaseTask -> {
                                        if (databaseTask.isSuccessful()) {
                                            Log.d(TAG, "user registered");
                                            registrationStatus.setValue(true);
                                        } else {
                                            Log.e(TAG, "user register failed: " + databaseTask.getException() );
                                            registrationStatus.setValue(false);
                                        }
                                    });
                    } else {
                        Log.e(TAG, "error registering user: " + task.getException());
                        registrationStatus.setValue(false);
                    }
                });
        return registrationStatus;
    }


    public LiveData<Boolean> loginUser(String email, String password) {
        loginStatus.setValue(null);

        //logs in the user with their info
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //if login successful sets value true to handle log in
                        loginStatus.setValue(true);
                        Log.d(TAG,"login: " + mAuth.getCurrentUser());
                    } else {
                        loginStatus.setValue(false);
                    }
                });
        return loginStatus;
    }


    public void checkLogin(MutableLiveData<Boolean> loggedIn) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null)
            loggedIn.setValue(true);
        else
            loggedIn.setValue(false);
    }
}
