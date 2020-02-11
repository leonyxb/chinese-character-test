package com.xubo.data.character;

import com.xubo.data.book.Lesson;
import com.xubo.data.dictionary.Dictionary;
import com.xubo.data.dictionary.DictionaryEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterFactory {

    private static Map<String, Character> characters = new HashMap<>();

    private static Dictionary dictionary = new Dictionary();

    public static Character getCharacter(String text, Lesson lesson) {
        Character c;
        if (characters.containsKey(text)) {
            c = characters.get(text);
        } else {
            c = new Character(text);
            characters.put(text, c);

            linkDictionary(c);
        }

        c.getLessons().add(lesson);

        return c;
    }

    private static void linkDictionary(Character character) {
        List<DictionaryEntry> entries = dictionary.getEntries(character.getText());
        if (entries == null) {
            System.out.println("Entry not found for " + character.getText());
        } else {
            character.getDictionaryEntries().addAll(entries);
        }
    }
}
