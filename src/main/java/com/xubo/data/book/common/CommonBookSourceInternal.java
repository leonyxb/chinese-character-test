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

    private String language;
    
    public CommonBookSourceInternal(String bookPath, String language) {
        this.bookPath = bookPath;
        this.language = language;
    }

    @Override
    protected List<Book> buildBooks(List<String> rawLines) {

        List<Book> books = new ArrayList<>();
        List<String> bookLines = new ArrayList<>();

        rawLines.forEach(
                        line -> {
                            if (line.startsWith("Book")) {
                                if (!bookLines.isEmpty()) {
                                    books.add(new CommonBook(bookLines, language));
                                    bookLines.clear();
                                }
                            }
                            bookLines.add(line);
                        }
                );
        books.add(new CommonBook(bookLines, language));
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
