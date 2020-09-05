package com.xubo.data.book.renjiao;

import com.xubo.data.book.Book;
import com.xubo.data.book.BookSourceInternal;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RenJiaoBookSource extends BookSourceInternal {

    @Override
    protected List<Book> buildBooks(List<String> rawLines) {
        List<Book> books = new ArrayList<>();
        List<Book> booksForWrite = new ArrayList<>();


        List<String> bookLines = new ArrayList<>();

        rawLines.subList(1, rawLines.size())
                .forEach(
                line -> {
                    if (line.startsWith("人教版")) {
                        if (!bookLines.isEmpty()) {
                            List<RenJiaoBook> book2 = get2Books(bookLines);
                            books.add(book2.get(0));
                            if (book2.size() > 1) {
                                booksForWrite.add(book2.get(1));
                            }
                            bookLines.clear();
                        }
                    }
                    bookLines.add(line);
                }
        );
        List<RenJiaoBook> book2 = get2Books(bookLines);
        books.add(book2.get(0));
        if (book2.size() > 1) {
            booksForWrite.add(book2.get(1));
        }

        return Stream.of(books, booksForWrite).flatMap(Collection::stream).collect(Collectors.toList());
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
