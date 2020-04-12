package com.xubo.data;

import com.xubo.data.book.Book;
import com.xubo.data.book.france.EchelleDuboisBuyse;

import java.util.List;

public class FrenchData implements DataSource {

    private List<Book> books;

    public FrenchData() {
        this.books = new EchelleDuboisBuyse().getBooks();
    }

    @Override
    public List<Book> getBooks() {
        return books;
    }
}
