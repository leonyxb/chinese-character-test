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
        List<Book> books = new ArrayList<>();

        List<FrenchCharacter> frenchCharacters = rawLines.stream()
                .map(this::buildCharacter).collect(Collectors.toList());

        Map<String, List<FrenchCharacter>> frenchCharactersByClasse = frenchCharacters.stream()
                .collect(Collectors.groupingBy(FrenchCharacter::getClasse));

        books.add(new EchelleBook("CP", frenchCharactersByClasse.get("CP")));
        books.add(new EchelleBook("CE1", frenchCharactersByClasse.get("CE1")));
        books.add(new EchelleBook("CE2", frenchCharactersByClasse.get("CE2")));
        books.add(new EchelleBook("CM1", frenchCharactersByClasse.get("CM1")));
        books.add(new EchelleBook("CM2", frenchCharactersByClasse.get("CM2")));
        books.add(new EchelleBook("Collège", frenchCharactersByClasse.get("Collège")));


        Map<String, List<FrenchCharacter>> frenchCharactersByGenre = frenchCharacters.stream()
                .collect(Collectors.groupingBy(FrenchCharacter::getGenre));

        books.add(buildInMemoryBook("Genre <nom féminin> ", frenchCharactersByGenre.get("nom féminin")));
        books.add(buildInMemoryBook("Genre <nom masculin> ", frenchCharactersByGenre.get("nom masculin")));
        books.add(buildInMemoryBook("Genre <adjectif> ", frenchCharactersByGenre.get("adjectif qualificatif")));
        books.add(buildInMemoryBook("Genre <verbe 1er group> ", frenchCharactersByGenre.get("verbe 1er group")));
        books.add(buildInMemoryBook("Genre <verbe 2ème group> ", frenchCharactersByGenre.get("verbe 2ème group")));
        books.add(buildInMemoryBook("Genre <verbe 3ème group> ", frenchCharactersByGenre.get("verbe 3ème group")));
        books.add(buildInMemoryBook("Genre <adverbe> ", frenchCharactersByGenre.get("adverbe")));

        return books;
    }

    private InMemoryBook buildInMemoryBook(String title, List<FrenchCharacter> c) {
        List<Character> characters = new ArrayList<>();
        characters.addAll(c);
        return new InMemoryBook(title, characters, 5);
    }

    @Override
    protected String getBookPath() {
        return "/book/french/echelle_dubois_buyse.txt";
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }


    private FrenchCharacter buildCharacter(String line) {
        String[] elements = line.split("\t");
        FrenchCharacter character = new FrenchCharacter(elements[2]);

        character.setEchelle(Integer.valueOf(elements[0]));
        character.setClasse(elements[1]);
        character.setCategory(elements[3]);
        character.setGenre(elements[4]);

        character.getWords().add(elements[4]);

        return character;
    }
}
