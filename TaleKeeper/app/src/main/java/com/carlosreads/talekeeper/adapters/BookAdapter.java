package com.carlosreads.talekeeper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.databinding.ItemBookBinding;
import com.carlosreads.talekeeper.models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> books;

    public BookAdapter(List<Book> books) {
        this.books = books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_book,
                parent,
                false
        );
        return new BookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        //img and click listener
        int width = 200;
        Glide.with(holder.itemView.getContext())
                        .load(book.getCover_url())
                        .override(width,(int) (width * 1.6))
                        .centerCrop()
                        .into(holder.binding.bookCover);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), book.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        private final ItemBookBinding binding;

        public BookViewHolder(@NonNull ItemBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Book book) {
            binding.setBook(book);
            binding.executePendingBindings();
        }
    }
}