package com.carlosreads.talekeeper.repositories;

import com.google.firebase.auth.FirebaseAuth;

public class UserRepositor {
    private final FirebaseAuth mAuth;


    public UserRepositor(FirebaseAuth firebaseAuth) {
        this.mAuth = FirebaseAuth.getInstance();


    }
}
