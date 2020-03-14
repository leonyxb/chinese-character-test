package com.xubo.application.panel;

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
    
    Color knowColor = new Color(142, 196, 142);

    private List<Lesson> lessons;

    public CharactersDisplayDialog(List<Lesson> lessons, boolean shuffle, JFrame owner) {
        super(owner);
        this.lessons = lessons;

        this.setSize(1000 + 25, 600);
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

        Dimension insideDimension = this.getContentPane().getSize();
        int squareSideLength = 100;

        if (shuffle) {
            Collections.shuffle(characters);
        }

        contextPanel.setPreferredSize(new Dimension(insideDimension.width, (characters.size() / 10 + 1) * squareSideLength));
        for (int i = 0; i < characters.size(); i++) {
            Character c = characters.get(i);
            JButton button = new JButton(c.getText());
            int row = i / 10;
            int col = i % 10; 
            button.setBounds(col * squareSideLength, row * squareSideLength, squareSideLength, squareSideLength);
            button.setFont(new Font(FONT_NAME, Font.PLAIN, 60));
            button.setBackground(Color.LIGHT_GRAY);
            button.setFocusPainted(false);
            button.addActionListener(e -> {
                Color currentColor = button.getBackground();
                if (currentColor != knowColor) {
                    button.setBackground(knowColor);
                } else {
                    button.setBackground(Color.LIGHT_GRAY);
                }
            });
            button.setToolTipText(buildToolTipText(c));
            contextPanel.add(button);
        }
        
        

        JScrollPane scrollPane = new JScrollPane(contextPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        this.add(scrollPane);
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
