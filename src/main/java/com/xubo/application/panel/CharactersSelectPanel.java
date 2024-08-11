package com.xubo.application.panel;

import com.xubo.application.ApplicationConfig;
import com.xubo.application.ApplicationUtils;
import com.xubo.application.ApplicationMainFrame;
import com.xubo.data.DataSource;
import com.xubo.data.book.Book;
import com.xubo.data.book.Lesson;
import com.xubo.data.book.common.InMemoryBook;
import com.xubo.data.character.Character;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;


public class CharactersSelectPanel extends JPanel {

    private ApplicationMainFrame mainFrame;
    private ApplicationConfig config;

    private JButton historyButton = new JButton("历史");
    private JButton addButton = new JButton("添加");
    private JButton removeAllButton = new JButton("清空");
    private JButton startButton = new JButton("测试");
    private JButton displayButton = new JButton("浏览");

    private JLabel selectInfoLabel = new JLabel();
    private JLabel totalNumLabel = new JLabel();
    private JLabel archiveNumLabel = new JLabel();
    private JLabel knownNumLabel = new JLabel();
    private JLabel retestNumLabel = new JLabel();
    private JLabel excludedNumLabel = new JLabel();
    private JCheckBox randomCheckbox = new JCheckBox("打乱测试顺序");
    private JCheckBox learnCheckbox = new JCheckBox("允许学习");
    private JCheckBox testUnknownOnlyCheckbox = new JCheckBox("仅测试不认识的字");

    private JCheckBox recordCheckbox = new JCheckBox("记录本次测试");

    private JList<Book> bookList = new JList<>();

    private JList<DisplayedLesson> lessonList = new JList<>();

    private DefaultListModel<SelectedLesson> selectedLessons = new DefaultListModel<>();
    private JList<SelectedLesson> selectedList = new JList<>(selectedLessons);

    public CharactersSelectPanel(DataSource data, ApplicationMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.config = mainFrame.getConfig();

        initGui();
        setActions();
        fillData(data);
        updateStatistic();
    }

    private void fillData(DataSource data) {

        Map<ApplicationUtils.Colors, List<Character>> colorsListMap = data.getBooks().stream()
                .flatMap(book -> book.getLessons().stream())
                .flatMap(lesson -> lesson.getCharacters().stream())
                .distinct()
                .collect(groupingBy(ApplicationUtils::getDisplayedColors));

        long knownNum = colorsListMap.getOrDefault(ApplicationUtils.Colors.KNOWN, Collections.emptyList()).size();
        long archiveNum = colorsListMap.getOrDefault(ApplicationUtils.Colors.ARCHIVED, Collections.emptyList()).size();
        long excludedNum = colorsListMap.getOrDefault(ApplicationUtils.Colors.EXCLUDED, Collections.emptyList()).size();
        long retestNum = colorsListMap.getOrDefault(ApplicationUtils.Colors.NEED_RETEST, Collections.emptyList()).size();

        totalNumLabel.setText("总识字数：" + (knownNum + archiveNum + excludedNum + retestNum));
        archiveNumLabel.setText("长期记忆：" +  archiveNum);
        knownNumLabel.setText("临时记忆：" +  knownNum);
        knownNumLabel.setText("临时记忆：" +  knownNum);
        excludedNumLabel.setText("永久：" +  excludedNum);
        retestNumLabel.setText("提醒：" +  retestNum);

        List<Book> books = new ArrayList<>();

        if (config.getLanguage() == ApplicationConfig.ApplicationLanguage.CHINESE) {
            addBook(colorsListMap, books, ApplicationUtils.Colors.NEED_RETEST, "<自动> 有没有忘掉？", 10);
            addBook(colorsListMap, books, ApplicationUtils.Colors.ALMOST_KNOWN, "<自动> 就要学会啦！", 10);
            addBook(colorsListMap, books, ApplicationUtils.Colors.ALMOST_UNKNOWN, "<自动> 再试一试吧！", 10);
            addBook(colorsListMap, books, ApplicationUtils.Colors.UNKNOWN, "<自动> 好难记住啊！", 10);
            addBook(colorsListMap, books, ApplicationUtils.Colors.EXCLUDED, "<自动> 不再自动测试！", 10);
        } else if (config.getLanguage() == ApplicationConfig.ApplicationLanguage.FRENCH) {
            addBook(colorsListMap, books, ApplicationUtils.Colors.NEED_RETEST, "<auto> Presque oublié?", 5);
            addBook(colorsListMap, books, ApplicationUtils.Colors.ALMOST_KNOWN, "<auto> Bientôt l'apprendre!", 5);
            addBook(colorsListMap, books, ApplicationUtils.Colors.ALMOST_UNKNOWN, "<auto> Réessayer encore?", 5);
            addBook(colorsListMap, books, ApplicationUtils.Colors.UNKNOWN, "<auto> Difficile à retenir", 5);
            addBook(colorsListMap, books, ApplicationUtils.Colors.EXCLUDED, "<auto> Ne test plus", 10);
        } else if (config.getLanguage() == ApplicationConfig.ApplicationLanguage.ENGLISH) {
            addBook(colorsListMap, books, ApplicationUtils.Colors.NEED_RETEST, "<auto> Almost forget?", 5);
            addBook(colorsListMap, books, ApplicationUtils.Colors.ALMOST_KNOWN, "<auto> Almost known!", 5);
            addBook(colorsListMap, books, ApplicationUtils.Colors.ALMOST_UNKNOWN, "<auto> Try again?", 5);
            addBook(colorsListMap, books, ApplicationUtils.Colors.UNKNOWN, "<auto> Hard to remember!", 5);
            addBook(colorsListMap, books, ApplicationUtils.Colors.EXCLUDED, "<auto> No more test!", 10);
        } else if (config.getLanguage() == ApplicationConfig.ApplicationLanguage.GERMANY) {
            addBook(colorsListMap, books, ApplicationUtils.Colors.NEED_RETEST, "<auto> fast vergessen！", 5);
            addBook(colorsListMap, books, ApplicationUtils.Colors.ALMOST_KNOWN, "<auto> studieren", 5);
            addBook(colorsListMap, books, ApplicationUtils.Colors.ALMOST_UNKNOWN, "<auto> versuchen Sie es erneut?", 5);
            addBook(colorsListMap, books, ApplicationUtils.Colors.UNKNOWN, "<auto> schwer zu merken", 5);
            addBook(colorsListMap, books, ApplicationUtils.Colors.EXCLUDED, "<auto> Kein Testen mehr", 10);
        }

        data.getBooks().stream()
                .filter(Book::display).forEach(books::add);

        Book[] options =  books.toArray(new Book[0]);
        bookList.setListData(options);
        bookList.setVisibleRowCount(options.length);
        bookList.setSelectedIndex(0);

    }

    private void addBook(Map<ApplicationUtils.Colors, List<Character>> colorsListMap, List<Book> books, ApplicationUtils.Colors colors, String title, int charactersPerLesson) {
        List<Character> characters = colorsListMap.getOrDefault(colors, Collections.emptyList());
        if (characters.size() > 0) {
            books.add(new InMemoryBook(title, characters, charactersPerLesson));
        }
    }

    private void setActions() {

        bookList.addListSelectionListener(e -> {
            JList<Book>  source = (JList<Book>) e.getSource();
            Book book = source.getSelectedValue();


            DisplayedLesson[] lessons = book.getLessons().stream().map(DisplayedLesson::new).toArray(DisplayedLesson[]::new);
            lessonList.setListData(lessons);
            lessonList.setVisibleRowCount(lessons.length);
            lessonList.setSelectedIndex(0);
        });

        bookList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList<Book> source = (JList<Book>) evt.getSource();
                if (evt.getClickCount() >= 2) {
                    // Double-click detected
                    Book book = source.getSelectedValue();
                    book.getLessons().forEach(lesson -> {
                        SelectedLesson selectedLesson = new SelectedLesson(new DisplayedLesson(lesson));
                        addToTestList(selectedLesson);
                    });
                    updateStatistic();
                }
            }
        });

        lessonList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList<DisplayedLesson> source = (JList<DisplayedLesson>) evt.getSource();
                if (evt.getClickCount() >= 2) {
                    // Double-click detected
                    DisplayedLesson displayedLesson = source.getSelectedValue();
                    addToTestList(new SelectedLesson(displayedLesson));
                    updateStatistic();
                }
            }
        });

        selectedList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList<SelectedLesson> source = (JList<SelectedLesson>) evt.getSource();
                if (evt.getClickCount() >= 2) {
                    // Double-click detected
                    SelectedLesson selectedLesson = source.getSelectedValue();
                    selectedLessons.removeElement(selectedLesson);
                    updateStatistic();
                }
            }
        });


        addButton.addActionListener(e -> {
            lessonList.getSelectedValuesList().forEach(displayedLesson -> {
                SelectedLesson selectedLesson = new SelectedLesson(displayedLesson);
                addToTestList(selectedLesson);
            });
            updateStatistic();
        });

        removeAllButton.addActionListener(e -> {
            ((DefaultListModel) selectedList.getModel()).removeAllElements();
            updateStatistic();
        });

        startButton.addActionListener(e -> {

            List<Lesson> lessons = Collections.list(selectedLessons.elements()).stream()
                    .map(SelectedLesson::getLesson)
                    .collect(toList());

            if (!lessons.isEmpty()) {
                mainFrame.remove(this);
                mainFrame.displayTestPanel(
                        lessons,
                        randomCheckbox.isSelected(),
                        learnCheckbox.isSelected(),
                        recordCheckbox.isSelected(),
                        testUnknownOnlyCheckbox.isSelected()
                );
            }
        });

        displayButton.addActionListener(e -> {

            List<Lesson> lessons = Collections.list(selectedLessons.elements()).stream()
                    .map(SelectedLesson::getLesson)
                    .collect(toList());

            if (!lessons.isEmpty()) {
                mainFrame.displayBrowseDialog(lessons, randomCheckbox.isSelected(), testUnknownOnlyCheckbox.isSelected());
            }
        });

        historyButton.addActionListener(e -> {
            mainFrame.displayHistoryDialog();
        });
    }

    synchronized private void addToTestList(SelectedLesson selectedLesson) {
        if (!selectedLessons.contains(selectedLesson)) {
            selectedLessons.add(0, selectedLesson);
        }
    }


    private void initGui() {

        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridwidth = 1;
        c.weightx = 1;

        //row 1
        c.gridy = 0;
        c.weighty = 1.5;
        c.gridheight = 1;
        add(buildRow1(), c);

        //row 2
        c.gridy = 1;
        c.gridheight = 1;
        c.weighty = 0.1;
        add(buildRow2(), c);

        //row 3
        c.gridy = 2;
        c.weighty = 1;
        c.gridheight = 1;
        add(buildRow3(), c);

        //row 4
        c.gridy = 3;
        c.weighty = 0.1;
        c.gridheight = 1;
        add(buildRow4(), c);

    }

    private JPanel buildRow1() {

        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookList.setFont(new Font(config.getFontName(), Font.PLAIN, 25));

        lessonList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lessonList.setFont(new Font(config.getFontName(), Font.PLAIN, 25));

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(1000, 400));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.weighty = 1;

        c.gridx = 0;
        c.gridwidth = 1;
        c.weightx = 40;
        JScrollPane scrollPane = new JScrollPane(bookList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        panel.add(scrollPane, c);

        c.gridx = 1;
        c.gridwidth = 1;
        c.weightx = 60;
        JScrollPane scrollPane2 = new JScrollPane(lessonList);
        scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane2.setPreferredSize(new Dimension(700, 300));
        panel.add(scrollPane2, c);

        return panel;
    }

    private JPanel buildRow2() {

        totalNumLabel.setFont(new Font(config.getLabelFontName(), Font.PLAIN, 24));
        totalNumLabel.setBorder(BorderFactory.createEmptyBorder(0,10, 0, 0));

        archiveNumLabel.setFont(new Font(config.getLabelFontName(), Font.PLAIN, 24));
        archiveNumLabel.setForeground(new Color(0, 30, 111));

        knownNumLabel.setFont(new Font(config.getLabelFontName(), Font.PLAIN, 24));
        knownNumLabel.setForeground(new Color(0, 138, 0));

        excludedNumLabel.setFont(new Font(config.getLabelFontName(), Font.PLAIN, 24));
        excludedNumLabel.setForeground(new Color(104, 104, 104));

        retestNumLabel.setFont(new Font(config.getLabelFontName(), Font.PLAIN, 24));
        retestNumLabel.setForeground(ApplicationUtils.Colors.NEED_RETEST.getForeground());

        addButton.setFont(new Font(config.getButtonFontName(), Font.PLAIN, 25));
        addButton.setFocusPainted(false);
        addButton.setPreferredSize(new Dimension(400, 100));

        historyButton.setFont(new Font(config.getButtonFontName(), Font.PLAIN, 25));
        historyButton.setFocusPainted(false);
        historyButton.setPreferredSize(new Dimension(400, 100));

        removeAllButton.setFont(new Font(config.getButtonFontName(), Font.PLAIN, 25));
        removeAllButton.setFocusPainted(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.weighty = 1;

        c.gridx = 0;
        c.gridwidth = 1;
        c.weightx = 1;
        panel.add(historyButton, c);

        c.gridx = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        panel.add(totalNumLabel, c);

        c.gridx = 2;
        c.gridwidth = 1;
        c.weightx = 1;
        panel.add(excludedNumLabel, c);

        c.gridx = 3;
        c.gridwidth = 1;
        c.weightx = 1;
        panel.add(archiveNumLabel, c);

        c.gridx = 4;
        c.gridwidth = 1;
        c.weightx = 1;
        panel.add(knownNumLabel, c);

        c.gridx = 5;
        c.gridwidth = 1;
        c.weightx = 1;
        panel.add(retestNumLabel, c);

        c.gridx = 6;
        c.gridwidth = 1;
        c.weightx = 1;
        panel.add(new JPanel(), c);

        c.gridx = 7;
        c.gridwidth = 1;
        c.weightx = 1;
        panel.add(addButton, c);

        c.gridx = 8;
        c.gridwidth = 1;
        c.weightx = 1;
        panel.add(removeAllButton, c);

        return panel;
    }

    private JScrollPane buildRow3() {

        selectedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        selectedList.setVisibleRowCount(-1);
        selectedList.setFont(new Font(config.getFontName(), Font.PLAIN, 25));

        JScrollPane scrollPane3 = new JScrollPane(selectedList);
        scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane3.setPreferredSize(new Dimension(900, 500));
        return scrollPane3;
    }

    private JPanel buildRow4() {

        selectInfoLabel.setFont(new Font(config.getLabelFontName(), Font.PLAIN, 20));

        randomCheckbox.setFont(new Font(config.getLabelFontName(), Font.PLAIN, 25));
        randomCheckbox.setSelected(true);
        randomCheckbox.setFocusPainted(false);

        learnCheckbox.setFont(new Font(config.getLabelFontName(), Font.PLAIN, 25));
        learnCheckbox.setSelected(true);
        learnCheckbox.setFocusPainted(false);

        recordCheckbox.setFont(new Font(config.getLabelFontName(), Font.PLAIN, 25));
        recordCheckbox.setSelected(true);
        recordCheckbox.setFocusPainted(false);

        testUnknownOnlyCheckbox.setFont(new Font(config.getLabelFontName(), Font.PLAIN, 25));
        testUnknownOnlyCheckbox.setSelected(false);
        testUnknownOnlyCheckbox.setFocusPainted(false);


        startButton.setFont(new Font(config.getButtonFontName(), Font.PLAIN, 25));
        startButton.setFocusPainted(false);

        displayButton.setFont(new Font(config.getButtonFontName(), Font.PLAIN, 25));
        displayButton.setFocusPainted(false);


        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.weighty = 1;

        c.gridx = 0;
        c.gridwidth = 1;
        c.weightx = 1;
        panel.add(recordCheckbox, c);

        c.gridx = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        panel.add(randomCheckbox, c);

        c.gridx = 2;
        c.gridwidth = 1;
        c.weightx = 1;
        panel.add(learnCheckbox, c);

        c.gridx = 3;
        c.gridwidth = 1;
        c.weightx = 1;
        panel.add(testUnknownOnlyCheckbox, c);

        c.gridx = 4;
        c.gridwidth = 3;
        c.weightx = 3;
        panel.add(selectInfoLabel, c);

        c.gridx = 7;
        c.gridwidth = 1;
        c.weightx = 1;
        panel.add(displayButton, c);

        c.gridx = 8;
        c.gridwidth = 2;
        c.weightx = 2;
        panel.add(startButton, c);

        return panel;
    }

    private void updateStatistic() {

        List<Character> selected = Collections.list(selectedLessons.elements()).stream()
                .flatMap(lesson -> lesson.getLesson().getCharacters().stream())
                .distinct()
                .collect(toList());

        long knownNum = selected.stream()
                .filter(c -> !ApplicationUtils.isKnown(c))
                .count();

        selectInfoLabel.setText("<html>已选择 " + selected.size() + " 字<br/>其中有 " + knownNum + " 字还不认识</html>");

    }

    private static class SelectedLesson {

        private DisplayedLesson lesson;

        public SelectedLesson(DisplayedLesson lesson) {
            this.lesson = lesson;
        }

        public Lesson getLesson() {
            return lesson.getLesson();
        }

        @Override
        public String toString() {

            StringBuilder html = new StringBuilder();
            html.append("<html>");
            html.append("<span>[" + lesson.getLesson().getParentBook().getTitle() + "]&nbsp;</span>");
            html.append(lesson.getHtmlContent());
            html.append("</html>");

            return html.toString();

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SelectedLesson that = (SelectedLesson) o;
            return Objects.equals(lesson, that.lesson);
        }

        @Override
        public int hashCode() {
            return Objects.hash(lesson);
        }
    }

    private static class DisplayedLesson {

        private Lesson lesson;

        public Lesson getLesson() {
            return lesson;
        }

        public DisplayedLesson(Lesson lesson) {
            this.lesson = lesson;
        }

        public String getHtmlContent() {
            StringBuilder html = new StringBuilder();
            html.append("<span>&lt;" + lesson.getTitle() + "&gt;&nbsp;</span>");
            lesson.getCharacters().forEach(character -> {
                String cssColor = ApplicationUtils.getCssColor(ApplicationUtils.getDisplayedColor(character, false));
                html.append(String.format("<span style=\" color:%s; font-size:24px; \">&nbsp;%s&nbsp;</span>", cssColor, character.getText()));
            });

            return html.toString();
        }

        @Override
        public String toString() {
            return "<html>" + getHtmlContent() + "</html>";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DisplayedLesson that = (DisplayedLesson) o;
            return Objects.equals(lesson, that.lesson);
        }

        @Override
        public int hashCode() {
            return Objects.hash(lesson);
        }
    }

}
