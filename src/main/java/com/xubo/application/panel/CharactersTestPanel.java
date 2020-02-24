package com.xubo.application.panel;

import com.xubo.application.ApplicationUtils;
import com.xubo.application.ChineseMainFrame;
import com.xubo.core.TestEngine;
import com.xubo.data.book.Lesson;
import com.xubo.data.character.Character;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.List;

import static com.xubo.application.ChineseMainFrame.FONT_NAME;


public class CharactersTestPanel extends JPanel {

    private TestEngine testEngine;

    private ChineseMainFrame mainFrame;

    private JTextPane characterPane = new JTextPane();
    private JTextArea correctCharactersArea = new JTextArea();
    private JTextArea incorrectCharactersArea = new JTextArea();
    private JTextArea statisticArea = new JTextArea();

    private JButton knowButton = new JButton("认识");
    private JButton unknowButton = new JButton("不认识");
    private JButton learnButton = new JButton("学习");
    private JButton endButton = new JButton("结束测试");

    private boolean learn;


    public CharactersTestPanel(List<Lesson> lessons, boolean shuffle, boolean learn, boolean record, ChineseMainFrame mainFrame) {

        this.testEngine = new TestEngine(lessons, shuffle, record);
        this.mainFrame = mainFrame;
        this.learn = learn;

        initGui();
        setActions();

        setNextCharacterToTest();
        updateStatistic();

    }

    private void initGui() {
        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        //row 1
        c.gridy = 0;

        c.gridx = 0;
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 2;
        c.fill = GridBagConstraints.BOTH;
        characterPane.setEditable(false);
        characterPane.setFont(new Font(FONT_NAME, Font.PLAIN, 350));
        characterPane.setBackground(Color.LIGHT_GRAY);
        characterPane.setPreferredSize(new Dimension(250, 250));

        StyledDocument doc = characterPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        add(characterPane, c);

        c.gridx = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 2;
        c.fill = GridBagConstraints.BOTH;
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(Color.black);
        add(actionPanel, c);

        //row 2
        c.gridy = 1;

        c.gridx = 0;
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        correctCharactersArea.setEditable(false);
        correctCharactersArea.setFont(new Font(FONT_NAME, Font.PLAIN, 24));
        correctCharactersArea.setLineWrap(true);
        correctCharactersArea.setWrapStyleWord(false);
        correctCharactersArea.setForeground(new Color(48, 143, 4));


        JScrollPane scrollPane1 = new JScrollPane(correctCharactersArea);
        scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane1.setPreferredSize(new Dimension(500, 150));
        add(scrollPane1, c);


        c.gridx = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        incorrectCharactersArea.setEditable(false);
        incorrectCharactersArea.setFont(new Font(FONT_NAME, Font.PLAIN, 24));
        incorrectCharactersArea.setLineWrap(true);
        incorrectCharactersArea.setWrapStyleWord(false);
        incorrectCharactersArea.setForeground(Color.RED);
        JScrollPane scrollPane2 = new JScrollPane(incorrectCharactersArea);
        scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane2.setPreferredSize(new Dimension(500, 150));

        add(scrollPane2, c);


        actionPanel.setLayout(new GridBagLayout());
        GridBagConstraints ac = new GridBagConstraints();
        ac.fill = GridBagConstraints.BOTH;

        //action row 1
        ac.gridy = 0;

        ac.gridx = 0;
        ac.weighty = 1;
        ac.weightx = 1;
        ac.gridwidth = 3;
        actionPanel.add(endButton, ac);



        //action row 2
        ac.gridy = 1;

        ac.gridx = 0;
        ac.weighty = 1;
        ac.weightx = 1;
        ac.gridwidth = 3;
        statisticArea.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
        statisticArea.setEditable(false);
        actionPanel.add(statisticArea, ac);

        //action row 3
        ac.gridy = 2;
        ac.weighty = 2;

        ac.gridx = 0;
        ac.weightx = 1;
        ac.gridwidth = 1;
        knowButton.setBackground(new Color(4, 73, 18));
        knowButton.setForeground(Color.WHITE);
        knowButton.setFont(new Font(FONT_NAME, Font.PLAIN, 30));
        actionPanel.add(knowButton, ac);

        ac.gridx = 1;
        ac.weightx = 1;
        ac.gridwidth = 1;
        unknowButton.setBackground(new Color(129, 15, 11));
        unknowButton.setForeground(Color.WHITE);
        unknowButton.setFont(new Font(FONT_NAME, Font.PLAIN, 30));
        actionPanel.add(unknowButton, ac);

        ac.gridx = 2;
        ac.weightx = 1;
        ac.gridwidth = 1;
        learnButton.setBackground(new Color(215, 96, 14));
        learnButton.setForeground(Color.WHITE);
        learnButton.setFont(new Font(FONT_NAME, Font.PLAIN, 30));
        if (learn) {
            learnButton.setEnabled(true);
        } else {
            learnButton.setEnabled(false);
        }
        actionPanel.add(learnButton, ac);

    }

    private void setActions() {

        knowButton.addActionListener(e -> {
            testEngine.knowCurrentTestCharacter();
            updateStatistic();
            setNextCharacterToTest();
        });

        unknowButton.addActionListener(e -> {
            testEngine.doNotKnowCurrentTestCharacter();
            updateStatistic();
            setNextCharacterToTest();
        });

        learnButton.addActionListener(e -> {
            mainFrame.showCharacterDetail(testEngine.currentCharacter());
            testEngine.doNotKnowCurrentTestCharacter();
            updateStatistic();
            setNextCharacterToTest();
        });

        endButton.addActionListener(e -> {
            mainFrame.remove(this);
            mainFrame.selectCharacters();
        });
    }

    private void setNextCharacterToTest() {

        if (!testEngine.hasNextCharacter()) {
            performNoMoreCharacterToTestActions();
        }

        if (testEngine.hasNextCharacter()) {
            Character character = testEngine.nextCharacter();
            characterPane.setText(character.getText());
            characterPane.setBackground(ApplicationUtils.getDisplayedColor(character, true));
        }
    }


    private void performNoMoreCharacterToTestActions() {

        boolean stopTest = false;
        boolean hasNextRound = testEngine.prepareNextTestRound();

        if (hasNextRound) {
            int result = JOptionPane.showConfirmDialog(mainFrame,"测试完成，是否重新测试不认识的字?", "测试完成",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (result != 0) {
                stopTest = true;
            }
        } else {
            stopTest = true;
        }

        if (stopTest) {
            unknowButton.setEnabled(false);
            knowButton.setEnabled(false);
            learnButton.setEnabled(false);
            characterPane.setText(testEngine.getTestResultSymbol());
        }
    }


    private void updateStatistic() {
        correctCharactersArea.setText(String.join(" ", testEngine.getKnownCharacters()));
        incorrectCharactersArea.setText(String.join(" ", testEngine.getUnknownCharacters()));
        statisticArea.setText(testEngine.statistic());
    }


}
