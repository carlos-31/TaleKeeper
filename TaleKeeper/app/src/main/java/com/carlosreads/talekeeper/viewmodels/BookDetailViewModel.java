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

    public BookDetailViewModel() {
        bookRepository = new BookRepository();
        userRepository = new UserRepository();
        bookStatus.setValue("Add Book");
    }

    public MutableLiveData<Book> getBookLiveData() {
        return bookLiveData;
    }

    public void loadBook(String isbn) {
        bookRepository.getBookByIsbn(isbn, bookLiveData);
        userRepository.isBookFavourite(isbn, isFavouriteLiveData);
    }

    public void updateBookStatus (String status){
        bookStatus.setValue(status);
    }

    public MutableLiveData<String> getBookStatus() {
        return bookStatus;
    }

    public MutableLiveData<Boolean> getIsFavouriteLiveData(){
        return isFavouriteLiveData;
    }

    public void toggleFavourite(String bookIsbn) {
        boolean currentState = isFavouriteLiveData.getValue() != null
                                && isFavouriteLiveData.getValue();
        MutableLiveData <Boolean> result = new MutableLiveData<>();
        if (currentState)
            userRepository.removeFromFavourites(bookIsbn, result);
        else
            userRepository.addToFavourites(bookIsbn, result);
        result.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean updateResult) {
                if (updateResult != null && updateResult)
                    isFavouriteLiveData.setValue(!currentState);
                result.removeObserver(observer -> {});
            }
        });
    }
}
