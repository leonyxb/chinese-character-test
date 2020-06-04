package com.xubo.data.book;

import com.xubo.utils.ChineseResourceReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public abstract class BookSourceExternal implements BookSource {

    private static final Logger logger = LogManager.getLogger(BookSourceExternal.class);

    private List<Book> books = null;
    
    public List<Book> getBooks() {
        if (books == null) {
            List<String> rawLines = ChineseResourceReader.readLines(getBookPath(), StandardCharsets.UTF_8);
            logger.info("载入扩展书文件：" + getBookPath());
            try {
                this.books = buildBooks(rawLines);
            } catch (Exception e) {
                logger.error("扩展书文件载入异常: " + e.getMessage(), e);
                throw e;
            }
            this.books.forEach(book -> logger.info("    扩展书成功载入: " + book.getTitle()));
        }
        return books;
    }

    protected abstract List<Book> buildBooks(List<String> rawLines);
    
    protected abstract Path getBookPath();

}
