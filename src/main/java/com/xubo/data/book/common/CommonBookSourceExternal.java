package com.xubo.data.book.common;

import com.xubo.data.book.Book;
import com.xubo.data.book.BookSourceExternal;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CommonBookSourceExternal extends BookSourceExternal {

    private Path bookPath;

    public CommonBookSourceExternal(Path bookPath) {
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
        if (!bookLines.isEmpty()) {
            books.add(new CommonBook(bookLines));
        }
        return books;
    }

    @Override
    protected Path getBookPath() {
        return bookPath;
    }

}
