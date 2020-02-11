package com.xubo.data.dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictionaryEntry {

    private static final Pattern PATTERN_PIN_YIN = Pattern.compile("【(.+)】[\\*0-9]?(（.+）)?([•’0-9a-zA-ZɑāáǎàōóǒòêēéěèềīíǐìūúǔùǖǘǚǜüńňǹĀÁǍÀŌÓǑÒÊĒÉĚÈĪÍǏÌŪÚǓÙǕǗǙǛÜŃŇǸɡ]+)(.+)");

    private static final Pattern PATTERN_PIN_YIN_ALIAS = Pattern.compile("(.)[\\*0-9]?(（.+）)([•’0-9a-zA-ZɑāáǎàōóǒòêēéěèềīíǐìūúǔùǖǘǚǜüńňǹĀÁǍÀŌÓǑÒÊĒÉĚÈĪÍǏÌŪÚǓÙǕǗǙǛÜŃŇǸɡ]+)(.+)");

    String rawLine;

    String text;

    String textAlias;

    String pinyin;

    String description;

    DictionaryEntryType type;

    List<DictionaryEntry> words = new ArrayList<>();

    public DictionaryEntry(String rawLine) {
        this.rawLine = rawLine;

        String normalizedLine = rawLine.replace("•", "");
        normalizedLine = normalizedLine.replace("\uF075", "");


        Matcher matcher = PATTERN_PIN_YIN.matcher(normalizedLine);
        Matcher matcherAlias = PATTERN_PIN_YIN_ALIAS.matcher(normalizedLine);

        if (matcher.matches()) {

            text = matcher.group(1);
            textAlias = matcher.group(2);
            pinyin = matcher.group(3);
            description = matcher.group(4);

            if (text.length() == 1) {
                type = DictionaryEntryType.CHARACTER;
            } else {
                type = DictionaryEntryType.WORD;
            }

        } else if (matcherAlias.matches()) {
            text = matcherAlias.group(1);
            textAlias = matcherAlias.group(2);
            pinyin = matcherAlias.group(3);
            description = matcherAlias.group(4);

            if (text.length() == 1) {
                type = DictionaryEntryType.CHARACTER;
            } else {
                type = DictionaryEntryType.WORD;
            }
        } else {
            System.out.println("Invalid line: " + rawLine);
        }
    }

    public String getText() {
        return text;
    }

    public String getTextAlias() {
        return textAlias;
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getDescription() {
        return description;
    }

    public DictionaryEntryType getType() {
        return type;
    }

    public List<DictionaryEntry> getWords() {
        return words;
    }

    @Override
    public String toString() {
        return text + " [" + pinyin + "] " + description;
    }

    public boolean isValid() {
        return rawLine.trim().length() > 0 && text != null && text.length() > 0;
    }

}
