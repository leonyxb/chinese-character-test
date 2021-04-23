package com.xubo.application.panel;

import com.xubo.application.ApplicationConfig;
import com.xubo.application.ApplicationUtils;
import com.xubo.application.ApplicationMainFrame;
import com.xubo.core.TestEngine;
import com.xubo.data.book.Lesson;
import com.xubo.data.character.Character;
import com.xubo.data.character.TestStatus;
import com.xubo.data.character.CharacterTestRecord;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.xubo.application.ApplicationUtils.oneRecordPerDay;

public class CharactersTestPanel extends JPanel {

    public static final int MAX_WORDS_DISPLAYED = 8;

    private TestEngine testEngine;

    private ApplicationMainFrame mainFrame;

    private ApplicationConfig config;

    private JTextPane characterPane = new JTextPane();
    private JList<String> wordsList = new JList<>();
    private JTextArea correctCharactersArea = new JTextArea();
    private JTextArea incorrectCharactersArea = new JTextArea();
    private JTextArea statisticArea = new JTextArea();
    private JList<String> testRecordList = new JList<>();

    private JButton knowButton = new JButton("认识");
    private JButton unknownButton = new JButton("不认识");
    private JButton learnButton = new JButton("学习");
    private JButton endButton = new JButton("结束测试");

    private boolean learn;

    public CharactersTestPanel(List<Lesson> lessons, boolean shuffle, boolean learn, boolean record, boolean unknownOnly, ApplicationMainFrame mainFrame) {

        this.testEngine = new TestEngine(lessons, shuffle, record, unknownOnly);
        this.mainFrame = mainFrame;
        this.config = mainFrame.getConfig();
        this.learn = learn;

        initGui();
        setActions();

        setNextCharacterToTest();
        updateStatistic();

    }

    private void initGui() {

        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;

        c.gridy = 0;
        c.weighty = 3;
        add(buildTestRow(), c);

        c.gridy = 1;
        c.weighty = 2;
        add(buildHistoryRow(), c);

    }

    private JPanel buildTestRow() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        //row 1
        c.gridy = 0;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridwidth = 1;
        c.weightx = 3;
        if (config.getLanguage() == ApplicationConfig.ApplicationLanguage.CHINESE) {
            panel.add(buildShowSubPanelVertical(), c);
        } else {
            panel.add(buildShowSubPanelHorizontal(), c);
        }

        c.gridx = 1;
        c.gridwidth = 1;
        c.weightx = 2;
        panel.add(buildActionSubPanel(), c);

        return panel;
    }

    private JPanel buildShowSubPanelHorizontal() {
        JPanel showPanel = new JPanel();

        showPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;

        //row 1
        c.gridy = 0;
        c.weighty = 1;
        characterPane.setEditable(false);
        characterPane.setFont(new Font(config.getFontName(), Font.PLAIN, 100));
        characterPane.setBackground(Color.LIGHT_GRAY);
        characterPane.setPreferredSize(new Dimension(500, 200));
        characterPane.setBorder(BorderFactory.createEmptyBorder(40, 5, 5, 5));
        StyledDocument doc = characterPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        showPanel.add(characterPane, c);

        //row 2
        c.gridy = 1;
        c.weighty = 1;

        c.fill = GridBagConstraints.BOTH;
        wordsList.setFont(new Font(config.getFontName(), Font.PLAIN, 50));
        wordsList.setBackground(new Color(149, 149, 149));
        wordsList.setVisibleRowCount(-1);
        JScrollPane scrollPane = new JScrollPane(wordsList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(500, 100));
        showPanel.add(scrollPane, c);

        showPanel.setPreferredSize(new Dimension(500, 300));
        return showPanel;
    }

    private JPanel buildShowSubPanelVertical() {
        JPanel showPanel = new JPanel();

        showPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //row 1
        c.gridy = 0;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridwidth = 1;
        c.weightx = 1;
        characterPane.setEditable(false);
        characterPane.setFont(new Font(config.getFontName(), Font.PLAIN, 350));
        characterPane.setBackground(Color.LIGHT_GRAY);
        characterPane.setPreferredSize(new Dimension(250, 250));

        StyledDocument doc = characterPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        showPanel.add(characterPane, c);

        c.gridx = 1;
        c.gridwidth = 1;
        c.weightx = 0.4;
        c.fill = GridBagConstraints.BOTH;
        wordsList.setFont(new Font(config.getFontName(), Font.PLAIN, 50));
        wordsList.setBackground(new Color(149, 149, 149));
        wordsList.setVisibleRowCount(-1);
        JScrollPane scrollPane = new JScrollPane(wordsList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(200, 400));
        showPanel.add(scrollPane, c);

        return showPanel;
    }

    private JPanel buildActionSubPanel() {

        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(Color.black);

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
        ac.weighty = 1;

        ac.gridx = 0;
        ac.weightx = 1;
        ac.gridwidth = 3;
        actionPanel.add(buildStatisticPanel(), ac);
        //actionPanel.add(new JPanel(), ac);


        //action row 3
        ac.gridy = 2;
        ac.weighty = 2;

        ac.gridx = 0;
        ac.weightx = 1;
        ac.gridwidth = 1;
        knowButton.setBackground(new Color(4, 73, 18));
        knowButton.setForeground(Color.WHITE);
        knowButton.setFont(new Font(config.getButtonFontName(), Font.PLAIN, 30));
        knowButton.setFocusPainted(false);
        actionPanel.add(knowButton, ac);

        ac.gridx = 1;
        ac.weightx = 1;
        ac.gridwidth = 1;
        unknownButton.setBackground(new Color(129, 15, 11));
        unknownButton.setForeground(Color.WHITE);
        unknownButton.setFont(new Font(config.getButtonFontName(), Font.PLAIN, 30));
        unknownButton.setFocusPainted(false);
        actionPanel.add(unknownButton, ac);

        ac.gridx = 2;
        ac.weightx = 1;
        ac.gridwidth = 1;
        learnButton.setBackground(new Color(215, 96, 14));
        learnButton.setForeground(Color.WHITE);
        learnButton.setFont(new Font(config.getButtonFontName(), Font.PLAIN, 30));
        learnButton.setFocusPainted(false);
        if (learn) {
            learnButton.setEnabled(true);
        } else {
            learnButton.setEnabled(false);
        }
        actionPanel.add(learnButton, ac);

        endButton.setFont(new Font(config.getButtonFontName(), Font.PLAIN, 30));

        return actionPanel;
    }

    private Panel buildStatisticPanel() {
        Panel panel = new Panel();
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(300, 200));

        testRecordList.setFont(new Font(config.getLabelFontName(), Font.PLAIN, 20));
        //testRecordList.setPreferredSize(new Dimension(100, 200));

        statisticArea.setFont(new Font(config.getLabelFontName(), Font.PLAIN, 25));
        statisticArea.setEditable(false);


        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        c.gridy = 0;
        c.gridheight = 1;
        c.weighty = 1;

        c.gridx = 0;
        c.gridwidth = 1;
        c.weightx = 2;
        panel.add(statisticArea, c);


        c.gridx = 1;
        c.gridwidth = 1;
        c.weightx = 2;
        JScrollPane scrollPane = new JScrollPane(testRecordList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(200, 300));

        panel.add(scrollPane, c);
        return panel;
    }

    private JPanel buildHistoryRow() {


        String fontName = config.getFontName();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));

        correctCharactersArea.setEditable(false);
        correctCharactersArea.setFont(new Font(fontName, Font.PLAIN, 30));
        correctCharactersArea.setLineWrap(true);
        correctCharactersArea.setWrapStyleWord(true);
        correctCharactersArea.setForeground(new Color(48, 143, 4));
        correctCharactersArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


        JScrollPane scrollPane1 = new JScrollPane(correctCharactersArea);
        scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane1.setPreferredSize(new Dimension(500, 150));
        panel.add(scrollPane1);


        incorrectCharactersArea.setEditable(false);
        incorrectCharactersArea.setFont(new Font(fontName, Font.PLAIN, 30));
        incorrectCharactersArea.setLineWrap(true);
        incorrectCharactersArea.setWrapStyleWord(true);
        incorrectCharactersArea.setForeground(Color.RED);
        incorrectCharactersArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JScrollPane scrollPane2 = new JScrollPane(incorrectCharactersArea);
        scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane2.setPreferredSize(new Dimension(500, 150));

        panel.add(scrollPane2);

        return panel;
    }

    private void setActions() {

        knowButton.addActionListener(e -> {
            testEngine.knowCurrentTestCharacter();
            updateStatistic();
            setNextCharacterToTest();
        });

        unknownButton.addActionListener(e -> {
            testEngine.doNotKnowCurrentTestCharacter();
            updateStatistic();
            setNextCharacterToTest();
        });

        learnButton.addActionListener(e -> {
            mainFrame.displayLearnDialog(testEngine.currentCharacter());
            testEngine.doNotKnowCurrentTestCharacter();
            updateStatistic();
            setNextCharacterToTest();
        });

        endButton.addActionListener(e -> {
            mainFrame.remove(this);
            mainFrame.displaySelectPanel();
        });
    }

    private void setNextCharacterToTest() {

        if (!testEngine.hasNextCharacter()) {
            performNoMoreCharacterToTestActions();
        }

        if (testEngine.hasNextCharacter()) {
            Character character = testEngine.nextCharacter();
            characterPane.setText(character.getText());
            ApplicationUtils.Colors displayedColors = ApplicationUtils.getDisplayedColors(character);
            characterPane.setBackground(displayedColors.getBackground());
            if (displayedColors == ApplicationUtils.Colors.EXCLUDED) {
                characterPane.setForeground(displayedColors.getForeground());
            } else {
                characterPane.setForeground(Color.BLACK);
            }
            characterPane.revalidate();
            wordsList.setListData(getWordsToDisplay(character));
            testRecordList.setListData(getTestRecordsToDisplay(character));
        }
    }


    private void performNoMoreCharacterToTestActions() {

        boolean stopTest = false;
        boolean hasNextRound = testEngine.prepareNextTestRound();

        if (hasNextRound) {
            int result = JOptionPane.showConfirmDialog(mainFrame,"测试完成，是否学习不认识的字?", "测试完成",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (result != 0) {
                stopTest = true;
            }
        } else {
            stopTest = true;
        }

        if (stopTest) {
            unknownButton.setEnabled(false);
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

    private String[] getWordsToDisplay(Character character) {
        List<String> words = character.getWords();
        if (config.getLanguage() == ApplicationConfig.ApplicationLanguage.CHINESE) {
            Collections.shuffle(words);
        }
        return words.stream().limit(MAX_WORDS_DISPLAYED).toArray(String[]::new);
    }


    private String[] getTestRecordsToDisplay(Character character) {

        List<String> history = new ArrayList<>();

        List<CharacterTestRecord> records = oneRecordPerDay(character.getTestRecord().getRecords());
        if (!records.isEmpty()) {
            history.add("<html><span style=\"font-size:18px;\">历史记录:</span></html>");
            records.stream()
                    .sorted()
                    .map(this::buildTestRecordHtml)
                    .forEach(history::add);
        }

        return history.toArray(new String[0]);

    }

    private String buildTestRecordHtml(CharacterTestRecord testRecord) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        ApplicationUtils.Colors colors = testRecord.getStatus() == TestStatus.KNOWN ? ApplicationUtils.Colors.KNOWN : ApplicationUtils.Colors.UNKNOWN;
        String cssColor = ApplicationUtils.getCssColor(colors.getForeground());
        builder.append(String.format("<span style=\" color:%s; font-size:18px; \">", cssColor));
        builder.append(format.format(testRecord.getDate()));
        builder.append("</span>");
        builder.append("</html>");
        return builder.toString();
    }

}
