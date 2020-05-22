package com.xubo.data.record;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xubo.application.ApplicationUtils;
import com.xubo.data.character.Character;
import com.xubo.data.character.CharacterTestRecord;
import com.xubo.data.character.CharacterTestRecords;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TestRecordManager {

    private static final Logger logger = LogManager.getLogger(Character.class);

    private static final TestRecordManager INSTANCE = new TestRecordManager();

    public static TestRecordManager getInstance() {
        return INSTANCE;
    }

    private TestRecordManager() {

    }

    private Map<String, CharacterTestRecords> history = new HashMap<>();

    public CharacterTestRecords getFullRecords(String text) {
        history.putIfAbsent(text, readTestRecordsFile(text));
        return history.get(text);
    }

    public void addRecord(String text, CharacterTestRecord record) {
        history.get(text).getRecords().add(record);
        writeTestRecordsFile(text);
    }

    private CharacterTestRecords readTestRecordsFile(String text) {
        File json = getJsonFile(text);
        if (json.exists()) {
            try {
                return new ObjectMapper().readValue(json, CharacterTestRecords.class);
            } catch (IOException e) {
                logger.error("Error: Can not read the test record from " + json.getAbsolutePath());
            }
        }
        return new CharacterTestRecords(text);
    }

    public void writeTestRecordsFile(String text) {
        File json = getJsonFile(text);
        try {
            new ObjectMapper().writeValue(json, history.get(text));
        } catch (IOException e) {
            logger.error("Error: Can not write the test record to " + json.getAbsolutePath());
        }
    }

    private File getJsonFile(String text) {
        File baseDir = new File("records");
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            baseDir.mkdirs();
        }
        return new File("records" + File.separator + text + ".json");
    }

    public Map<Date, List<CharacterTestRecords>> getHistoryByDate() {

        Map<Date, List<CharacterTestRecords>> recordsByDate = new HashMap<>();

        history.values().forEach(rs -> {
            List<CharacterTestRecord> recordsPerDay = ApplicationUtils.oneRecordPerDay(rs.getRecords());
            recordsPerDay.forEach(r -> {
                recordsByDate.putIfAbsent(r.getDate(), new ArrayList<>());
                recordsByDate.get(r.getDate()).add(buildRecordsWithOneRecord(rs.getName(), r));
            });
        });

        return recordsByDate;
    }

    private CharacterTestRecords buildRecordsWithOneRecord(String name, CharacterTestRecord r) {
        CharacterTestRecords records = new CharacterTestRecords();
        records.setName(name);
        records.getRecords().add(r);
        return records;
    }
}
