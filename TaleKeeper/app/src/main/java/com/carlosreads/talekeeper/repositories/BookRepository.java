package com.carlosreads.talekeeper.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.carlosreads.talekeeper.models.Book;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    private final DatabaseReference bookRef;
    private final DatabaseReference devBooksRef;

    public BookRepository() {
        bookRef = FirebaseDatabase.getInstance().getReference("books_table");
        devBooksRef = FirebaseDatabase.getInstance().getReference("dev_favs");
    }


    public void loadSpotlight(MutableLiveData<Book> bookLiveData) {
        devBooksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<String> books = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String isbn = child.getValue(String.class);
                    if (isbn != null) {
                        books.add(isbn);
                    }
                }

                //logic for daily book shuffle random seed thing

                getBookByIsbn(books.get(0), bookLiveData);

            }

            @Override
            public void onCancelled(DatabaseError error) {
               }
        });
    }


    public void getBookByIsbn(String isbn, MutableLiveData<Book> bookLiveData) {
        bookRef.child(isbn).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Book book = snapshot.getValue(Book.class);
                bookLiveData.setValue(book);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

}
