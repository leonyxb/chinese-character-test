package com.xubo.data;

import com.xubo.data.book.Book;
import com.xubo.data.dictionary.Dictionary;

import java.util.List;

public interface DataSource {

    List<Book> getBooks();

    Dictionary getDictionary();

}
