package com.xubo.data.word;

import com.xubo.utils.ChineseResourceReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class WordsRepository {

    private static final Logger logger = LogManager.getLogger(WordsRepository.class);

    private Map<String, Set<String>> words = new HashMap<>();

    public WordsRepository() {

        logger.info("载入小学词汇...");

        List<String>  rawLines = new ArrayList<>();

        rawLines.addAll(ChineseResourceReader.readLinesFromResources("/words/沪教版小学1-5年级词语表.txt", StandardCharsets.UTF_8.toString()));
        rawLines.addAll(ChineseResourceReader.readLinesFromResources("/words/人教版小学语文一至六年级生字词语汇总.txt", StandardCharsets.UTF_8.toString()));
        rawLines.addAll(ChineseResourceReader.readLinesFromResources("/words/手动添加.txt", StandardCharsets.UTF_8.toString()));

        List<String> wordsFound = rawLines.stream()
                .map(String::trim)
                .filter(line -> !line.startsWith("#"))
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .map(String::trim)
                .filter(WordsRepository::isValidWord)
                .distinct()
                .collect(Collectors.toList());

        logger.info("    成功载入" + wordsFound.size() + " 个小学词汇.");

        wordsFound.forEach(word -> {
            word.chars().forEach(c -> {
                String key = String.valueOf((char) c);
                this.words.putIfAbsent(key, new LinkedHashSet<>());
                this.words.get(key).add(word);
            });
        });

    }

    public Set<String> getWords(String character) {
        return words.get(character);
    }

    private static boolean isValidWord(String word) {
        if (word.equals("")) {
            return false;
        }

        try {
            Integer.valueOf(word);
            return false;
        } catch (NumberFormatException e) {

        }

        return true;
    }
}
