package com.xubo.data.dictionary;

import com.xubo.utils.ChineseResourceReader;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class Dictionary {

    private List<String> rawLines;

    private List<DictionaryEntry> entries;

    private Map<String, List<DictionaryEntry>> groupedEntries;

    public Dictionary() {
        rawLines = ChineseResourceReader.readLines("/dictionary.txt", "Unicode");
        entries = buildEntries(rawLines);
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
