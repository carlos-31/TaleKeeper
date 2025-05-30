package com.carlosreads.talekeeper.repositories;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.carlosreads.talekeeper.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UserRepository {
    private final FirebaseAuth mAuth;
    private DatabaseReference usersInfoRef;
    private MutableLiveData<Boolean> loginStatus = new MutableLiveData<>();


    public UserRepository() {
        this.mAuth = FirebaseAuth.getInstance();
        this.usersInfoRef = FirebaseDatabase.getInstance().getReference("user_info");

    }

    public void loginUser(String email, String password, MutableLiveData<String> messageLiveData) {
        messageLiveData.setValue(null);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        messageLiveData.setValue("Login successful!");
                    } else {
                        String errorMessage = "Login failed. Please try again later.";
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            errorMessage = "Incorrect password. Please try again.";
                        messageLiveData.setValue(errorMessage);
                    }
                });
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
        // checks if theres an user logged in, and returns that user if there is one
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
        // checks if the book is marked as a favourite
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
        // removes the book from favourites
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
        //adds the book to the user's favourites list
        usersInfoRef.child(userId).child("lists").child("favourites").child(isbn)
                .setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        resultLiveData.setValue(task.isSuccessful());
                    }
                });
    }

    public void getBookListStatus(String isbn, MutableLiveData<String> listStatusLiveData) {
        String userId = getCurrentUserID();
        if (userId == null || isbn == null) {
            listStatusLiveData.setValue("Add book"); //default to "Add book"
            return;
        }
        //checks which list the book is in, if any
        usersInfoRef.child(userId).child("lists").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //gets all the data from each list
                DataSnapshot readList = snapshot.child("read");
                DataSnapshot readingList = snapshot.child("reading");
                DataSnapshot tbrList = snapshot.child("tbr");

                //checks each list for the book, if its found it sets the status to that list
                if (readList.hasChild(isbn))
                    listStatusLiveData.setValue("Read");
                else if (tbrList.hasChild(isbn))
                    listStatusLiveData.setValue("To be read");
                else if (readingList.hasChild(isbn))
                    listStatusLiveData.setValue("Reading");
                else
                    // if it isn't found, defaults to "Add book"
                    listStatusLiveData.setValue("Add book");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listStatusLiveData.setValue("Add book"); //defaults to "Add book"
            }
        });
    }

    public void removeBookFromAllLists(String isbn) {
        String userId = getCurrentUserID();
        if (userId == null || isbn == null) {
            return;
        }
        DatabaseReference usersLists = usersInfoRef.child(userId).child("lists");

        //deletes said book from all lists
        usersLists.child("read").child(isbn).removeValue();
        usersLists.child("reading").child(isbn).removeValue();
        usersLists.child("tbr").child(isbn).removeValue();
    }

    public void addBookToList(String isbn, String list) {
        String userId = getCurrentUserID();
        if (userId == null || isbn == null) {
            return;
        }
        // adds the book into the list sent
        usersInfoRef.child(userId).child("lists").child(list).child(isbn)
                .setValue(true);
    }

    public void getFavouritesCount(MutableLiveData<Integer> favouritesCount) {
        String userId = getCurrentUserID();
        if (userId == null) {
            return;
        }
        usersInfoRef.child(userId).child("lists").child("favourites")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //gets the book count for the list, saving it in the mutableLiveData
                        favouritesCount.setValue((int) snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        favouritesCount.setValue(null);
                    }
                });
    }

    public LiveData<List<String>> getIsbnList(String listNode) {
        MutableLiveData<List<String>> isbnLiveData = new MutableLiveData<>();
        String userId = getCurrentUserID();
        if (userId == null) {
            isbnLiveData.setValue(new ArrayList<>());
            return isbnLiveData;
        }
        //gets the books stored in the list sent by listNode
        usersInfoRef.child(userId).child("lists").child(listNode)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> isbns = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            String isbn = child.getKey();
                            if (isbn != null) {
                                //adds the key (isbn) to the list
                                isbns.add(isbn);
                            }
                        }
                        isbnLiveData.setValue(isbns);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //sets an empty list in case of an error
                        isbnLiveData.setValue(new ArrayList<>());
                    }
                });
        return isbnLiveData;
    }

    public void getReadCount(MutableLiveData<Integer> readCount) {
        String userId = getCurrentUserID();
        if (userId == null) {
            return;
        }
        usersInfoRef.child(userId).child("lists").child("read")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        readCount.setValue((int) snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        readCount.setValue(null);
                    }
                });
    }

    public void getReadingCount(MutableLiveData<Integer> readingCount) {
        String userId = getCurrentUserID();
        if (userId == null) {
            return;
        }
        usersInfoRef.child(userId).child("lists").child("reading")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        readingCount.setValue((int) snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        readingCount.setValue(null);
                    }
                });
    }

    public void getTbrCount(MutableLiveData<Integer> tbrCount) {
        String userId = getCurrentUserID();
        if (userId == null) {
            return;
        }
        usersInfoRef.child(userId).child("lists").child("tbr")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tbrCount.setValue((int) snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        tbrCount.setValue(null);
                    }
                });
    }

    public void changePassword(String currentPass, String newPass1, MutableLiveData<String> toastMessage) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null)
            return;

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPass);

        user.reauthenticate(credential).addOnCompleteListener(auth -> {
            if (auth.isSuccessful()) {
                user.updatePassword(newPass1).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        toastMessage.setValue("Password changed successfully");
                    else
                        toastMessage.setValue("Rerror changing password. Please try again later");
                });
            } else
                toastMessage.setValue("Current password is incorrect");
        });
    }

    public void deleteAccount(MutableLiveData<String> toastMessage) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null)
            return;

        usersInfoRef.child(user.getUid()).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.delete().addOnCompleteListener(delete -> {
                    if (delete.isSuccessful()) {
                        toastMessage.setValue("Account was successfully deleted");
                        logoutUser();
                    } else
                        toastMessage.setValue("Error deleting the account. Try again later or contact us.");
                });
            } else
                toastMessage.setValue("Error deleting the account. Try again later or contact us.");
        });
    }

    public void requestBook(String title, String author, String isbn, MutableLiveData<String> toastMessage) {
        String userId = getCurrentUserID();
        if (userId == null) {
            toastMessage.setValue("You must be logged in for this");
            return;
        }
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("author", author);
        data.put("isbn13", (isbn != null && !isbn.trim().isEmpty() ? isbn : "N/A"));
        data.put("req_date", date);
        data.put("req_by_user", userId);

        FirebaseDatabase.getInstance().getReference("requested_books")
                .push().setValue(data).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        toastMessage.setValue("Request sent. Thanks!");
                    else
                        toastMessage.setValue("Something went wrong. Try again later");

                });

    }
}
