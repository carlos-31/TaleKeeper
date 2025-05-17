package com.carlosreads.talekeeper.repositories;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.carlosreads.talekeeper.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                                        Log.e(TAG, "user register failed: " + databaseTask.getException());
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
                        Log.d(TAG, "login: " + mAuth.getCurrentUser());
                    } else {
                        loginStatus.setValue(false);
                    }
                });
        return loginStatus;
    }


    public void checkLogin(MutableLiveData<Boolean> loggedIn, MutableLiveData<User> userLiveData) {
        //checks if theres a user logged in
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            loggedIn.setValue(true);
            getUserInfo(user, userLiveData);
        } else
            loggedIn.setValue(false);
    }

    private void getUserInfo(FirebaseUser user, MutableLiveData<User> userLiveData) {
        //gets the user's data from the table "user_info"
        usersInfoRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userData = snapshot.getValue(User.class);
                if (userData != null) {
                    userLiveData.setValue(userData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void logoutUser() {
        mAuth.signOut();
    }

    private String getCurrentUserID() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null)
            return user.getUid();
        else
            return null;
    }

    public void isBookFavourite(String isbn13, MutableLiveData<Boolean> isFavouriteLiveData) {
        String userId = getCurrentUserID();
        if (userId == null || isbn13 == null) {
            isFavouriteLiveData.setValue(null);
            return;
        }
        usersInfoRef.child(userId).child("lists").child("favourites")
                .child(isbn13).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        isFavouriteLiveData.setValue(snapshot.exists());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        isFavouriteLiveData.setValue(false);
                    }
                });

    }

    public void removeFromFavourites(String isbn, MutableLiveData<Boolean> resultLiveData) {
        String userId = getCurrentUserID();
        if (userId == null || isbn == null) {
            resultLiveData.setValue(null);
            return;
        }
        usersInfoRef.child(userId).child("lists").child("favourites").child(isbn)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        resultLiveData.setValue(task.isSuccessful());
                    }
                });
    }

    public void addToFavourites(String isbn, MutableLiveData<Boolean> resultLiveData) {
        String userId = getCurrentUserID();
        if (userId == null || isbn == null) {
            resultLiveData.setValue(null);
            return;
        }
        usersInfoRef.child(userId).child("lists").child("favourites").child(isbn)
                .setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        resultLiveData.setValue(task.isSuccessful());
                    }
                });
    }

    public void getBookListStatus(String isbn, MutableLiveData<String> listStatusLiveData){
        String userId = getCurrentUserID();
        if (userId == null || isbn == null) {
            listStatusLiveData.setValue("Add book"); //default to "Add book"
            return;
        }
        usersInfoRef.child(userId).child("lists").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot readList = snapshot.child("read");
                DataSnapshot readingList = snapshot.child("reading");
                DataSnapshot tbrList = snapshot.child("tbr");

                if (readList.hasChild(isbn))
                    listStatusLiveData.setValue("Read");
                else if (tbrList.hasChild(isbn))
                    listStatusLiveData.setValue("To be read");
                else if (readingList.hasChild(isbn))
                    listStatusLiveData.setValue("Reading");
                else
                    listStatusLiveData.setValue("Add book");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listStatusLiveData.setValue("Add book");
            }
        });
    }

    public void removeBookFromAllLists (String isbn){
        String userId = getCurrentUserID();
        if (userId == null || isbn == null) {
            return;
        }
        DatabaseReference usersLists = usersInfoRef.child(userId).child("lists");

        usersLists.child("read").child(isbn).removeValue();
        usersLists.child("reading").child(isbn).removeValue();
        usersLists.child("tbr").child(isbn).removeValue();
    }

    public void addBookToList(String isbn, String list, MutableLiveData<Boolean> resultLiveData){
        String userId = getCurrentUserID();
        if (userId == null || isbn == null) {
            resultLiveData.setValue(false);
            return;
        }
        usersInfoRef.child(userId).child("lists").child(list).child(isbn)
                .setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            resultLiveData.setValue(true);
                        else
                            resultLiveData.setValue(false);
                    }
                });
    }
}
