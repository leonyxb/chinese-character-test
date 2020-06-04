package com.xubo.data;

import com.xubo.application.ApplicationConfig;
import com.xubo.data.book.Book;
import com.xubo.data.book.BookSource;
import com.xubo.data.book.common.CommonBookSourceExternal;
import com.xubo.data.book.france.EchelleDuboisBuyse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FrenchData implements DataSource {

    private static final Logger logger = LogManager.getLogger(FrenchData.class);

    private List<Book> books;

    public FrenchData() {
        logger.info("开始载入法语数据...");

        books = getBookSources().stream()
                .flatMap(s -> s.getBooks().stream())
                .collect(Collectors.toList());

        logger.info("结束载入法语数据");
    }

    private List<BookSource> getBookSources() {

        List<BookSource> bookSources = new ArrayList<>();

        try {
            Path folderToScan = Paths.get("", "books", ApplicationConfig.ApplicationLanguage.FRENCH.toString().toLowerCase());
            logger.info("在这个位置扫描扩展书文件: " + folderToScan.toAbsolutePath());
            Files.list(folderToScan).forEach(
                    path -> bookSources.add(new CommonBookSourceExternal(path))
            );
        } catch (NoSuchFileException e) {
            logger.info("没有找到任何扩展书");
        } catch (Exception e) {
            logger.error("扩展书载入异常！", e);
        }

        bookSources.add(new EchelleDuboisBuyse());

        return bookSources;
    }

    @Override
    public List<Book> getBooks() {
        return books;
    }

}
