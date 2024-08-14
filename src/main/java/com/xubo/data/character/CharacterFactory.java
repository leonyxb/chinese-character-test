package com.xubo.data.character;

import com.xubo.data.book.Lesson;
import com.xubo.data.dictionary.Dictionary;
import com.xubo.data.dictionary.DictionaryEntry;
import com.xubo.data.word.WordsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CharacterFactory {

    private static final Logger logger = LogManager.getLogger(CharacterFactory.class);

    private static Map<String, Character> characters = new HashMap<>();

    private static Dictionary dictionary;

    private static WordsRepository wordsRepository;

    public static void setDictionary(Dictionary dictionary) {
        CharacterFactory.dictionary = dictionary;
    }

    public static void setWordsRepository(WordsRepository wordsRepository) {
        CharacterFactory.wordsRepository = wordsRepository;
    }

    public static Character getCharacter(String text, String language, Lesson lesson) {
        Character c;
        if (characters.containsKey(text)) {
            c = characters.get(text);
        } else {
            c = new Character(text, language);
            characters.put(text, c);

            linkDictionary(c);
            linkWords(c);
        }

        if (lesson != null) {
            c.getLessons().add(lesson);
        }

        return c;
    }

    private static void linkDictionary(Character character) {
        if (dictionary != null) {
            List<DictionaryEntry> entries = dictionary.getEntries(character.getText());
            if (entries == null) {
                logger.warn("    无法找到词条：" + character.getText());
            } else {
                character.getDictionaryEntries().addAll(entries);
            }
        }
    }

    private static void linkWords(Character character) {
        if (wordsRepository != null) {
            Set<String> words = wordsRepository.getWords(character.getText());
            if (words != null) {
                character.getWords().addAll(words);
            }
        }
    }
}
