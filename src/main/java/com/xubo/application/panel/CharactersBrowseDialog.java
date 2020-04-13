package com.xubo.application.panel;

import com.xubo.application.ApplicationConfig;
import com.xubo.application.ApplicationUtils;
import com.xubo.data.book.Lesson;
import com.xubo.data.character.Character;
import com.xubo.data.dictionary.DictionaryEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CharactersBrowseDialog extends JDialog {

    private int boxHeight;
    private int boxWidth;
    private int boxNumPerRow;
    private int fontSize;

    private ApplicationConfig config;

    public CharactersBrowseDialog(List<Lesson> lessons, boolean shuffle, boolean unknownOnly, ApplicationConfig config, JFrame owner) {
        super(owner);

        this.config = config;
        if (config.getLanguage() == ApplicationConfig.ApplicationLanguage.CHINESE) {
            boxHeight = 100;
            boxWidth = 100;
            boxNumPerRow = 10;
            fontSize = 60;
        } else {
            boxHeight = 50;
            boxWidth = 200;
            boxNumPerRow = 5;
            fontSize = 24;
        }

        List<Character> characters = lessons.stream()
                .flatMap(lesson -> lesson.getCharacters().stream())
                .filter(character -> !unknownOnly || !ApplicationUtils.isKnown(character))
                .distinct()
                .collect(Collectors.toList());

        if (shuffle) {
            Collections.shuffle(characters);
        }

        display(characters);
    }

    private void display(List<Character> characters) {
        this.setSize(1000 + 25, 700);
        this.setLocationRelativeTo(null);
        this.setTitle("展示");
        this.setModal(true);
        this.setResizable(false);

        JPanel contextPanel = new JPanel();
        contextPanel.setLayout(null);
        contextPanel.setBackground(Color.GRAY);

        Dimension insideDimension = this.getContentPane().getSize();
        contextPanel.setPreferredSize(new Dimension(insideDimension.width, ((characters.size() - 1) / boxNumPerRow + 1) * boxHeight));
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
        int row = index / boxNumPerRow;
        int col = index % boxNumPerRow;
        button.setBounds(
                col * boxWidth,
                row * boxHeight,
                boxWidth,
                boxHeight
        );
        button.setFont(new Font(config.getFontName(), Font.PLAIN, fontSize));
        button.setBackground(Color.LIGHT_GRAY);
        button.setBackground(ApplicationUtils.getDisplayedColor(character, true));
        button.setFocusPainted(false);
        //button.setToolTipText(buildToolTipText(character));

        JPopupMenu menu = getPinyinMenu(character);

        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == e.BUTTON3) {
                    menu.show(button, e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == e.BUTTON1) {
                    Color currentColor = button.getBackground();
                    if (currentColor != ApplicationUtils.COLOR_KNOWN_BACKGROUND) {
                        button.setBackground(ApplicationUtils.COLOR_KNOWN_BACKGROUND);
                    } else {
                        button.setBackground(Color.LIGHT_GRAY);
                    }
                } else if (e.getButton() == e.BUTTON3) {
                    menu.setVisible(false);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        return button;
    }

    private JPopupMenu getPinyinMenu(Character character) {
        JPopupMenu menu = new JPopupMenu();
        character.getDictionaryEntries()
                .stream()
                .map(DictionaryEntry::getPinyin)
                .map(String::toLowerCase)
                .distinct()
                .forEach(py -> {
                    JMenuItem menuItem = new JMenuItem("  " + py);
                    menuItem.setFont(new Font("Arial", Font.PLAIN, 24));
                    menu.add(menuItem);
                });
        return menu;
    }

}
