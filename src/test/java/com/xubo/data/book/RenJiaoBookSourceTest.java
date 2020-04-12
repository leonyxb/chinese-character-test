package com.xubo.data.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xubo.data.book.france.EchelleDuboisBuyse;
import com.xubo.data.character.CharacterStatus;
import com.xubo.data.character.CharacterTestRecords;
import com.xubo.data.character.CharacterTestRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class RenJiaoBookSourceTest {

    @Test
    public void read() throws IOException {
        List<Book> books = new EchelleDuboisBuyse().getBooks();

        Assert.assertNotNull(books);
    }
}