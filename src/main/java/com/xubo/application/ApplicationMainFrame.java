package com.xubo.application;

import com.xubo.application.panel.*;
import com.xubo.data.DataSource;
import com.xubo.data.book.Lesson;
import com.xubo.data.character.Character;

import javax.swing.*;
import java.util.List;

public class ApplicationMainFrame extends JFrame {

    DataSource data;

    ApplicationConfig config;

    public ApplicationMainFrame(DataSource data, ApplicationConfig config) {

        this.data = data;
        this.config = config;

        setTitle("识字测试 版本： 多语言版");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        displaySelectPanel();
    }

    public void displaySelectPanel() {
        add(new CharactersSelectPanel(data, this));
        revalidate();
    }

    public void displayTestPanel(List<Lesson> lessons, boolean shuffle, boolean learn, boolean record, boolean unknownOnly) {
        add(new CharactersTestPanel(lessons, shuffle, learn,record, unknownOnly, this));
        revalidate();
    }

    public void displayLearnDialog(Character character) {
        CharactersLearnDialog dialog = new CharactersLearnDialog(character, this);
        dialog.setVisible(true);
    }

    public void displayBrowseDialog(List<Lesson> lessons, boolean shuffle, boolean unknownOnly) {
        CharactersBrowseDialog dialog = new CharactersBrowseDialog(lessons, shuffle, unknownOnly, this.config, this);
        dialog.setVisible(true);
    }

    public void displayHistoryDialog() {
        CharactersHistoryDialog dialog = new CharactersHistoryDialog(this.config, this);
        dialog.setVisible(true);
    }

    public ApplicationConfig getConfig() {
        return config;
    }
}
