package com.xubo.data.book.english;

import com.xubo.data.book.Book;
import com.xubo.data.book.BookSourceInternal;
import com.xubo.data.book.common.InMemoryBook;
import com.xubo.data.character.Character;
import com.xubo.data.character.EnglishWord;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnglishFrequencyList extends BookSourceInternal {

    private static final Map<String, String> POS_MAP;

    static {
        POS_MAP = new HashMap<>();
        POS_MAP.put("fw", "function word");
        POS_MAP.put("v", "verb");
        POS_MAP.put("r", "adverb");
        POS_MAP.put("j", "adjective");
        POS_MAP.put("m", "number");
        POS_MAP.put("u", "interjection");
        POS_MAP.put("n", "noun");
        POS_MAP.put("k", "proper noun");
    }

    @Override
    protected List<Book> buildBooks(List<String> rawLines) {
        List<Book> books = new ArrayList<>();

        List<EnglishWord> words = rawLines.stream()
                .map(this::buildWord).collect(Collectors.toList());

        books.add(new InMemoryBook("100:  Frequency List", new ArrayList<>(words.subList(0, 100)), 5));
        books.add(new InMemoryBook("200:  Frequency List", new ArrayList<>(words.subList(100, 200)), 5));
        books.add(new InMemoryBook("300:  Frequency List", new ArrayList<>(words.subList(200, 300)), 5));
        books.add(new InMemoryBook("400:  Frequency List", new ArrayList<>(words.subList(300, 400)), 5));
        books.add(new InMemoryBook("500:  Frequency List", new ArrayList<>(words.subList(400, 500)), 5));
        books.add(new InMemoryBook("600:  Frequency List", new ArrayList<>(words.subList(500, 600)), 5));
        books.add(new InMemoryBook("800:  Frequency List", new ArrayList<>(words.subList(600, 800)), 5));
        books.add(new InMemoryBook("1000: Frequency List", new ArrayList<>(words.subList(800, 1000)), 5));
        //books.add(new InMemoryBook("1500: Frequency List", words.subList(1000, 1500), 5));
        //books.add(new InMemoryBook("2000: Frequency List", words.subList(1500, 2000), 10));
        //books.add(new InMemoryBook("3000: Frequency List", words.subList(2000, 3000), 10));
        //books.add(new InMemoryBook("4000: Frequency List", words.subList(3000, 4000), 10));
        //books.add(new InMemoryBook("5000: Frequency List", words.subList(4000, 5000), 10));

        List<Character> verbs = words.subList(0, 1000).stream().filter(w -> w.getPartOfSpeech().equals("v")).map(w -> (Character) w).collect(Collectors.toList());
        books.add(new InMemoryBook("Verbs:Frequency List", verbs, 5));

        return books;
    }

    @Override
    protected String getBookPath() {
        return "/book/english/english_frequency_list.csv";
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    private EnglishWord buildWord(String line) {
        String[] elements = line.split(",");
        EnglishWord character = new EnglishWord(elements[1]);

        character.setPartOfSpeech(elements[2]);
        character.getWords().add(POS_MAP.getOrDefault(character.getPartOfSpeech(), character.getPartOfSpeech()));

        character.setFrequency(Integer.valueOf(elements[3]));
        String inflections = StringUtils.substringBetween(line, "\"", "\"");
        if (StringUtils.isNotBlank(inflections)) {
            character.setInflections(Stream.of(inflections.split(",")).map(String::trim).collect(Collectors.toList()));
            character.getWords().add(inflections);
        }

        return character;
    }
}
