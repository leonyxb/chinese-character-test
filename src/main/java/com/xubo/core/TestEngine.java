package com.xubo.core;

import com.xubo.application.ApplicationUtils;
import com.xubo.data.character.Character;
import com.xubo.data.character.TestStatus;
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

    boolean record;

    public TestEngine(List<Lesson> lessons, boolean shuffle, boolean record, boolean unknownOnly) {

        Collections.reverse(lessons);
        this.characters = lessons.stream()
                .flatMap(lesson -> lesson.getCharacters().stream())
                .distinct()
                .filter(character -> !unknownOnly || !ApplicationUtils.isKnown(character))
                .collect(toList());

        if (shuffle) {
            Collections.shuffle(this.characters);
        }


        this.characters.forEach(character -> character.setStatus(TestStatus.NOT_TESTED));


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
                .filter(character -> character.getStatus() == TestStatus.NOT_TESTED || character.getStatus() == TestStatus.UNKNOWN)
                .collect(toList());

        return wordsToTest.iterator();
    }


    public boolean hasNextCharacter() {
        return testCharactersIterator.hasNext();
    }

    public void knowCurrentTestCharacter() {
        if (currentTestCharacter.getStatus() == TestStatus.NOT_TESTED) {
            currentTestCharacter.setStatus(TestStatus.KNOWN);
            if (record) {
                currentTestCharacter.addNewRecord(TestStatus.KNOWN);
            }
        } else {
            currentTestCharacter.setStatus(TestStatus.LEARNED);
        }
    }

    public void doNotKnowCurrentTestCharacter() {
        if (currentTestCharacter.getStatus() == TestStatus.NOT_TESTED) {
            if (record) {
                currentTestCharacter.addNewRecord(TestStatus.UNKNOWN);
            }
        }
        currentTestCharacter.setStatus(TestStatus.UNKNOWN);
    }


    public String statistic() {
        StringBuilder message = new StringBuilder();

        Map<TestStatus, List<Character>> groupedWords = characters.stream()
                .collect(groupingBy(Character::getStatus));

        int knowWordsNum = groupedWords.containsKey(TestStatus.KNOWN) ? groupedWords.get(TestStatus.KNOWN).size() : 0;
        int unknowWordsNum = groupedWords.containsKey(TestStatus.UNKNOWN) ? groupedWords.get(TestStatus.UNKNOWN).size() : 0;
        int learnedNum = groupedWords.containsKey(TestStatus.LEARNED) ? groupedWords.get(TestStatus.LEARNED).size() : 0;

        int alreadyTested = knowWordsNum + unknowWordsNum + learnedNum;
        int currentIndex = alreadyTested == characters.size() ? alreadyTested : alreadyTested + 1;

        message.append("测试进度 " + currentIndex + "/" + characters.size() + "\n");
        message.append("\n");
        message.append("  - 认识   " + knowWordsNum + " 字\n");
        message.append("  - 不认识 " + unknowWordsNum + " 字\n");
        message.append("  - 通过学习认识 " + learnedNum + " 字\n");
        message.append("\n");

        return message.toString();
    }

    public List<String> getKnownCharacters() {
        return characters.stream()
                .filter(character -> character.getStatus() == TestStatus.KNOWN || character.getStatus() == TestStatus.LEARNED)
                .map(Character::getDisplayText)
                .collect(toList());
    }

    public List<String> getUnknownCharacters() {
        return characters.stream()
                .filter(character -> character.getStatus() == TestStatus.UNKNOWN)
                .map(Character::getDisplayText)
                .collect(toList());
    }

    public String getTestResultSymbol() {
        return "\uD83D\uDC4D";
    }
}
