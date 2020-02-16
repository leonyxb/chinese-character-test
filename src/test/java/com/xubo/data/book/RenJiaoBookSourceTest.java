package com.xubo.data.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xubo.data.character.CharacterStatus;
import com.xubo.data.character.CharacterTestRecords;
import com.xubo.data.character.CharacterTestRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class RenJiaoBookSourceTest {

    @Test
    public void read() throws IOException {
        CharacterTestRecords records = new CharacterTestRecords("a");
        records.getRecords().add(new CharacterTestRecord(new Date(), CharacterStatus.UNKNOWN));

        File json = new File("a.json");
        new ObjectMapper().writeValue(json, records);

        CharacterTestRecords newRecords = new ObjectMapper().readValue(json, CharacterTestRecords.class);
        Assert.assertEquals(records.getName(), newRecords.getName());
    }
}