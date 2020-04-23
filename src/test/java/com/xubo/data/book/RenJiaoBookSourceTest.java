package com.xubo.data.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xubo.application.ApplicationUtils;
import com.xubo.data.book.france.EchelleDuboisBuyse;
import com.xubo.data.character.Character;
import com.xubo.data.character.CharacterTestRecord;
import com.xubo.data.character.CharacterTestRecords;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RenJiaoBookSourceTest {

    @Test
    public void read() throws IOException {
        File json = new File("C:\\Users\\Alain\\Desktop\\records\\爱.json");
        CharacterTestRecords testRecord = new ObjectMapper().readValue(json, CharacterTestRecords.class);

        Assert.assertNotNull(testRecord);

        List<CharacterTestRecord> perDay = ApplicationUtils.oneRecordPerDay(testRecord.getRecords());

        Assert.assertNotNull(perDay);

        Character character = new Character("妈");
        List<CharacterTestRecord> records = character.getTestRecord().getRecords();
        records.clear();
        records.addAll(testRecord.getRecords());

        ApplicationUtils.Colors displayedColors = ApplicationUtils.getDisplayedColors(character);

        Assert.assertNotNull(displayedColors);
    }
}