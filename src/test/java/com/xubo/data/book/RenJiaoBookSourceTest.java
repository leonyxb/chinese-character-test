package com.xubo.data.book;

import com.xubo.data.book.renjiao.RenJiaoBookSource;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RenJiaoBookSourceTest {

    @Test
    public void read() {
        List<Book> books = new RenJiaoBookSource().getBooks();

        Assert.assertNotNull(books);
    }
}