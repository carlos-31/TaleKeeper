package com.carlosreads.talekeeper.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.carlosreads.talekeeper.models.Book;
import com.carlosreads.talekeeper.repositories.BookRepository;
import com.carlosreads.talekeeper.repositories.UserRepository;

public class BookDetailViewModel extends ViewModel {
    private BookRepository bookRepository;
    private UserRepository userRepository;
    private MutableLiveData<Book> bookLiveData = new MutableLiveData<>();
    private MutableLiveData<String> bookStatus = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFavouriteLiveData = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> userLoggegIn = new MutableLiveData<>();

    public BookDetailViewModel() {
        bookRepository = new BookRepository();
        userRepository = new UserRepository();
        bookStatus.setValue("Add book");
        userRepository.checkLogin(userLoggegIn, new MutableLiveData<>());
    }

    public MutableLiveData<Book> getBookLiveData() {
        return bookLiveData;
    }

    public MutableLiveData<String> getBookStatus() {
        return bookStatus;
    }

    public MutableLiveData<Boolean> getIsFavouriteLiveData() {
        return isFavouriteLiveData;
    }

    public MutableLiveData<Boolean> getUserLoggedIn() {
        return userLoggegIn;
    }

    public void loadBook(String isbn) {
        bookRepository.getBookByIsbn(isbn, bookLiveData);
        userRepository.isBookFavourite(isbn, isFavouriteLiveData);
        userRepository.getBookListStatus(isbn, bookStatus);
    }

    public void updateBookStatus(String newList) {
        String isbn = bookLiveData.getValue() != null ? bookLiveData.getValue().getIsbn13() : null;
        if (isbn == null) return;
        bookStatus.setValue(newList);

        //remove from all list, since lists ate mutually exclusive
        userRepository.removeBookFromAllLists(isbn);

        //adds book into the list the user selected
        switch (newList) {
            case "To be read":
                userRepository.addBookToList(isbn, "tbr");
                break;
            case "Reading":
                userRepository.addBookToList(isbn, "reading");
                break;
            case "Read":
                userRepository.addBookToList(isbn, "read");
                break;
        }
    }

    public void toggleFavourite(String bookIsbn) {
        //gets current favourite status of the book
        boolean currentState = isFavouriteLiveData.getValue() != null && isFavouriteLiveData.getValue();
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        //Updates the database with the change
        if (currentState) userRepository.removeFromFavourites(bookIsbn, result);
        else userRepository.addToFavourites(bookIsbn, result);

        //observes the change in the database, and when its value changes it
        // toggles the value of the liveData to update the UI
        result.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean updateResult) {
                if (updateResult != null && updateResult)
                    isFavouriteLiveData.setValue(!currentState);
                result.removeObserver(this);
            }
        });
    }
}
