package com.xubo.data.dictionary;

import com.xubo.utils.ChineseResourceReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class Dictionary {

    private static final Logger logger = LogManager.getLogger(Dictionary.class);

    private List<String> rawLines;

    private List<DictionaryEntry> entries;

    private Map<String, List<DictionaryEntry>> groupedEntries;

    public Dictionary() {
        logger.info("Start loading chinese dictionary...");
        rawLines = ChineseResourceReader.readLinesFromResources("/dictionary.txt", "Unicode");
        entries = buildEntries(rawLines);
        logger.info("End loading chinese dictionary...");
    }

    private List<DictionaryEntry> buildEntries(List<String> rawLines) {
        List<DictionaryEntry> dictionaryEntries = rawLines.stream()
                .filter(this::firstFilter)
                .map(DictionaryEntry::new)
                .filter(DictionaryEntry::isValid)
                .collect(Collectors.toList());

        linkWords(dictionaryEntries);

        groupedEntries = dictionaryEntries.stream()
                .filter(entry -> entry.getType() == DictionaryEntryType.CHARACTER)
                .collect(groupingBy(DictionaryEntry::getText));

        return dictionaryEntries;
    }

    private boolean firstFilter(String line) {

        line = line.trim();

        boolean isValid = line.trim().length() > 5;
        isValid = isValid && (!line.startsWith("另见"));
        isValid = isValid && (!line.startsWith("制作"));
        isValid = isValid && (!line.startsWith("〈"));
        isValid = isValid && (!line.startsWith("＜"));
        isValid = isValid && (!line.startsWith("："));
        isValid = isValid && (!line.startsWith("“"));
        isValid = isValid && (!line.startsWith("•"));
        isValid = isValid && (!line.startsWith("ɡ"));
        isValid = isValid && (!line.startsWith("（"));
        isValid = isValid && (!line.startsWith("━━"));
        isValid = isValid && (!line.startsWith("《"));
        isValid = isValid && !line.equals("【");

        return isValid;
    }

    private void linkWords(List<DictionaryEntry> dictionaryEntries) {

        DictionaryEntry character = null;
        for (DictionaryEntry dictionaryEntry : dictionaryEntries) {
            if (dictionaryEntry.getType() == DictionaryEntryType.CHARACTER) {
                character = dictionaryEntry;
            } else {
                character.getWords().add(dictionaryEntry);
            }
        }

    }

    public List<DictionaryEntry> getEntries() {
        return entries;
    }

    public List<DictionaryEntry> getEntries(String text) {
        return groupedEntries.get(text);
    }



}
