package com.xubo.data.book.renjiao;

import com.xubo.data.book.Book;
import com.xubo.data.book.BookSource;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RenJiaoBookSource extends BookSource {

    @Override
    protected List<Book> readBooks(List<String> rawLines) {
        List<Book> books = new ArrayList<>();

        List<String> bookLines = new ArrayList<>();

        rawLines.subList(1, rawLines.size())
                .forEach(
                line -> {
                    if (line.startsWith("人教版")) {
                        if (!bookLines.isEmpty()) {
                            books.addAll(get2Books(bookLines));
                            bookLines.clear();
                        }
                    }
                    bookLines.add(line);
                }
        );
        books.addAll(get2Books(bookLines));

        return books;
    }

    @Override
    protected String getBookPath() {
        return "/book/character_of_renjiao_primary_school.txt";
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    private List<RenJiaoBook> get2Books(List<String> rawLines) {

        List<RenJiaoBook> books = new ArrayList<>();

        String title = rawLines.get(0);

        List<String> bookLines = new ArrayList<>();
        rawLines.subList(1, rawLines.size())
                .forEach(line -> {
                    if (line.startsWith("生字表")) {
                        if (!bookLines.isEmpty()) {
                            books.add(new RenJiaoBook(title, bookLines));
                            bookLines.clear();
                        }
                    }
                    bookLines.add(line);
                });

        books.add(new RenJiaoBook(title, bookLines));

        return books;

    }

}
