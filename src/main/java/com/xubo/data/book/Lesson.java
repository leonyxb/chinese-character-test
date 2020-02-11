package com.xubo.data.book;

import com.xubo.data.character.Character;

import java.util.List;

public interface Lesson {

    String getTitle();

    List<Character> getCharacters();

    Book getParentBook();

}
