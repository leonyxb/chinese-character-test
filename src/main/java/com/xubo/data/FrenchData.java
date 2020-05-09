package com.xubo.data;

import com.xubo.data.book.Book;
import com.xubo.data.book.france.EchelleDuboisBuyse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class FrenchData implements DataSource {

    private static final Logger logger = LogManager.getLogger(FrenchData.class);

    private List<Book> books;

    public FrenchData() {

        logger.info("Start Loading french data");
        this.books = new EchelleDuboisBuyse().getBooks();

        logger.info("End Loading french data");
    }

    @Override
    public List<Book> getBooks() {
        return books;
    }
}
