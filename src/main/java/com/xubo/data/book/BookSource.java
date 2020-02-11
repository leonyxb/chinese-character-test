package com.xubo.data.book;

import com.xubo.utils.ChineseResourceReader;

import java.nio.charset.Charset;
import java.util.List;

public abstract class BookSource {
    
    private List<Book> books = null;
    
    public List<Book> getBooks() {
        if (books == null) {
            List<String> rawLines = ChineseResourceReader.readLines(getBookPath(), getCharset().toString());
            this.books = readBooks(rawLines);
        }
        return books;
    }

    protected abstract List<Book> readBooks(List<String> rawLines);
    
    protected abstract String getBookPath(); 
    
    protected abstract Charset getCharset();

}
