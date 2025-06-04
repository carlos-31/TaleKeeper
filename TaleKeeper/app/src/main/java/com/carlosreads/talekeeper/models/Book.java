package com.carlosreads.talekeeper.models;

// this model will be used as the structure of the book's data fetched from firebase

public class Book {
    private String isbn13;
    private String title;
    private String author;
    private String genres;
    private String cover_url;
    private String synopsis;
    private String publisher;
    private int page_count;
    private int pub_year;
    private String language;
    private String genres_en;

    public Book() {
    }

    public Book(String isbn13, String title, String author, String genres, String cover_url, String synopsis, String publisher, int page_count, int pub_year, String language, String genres_en) {
        this.isbn13 = isbn13;
        this.title = title;
        this.author = author;
        this.genres = genres;
        this.cover_url = cover_url;
        this.synopsis = synopsis;
        this.publisher = publisher;
        this.page_count = page_count;
        this.pub_year = pub_year;
        this.language = language;
        this.genres_en = genres_en;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenres() {
        return genres;
    }

    public String getCover_url() {
        return cover_url;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getPage_count() {
        return page_count;
    }

    public int getPub_year() {
        return pub_year;
    }

    public String getLanguage() {
        return language;
    }

    public String getGenres_en() {
        return genres_en;
    }
}