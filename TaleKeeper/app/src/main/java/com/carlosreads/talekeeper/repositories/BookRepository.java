package com.carlosreads.talekeeper.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.carlosreads.talekeeper.models.Book;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookRepository {

    private final DatabaseReference bookRef;
    private final DatabaseReference devBooksRef;

    public BookRepository() {
        // sets up references to the tables being used from realtime database
        bookRef = FirebaseDatabase.getInstance().getReference("books_table");
        devBooksRef = FirebaseDatabase.getInstance().getReference("dev_favs");
    }


    public void loadSpotlight(MutableLiveData<Book> bookLiveData) {
        devBooksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<String> books = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    // makes a list of all the isbn in the dev_favs table
                    String isbn = child.getValue(String.class);
                    if (isbn != null) {
                        books.add(isbn);
                    }
                }

                // uses todays date as a seed to shuffle the list of books
                // making it so the output is the same, but changes every day
                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                Collections.shuffle(books, new java.util.Random(date.hashCode()));

                // sets the first book in the list after shuffling in the spotlight
                getBookByIsbn(books.get(0), bookLiveData);

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }


    public void getBookByIsbn(String isbn, MutableLiveData<Book> bookLiveData) {
        // gets a specific book by its isbn (the key in the table)
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

    public void getAllBooks(MutableLiveData<List<Book>> bookLiveData) {
        bookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Book> books = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Book book = child.getValue(Book.class);
                    books.add(book);
                }
                bookLiveData.setValue(books);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                bookLiveData.setValue(null);
            }
        });
    }

    public void getBooksByGenre(MutableLiveData<List<Book>> bookLiveData, String genre) {
        bookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Book> books = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Book book = child.getValue(Book.class);
                    // checks each book for the genre requested
                    if (book.getGenres().toLowerCase().contains(genre.toLowerCase()))
                        books.add(book);
                }
                bookLiveData.setValue(books);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                bookLiveData.setValue(null);
            }
        });
    }

}
