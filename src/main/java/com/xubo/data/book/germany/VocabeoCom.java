package com.xubo.data.book.germany;

import com.xubo.data.book.Book;
import com.xubo.data.book.BookSourceInternal;
import com.xubo.data.book.common.InMemoryBook;
import com.xubo.data.character.GermanWord;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VocabeoCom extends BookSourceInternal {

    @Override
    protected List<Book> buildBooks(List<String> rawLines) {

        List<Book> books = new ArrayList<>();

        String fullContent = StringUtils.join(rawLines, " ");
        String[] elements = fullContent.split("div slot=");

        Map<String, GermanWord> germanyWordsMap = new LinkedHashMap<>();
        for (String element: elements) {
            if (element.contains("cell word")) {
                String wordClass = StringUtils.substringBetween(element, "--color-accent: var(--color-", ");");
                String wordTextOriginal = StringUtils.substringAfter(StringUtils.substringBetween(element, "cell word no-break svelte-37mltv", "</div>"), ">");
                String wordText = wordTextOriginal;
                if (wordClass.equals("Noun") || wordClass.equals("Num")) {
                    wordText = StringUtils.substringBefore(wordTextOriginal, "<");
                }
                wordText = wordText.trim();
                String genre = StringUtils.substringBetween(wordTextOriginal, ">, ", "<");
                String wordLevel = StringUtils.substringBetween(element, "<div class=\"cell level svelte-37mltv\">", "</div>");
                String wordFreq = StringUtils.substringBetween(element, "<div class=\"cell frequency svelte-37mltv\">", "</div>");

                GermanWord word = new GermanWord(wordText);
                word.setWordClass(wordClass);
                word.setFrequency(wordFreq);
                word.setGenre(genre);
                word.setLevel(wordLevel);
                word.setOriginalText(wordText);
                word.generateDescription();

                word.split().forEach(n -> {
                    if (germanyWordsMap.containsKey(n.getText())) {
                        germanyWordsMap.get(n.getText()).merge(n);
                    } else {
                        germanyWordsMap.put(n.getText(), n);
                    }
                });

            }
        }

        List<GermanWord> germanyWords = new ArrayList<>();
        germanyWords.addAll(germanyWordsMap.values());
        books.add(new InMemoryBook("100:  Frequency List", new ArrayList<>(germanyWords.subList(0, 100)), 5));
        books.add(new InMemoryBook("200:  Frequency List", new ArrayList<>(germanyWords.subList(100, 200)), 5));
        books.add(new InMemoryBook("300:  Frequency List", new ArrayList<>(germanyWords.subList(200, 300)), 5));
        books.add(new InMemoryBook("400:  Frequency List", new ArrayList<>(germanyWords.subList(300, 400)), 5));
        books.add(new InMemoryBook("500:  Frequency List", new ArrayList<>(germanyWords.subList(400, 500)), 5));
        books.add(new InMemoryBook("600:  Frequency List", new ArrayList<>(germanyWords.subList(500, 600)), 5));
        books.add(new InMemoryBook("800:  Frequency List", new ArrayList<>(germanyWords.subList(600, 800)), 5));
        books.add(new InMemoryBook("1000: Frequency List", new ArrayList<>(germanyWords.subList(800, 1000)), 5));
        books.add(new InMemoryBook("1200: Frequency List", new ArrayList<>(germanyWords.subList(1000, 1200)), 5));
        books.add(new InMemoryBook("1400: Frequency List", new ArrayList<>(germanyWords.subList(1200, 1400)), 5));
        books.add(new InMemoryBook("1600: Frequency List", new ArrayList<>(germanyWords.subList(1400, 1600)), 5));
        books.add(new InMemoryBook("1800: Frequency List", new ArrayList<>(germanyWords.subList(1600, 1800)), 5));
        books.add(new InMemoryBook("2000: Frequency List", new ArrayList<>(germanyWords.subList(1800, 2000)), 5));

        return books;
    }

    @Override
    protected String getBookPath() {
        return "/book/germany/vocabeo.html.txt";
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }
}
