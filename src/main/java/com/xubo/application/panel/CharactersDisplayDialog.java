package com.xubo.application.panel;

import com.xubo.application.ApplicationUtils;
import com.xubo.data.book.Lesson;
import com.xubo.data.character.Character;
import com.xubo.data.dictionary.DictionaryEntry;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.xubo.application.ChineseMainFrame.FONT_NAME;

public class CharactersDisplayDialog extends JDialog {

    private static int SQUARE_SIDE_LENGTH = 100;

    public CharactersDisplayDialog(List<Lesson> lessons, boolean shuffle, JFrame owner) {
        super(owner);

        this.setSize(1000 + 25, 700);
        this.setLocationRelativeTo(null);
        this.setTitle("展示");
        this.setModal(true);
        this.setResizable(false);

        JPanel contextPanel = new JPanel();
        contextPanel.setLayout(null);
        contextPanel.setBackground(Color.GRAY);

        List<Character> characters = lessons.stream()
                .flatMap(lesson -> lesson.getCharacters().stream())
                .collect(Collectors.toList());

        if (shuffle) {
            Collections.shuffle(characters);
        }

        Dimension insideDimension = this.getContentPane().getSize();
        contextPanel.setPreferredSize(new Dimension(insideDimension.width, (characters.size() / 10 + 1) * SQUARE_SIDE_LENGTH));
        for (int i = 0; i < characters.size(); i++) {
            Character c = characters.get(i);
            JButton button = buildButton(i, c);
            contextPanel.add(button);
        }

        JScrollPane scrollPane = new JScrollPane(contextPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        this.add(scrollPane);
    }

    private JButton buildButton(int index, Character character) {
        JButton button = new JButton(character.getText());
        int row = index / 10;
        int col = index % 10;
        button.setBounds(
                col * SQUARE_SIDE_LENGTH,
                row * SQUARE_SIDE_LENGTH,
                SQUARE_SIDE_LENGTH,
                SQUARE_SIDE_LENGTH
        );
        button.setFont(new Font(FONT_NAME, Font.PLAIN, 60));
        button.setBackground(Color.LIGHT_GRAY);
        button.setBackground(ApplicationUtils.getDisplayedColor(character, true));
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            Color currentColor = button.getBackground();
            if (currentColor != ApplicationUtils.COLOR_KNOWN_BACKGROUND) {
                button.setBackground(ApplicationUtils.COLOR_KNOWN_BACKGROUND);
            } else {
                button.setBackground(Color.LIGHT_GRAY);
            }
        });
        button.setToolTipText(buildToolTipText(character));
        return button;
    }

    private String buildToolTipText(Character character) {
        List<String> lines = new ArrayList<>();
        lines.add("<html>");


        character.getDictionaryEntries()
                .stream()
                .map(DictionaryEntry::getPinyin)
                .map(String::toLowerCase)
                .distinct()
                .forEach(py -> {
                    lines.add("<p style=\" color:blue; font-size:20px; \">" + py +"</p>");
                });

        lines.add("</html>");
        return String.join("", lines);
    }
}
