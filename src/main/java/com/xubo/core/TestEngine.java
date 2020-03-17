package com.xubo.core;

import com.xubo.data.character.Character;
import com.xubo.data.character.CharacterStatus;
import com.xubo.data.book.Lesson;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class TestEngine {

    List<Character> characters;

    Iterator<Character> testCharactersIterator;

    Character currentTestCharacter;

    boolean shuffle;
    boolean record;

    public TestEngine(List<Lesson> lessons, boolean shuffle, boolean record) {

        Collections.reverse(lessons);
        this.characters = lessons.stream()
                .flatMap(lesson -> lesson.getCharacters().stream())
                .collect(toList());

        this.characters.forEach(character -> character.setStatus(CharacterStatus.NOT_TESTED));

        this.shuffle = shuffle;
        this.record = record;

        prepareNextTestRound();

    }

    public boolean prepareNextTestRound() {
        this.testCharactersIterator = getTestCharactersIterator();
        return testCharactersIterator.hasNext();
    }


    public Character nextCharacter() {
        this.currentTestCharacter = testCharactersIterator.next();
        return this.currentTestCharacter;
    }

    public Character currentCharacter() {
        return this.currentTestCharacter;
    }

    private Iterator<Character> getTestCharactersIterator() {
        List<Character> wordsToTest = characters.stream()
                .filter(character -> character.getStatus() == CharacterStatus.NOT_TESTED || character.getStatus() == CharacterStatus.UNKNOWN)
                .collect(toList());

        if (shuffle) {
            Collections.shuffle(wordsToTest);
        }
        return wordsToTest.iterator();
    }


    public boolean hasNextCharacter() {
        return testCharactersIterator.hasNext();
    }

    public void knowCurrentTestCharacter() {
        if (currentTestCharacter.getStatus() == CharacterStatus.NOT_TESTED) {
            currentTestCharacter.setStatus(CharacterStatus.KNOWN);
            if (record) {
                currentTestCharacter.addNewRecord(CharacterStatus.KNOWN);
            }
        } else {
            currentTestCharacter.setStatus(CharacterStatus.LEARNED);
        }
    }

    public void doNotKnowCurrentTestCharacter() {
        if (currentTestCharacter.getStatus() == CharacterStatus.NOT_TESTED) {
            if (record) {
                currentTestCharacter.addNewRecord(CharacterStatus.UNKNOWN);
            }
        }
        currentTestCharacter.setStatus(CharacterStatus.UNKNOWN);
    }


    public String statistic() {
        StringBuilder message = new StringBuilder();

        Map<CharacterStatus, List<Character>> groupedWords = characters.stream()
                .collect(groupingBy(Character::getStatus));

        int knowWordsNum = groupedWords.containsKey(CharacterStatus.KNOWN) ? groupedWords.get(CharacterStatus.KNOWN).size() : 0;
        int unknowWordsNum = groupedWords.containsKey(CharacterStatus.UNKNOWN) ? groupedWords.get(CharacterStatus.UNKNOWN).size() : 0;
        int learnedNum = groupedWords.containsKey(CharacterStatus.LEARNED) ? groupedWords.get(CharacterStatus.LEARNED).size() : 0;

        int alreadyTested = knowWordsNum + unknowWordsNum + learnedNum;

        message.append("测试进度 " + (alreadyTested + 1) + "/" + characters.size() + "\n");
        message.append("\n");
        message.append("  - 认识   " + knowWordsNum + " 字\n");
        message.append("  - 不认识 " + unknowWordsNum + " 字\n");
        message.append("  - 通过学习认识 " + learnedNum + " 字\n");
        message.append("\n");

        return message.toString();
    }

    public List<String> getKnownCharacters() {
        return characters.stream()
                .filter(character -> character.getStatus() == CharacterStatus.KNOWN || character.getStatus() == CharacterStatus.LEARNED)
                .map(Character::getText)
                .collect(toList());
    }

    public List<String> getUnknownCharacters() {
        return characters.stream()
                .filter(character -> character.getStatus() == CharacterStatus.UNKNOWN)
                .map(Character::getText)
                .collect(toList());
    }

    public String getTestResultSymbol() {
        return "\uD83D\uDC4D";
    }
}
