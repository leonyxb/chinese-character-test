package com.xubo.application;

import com.xubo.application.panel.CharactersLearnDialog;
import com.xubo.application.panel.CharactersSelectPanel;
import com.xubo.application.panel.CharactersTestPanel;
import com.xubo.data.character.Character;
import com.xubo.data.ChineseData;
import com.xubo.data.book.Lesson;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChineseMainFrame extends JFrame {

    public static final String FONT_NAME = "楷体";

    ChineseData data;

    Map<Character, CharactersLearnDialog> characterDetails = new HashMap<>();

    public ChineseMainFrame(ChineseData data) {

        this.data = data;

        setTitle("识字测试");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        selectCharacters();
    }

    public void selectCharacters() {
        add(new CharactersSelectPanel(data, this));
        revalidate();
    }

    public void launchTest(List<Lesson> lessons, boolean shuffle, boolean learn) {
        add(new CharactersTestPanel(lessons, shuffle, learn,this));
        revalidate();
    }

    public void showCharacterDetail(Character character) {
        characterDetails.putIfAbsent(character, new CharactersLearnDialog(character, this));
        characterDetails.get(character).setVisible(true);
    }

}
