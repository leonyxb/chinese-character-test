package com.xubo.data;

import com.xubo.application.ApplicationConfig;
import com.xubo.data.book.Book;
import com.xubo.data.book.BookSource;
import com.xubo.data.book.common.CommonBookSource;
import com.xubo.data.book.common.CommonBookSourceExternal;
import com.xubo.data.book.renjiao.RenJiaoBookSource;
import com.xubo.data.dictionary.Dictionary;
import com.xubo.data.word.WordsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChineseData implements DataSource {

    private static final Logger logger = LogManager.getLogger(ChineseData.class);

    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public ChineseData() {
        logger.info("Start Loading chinese data");
        books = getBookSources().stream()
                .flatMap(s -> s.getBooks().stream())
                .collect(Collectors.toList());
        logger.info("End Loading chinese data");
    }


    private List<BookSource> getBookSources() {

        List<BookSource> bookSources = new ArrayList<>();

        try {
            Path folderToScan = Paths.get("", "books", ApplicationConfig.ApplicationLanguage.CHINESE.toString().toLowerCase());
            logger.info("Scan external book on folder: " + folderToScan.toAbsolutePath());
            Files.list(folderToScan).forEach(
                    path -> bookSources.add(new CommonBookSourceExternal(path))
            );

        } catch (Exception e) {
            logger.error("Exception when scanning folder", e);
        }

        bookSources.add(new RenJiaoBookSource());
        bookSources.add(new CommonBookSource("/book/character_commonly_used.txt"));
        bookSources.add(new CommonBookSource("/book/character_similar.txt"));

        return bookSources;
    }

}
