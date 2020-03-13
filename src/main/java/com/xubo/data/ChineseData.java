package com.xubo.data;

import com.xubo.data.book.Book;
import com.xubo.data.book.BookSource;
import com.xubo.data.book.common.CommonBookSource;
import com.xubo.data.book.renjiao.RenJiaoBookSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChineseData {

    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public ChineseData() {
        books = getBookSources().stream()
                .flatMap(s -> s.getBooks().stream())
                .collect(Collectors.toList());
    }


    private List<BookSource> getBookSources() {
        return Arrays.asList(
                new RenJiaoBookSource(), 
                new CommonBookSource("/book/character_commonly_used.txt"),
                new CommonBookSource("/book/character_similar.txt")
        );
    }

}
