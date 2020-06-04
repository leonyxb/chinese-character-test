package com.xubo.data.book.common;

import com.xubo.data.book.Book;
import com.xubo.data.book.BookSourceInternal;
import com.xubo.data.book.common.CommonBook;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CommonBookSourceInternal extends BookSourceInternal {

    private String bookPath;
    
    public CommonBookSourceInternal(String bookPath) {
        this.bookPath = bookPath;
    }

    @Override
    protected List<Book> buildBooks(List<String> rawLines) {

        List<Book> books = new ArrayList<>();
        List<String> bookLines = new ArrayList<>();

        rawLines.forEach(
                        line -> {
                            if (line.startsWith("Book")) {
                                if (!bookLines.isEmpty()) {
                                    books.add(new CommonBook(bookLines));
                                    bookLines.clear();
                                }
                            }
                            bookLines.add(line);
                        }
                );
        books.add(new CommonBook(bookLines));
        return books;
    }

    @Override
    protected String getBookPath() {
        return bookPath;
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }
}
