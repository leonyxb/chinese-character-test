package com.xubo.data;

import com.xubo.application.ApplicationConfig;
import com.xubo.data.book.Book;
import com.xubo.data.book.BookSource;
import com.xubo.data.book.common.CommonBookSourceExternal;
import com.xubo.data.book.common.CommonBookSourceInternal;
import com.xubo.data.book.renjiao.RenJiaoBookSource;
import com.xubo.data.character.CharacterFactory;
import com.xubo.data.dictionary.Dictionary;
import com.xubo.data.word.WordsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChineseData implements DataSource {

    private static final Logger logger = LogManager.getLogger(ChineseData.class);

    private List<Book> books;

    private Dictionary dictionary;

    public List<Book> getBooks() {
        return books;
    }

    @Override
    public Dictionary getDictionary() {
        return dictionary;
    }


    public ChineseData() {
        logger.info("开始载入中文数据...");

        this.dictionary = new Dictionary();

        CharacterFactory.setDictionary(dictionary);
        CharacterFactory.setWordsRepository(new WordsRepository());

        books = getBookSources().stream()
                .flatMap(s -> s.getBooks().stream())
                .collect(Collectors.toList());

        logger.info("结束载入中文数据");
    }

    private List<BookSource> getBookSources() {

        List<BookSource> bookSources = new ArrayList<>();

        logger.info("载入扩展书...");
        try {
            Path folderToScan = ApplicationConfig.CHINESE_CONFIG.getResourceFolder();
            logger.info("    扫描文件夹: " + folderToScan.toAbsolutePath());
            Files.list(folderToScan)
                    .filter(f-> f.toFile().isFile())
                    .forEach(path ->
                            bookSources.add(new CommonBookSourceExternal(path, "CN"))
                    );
        } catch (NoSuchFileException e) {
            logger.info("    文件夹不存在");
        } catch (Exception e) {
            logger.error("    扩展书载入异常！", e);
        }

        bookSources.add(new RenJiaoBookSource());
        bookSources.add(new CommonBookSourceInternal("/book/character_commonly_used.txt", "CN"));
        bookSources.add(new CommonBookSourceInternal("/book/character_similar.txt", "CN"));

        return bookSources;
    }

}
