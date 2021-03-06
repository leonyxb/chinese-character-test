package com.xubo.data.book;

import com.xubo.utils.ChineseResourceReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.util.List;

public abstract class BookSourceInternal implements BookSource {

    private static final Logger logger = LogManager.getLogger(BookSourceInternal.class);

    private List<Book> books = null;
    
    public List<Book> getBooks() {
        if (books == null) {
            logger.info("载入内置书文件: " + getBookPath());
            List<String> rawLines = ChineseResourceReader.readLinesFromResources(getBookPath(), getCharset().toString());
            this.books = buildBooks(rawLines);
            //this.books.forEach(book -> logger.info("    内置书成功载入: " + book.getTitle()));
        }
        return books;
    }

    protected abstract List<Book> buildBooks(List<String> rawLines);
    
    protected abstract String getBookPath(); 
    
    protected abstract Charset getCharset();

}
