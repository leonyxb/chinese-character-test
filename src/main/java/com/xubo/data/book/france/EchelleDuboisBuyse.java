package com.xubo.data.book.france;

import com.xubo.data.book.Book;
import com.xubo.data.book.BookSourceInternal;
import com.xubo.data.book.common.InMemoryBook;
import com.xubo.data.character.Character;
import com.xubo.data.character.FrenchCharacter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class EchelleDuboisBuyse extends BookSourceInternal {

    @Override
    protected List<Book> buildBooks(List<String> rawLines) {
        Map<String, List<String>> lines = rawLines.stream()
                .collect(Collectors.groupingBy(
                        line -> line.split("\t")[0])
                );

        List<Book> books = new ArrayList<>();
        lines.forEach((k, v) -> {
            books.add(new EchelleBook(Integer.parseInt(k), v));
        });

        books.sort((o1, o2) -> {
            EchelleBook eb1 = (EchelleBook) o1;
            EchelleBook eb2 = (EchelleBook) o2;
            return eb1.getEchelle().compareTo(eb2.getEchelle());
        });

        Map<String, List<FrenchCharacter>> frenchWords = books.stream()
                .flatMap(book -> book.getLessons().stream())
                .flatMap(lesson -> lesson.getCharacters().stream())
                .map(character -> (FrenchCharacter) character)
                .collect(Collectors.groupingBy(FrenchCharacter::getGenre));

        frenchWords.keySet().stream().sorted().forEach(k -> {
            List<Character> characters = new ArrayList<>();
            characters.addAll(frenchWords.get(k));
            books.add(new InMemoryBook("Echelle: " + getShortGenre(k) , characters, 5));
        });

        return books;
    }

    private String getShortGenre(String k) {
        return k.replace("adjectif", "adj.");
    }

    @Override
    protected String getBookPath() {
        return "/book/french/echelle_dubois_buyse.txt";
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }
}
