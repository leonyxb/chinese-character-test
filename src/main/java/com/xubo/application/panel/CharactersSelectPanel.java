package com.xubo.application.panel;

import com.xubo.application.ApplicationUtils;
import com.xubo.application.ChineseMainFrame;
import com.xubo.data.ChineseData;
import com.xubo.data.book.Book;
import com.xubo.data.book.Lesson;
import com.xubo.data.character.Character;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.xubo.application.ChineseMainFrame.FONT_NAME;
import static java.util.stream.Collectors.toList;


public class CharactersSelectPanel extends JPanel {

    private ChineseMainFrame mainFrame;

    private JButton addButton = new JButton("添加");
    private JButton removeButton = new JButton("移除");
    private JLabel selectInfoLabel = new JLabel();
    private JLabel totalNumLabel = new JLabel();
    private JButton startButton = new JButton("测试");
    private JButton displayButton = new JButton("浏览");
    private JCheckBox randomCheckbox = new JCheckBox("打乱测试顺序");
    private JCheckBox learnCheckbox = new JCheckBox("允许学习");
    private JCheckBox testKnownOnlyCheckbox = new JCheckBox("仅测试不认识的字");

    private JCheckBox recordCheckbox = new JCheckBox("记录本次测试");

    private JList<Book> bookList = new JList<>();

    private JList<DisplayedLesson> lessonList = new JList<>();

    private DefaultListModel<SelectedLesson> selectedLessons = new DefaultListModel<>();
    private JList<SelectedLesson> selectedList = new JList<>(selectedLessons);

    public CharactersSelectPanel(ChineseData data, ChineseMainFrame mainFrame) {
        this.mainFrame = mainFrame;

        initGui();
        setActions();

        Book[] options = data.getBooks().stream()
                .filter(Book::display)
                .toArray(Book[]::new);

        bookList.setListData(options);
        bookList.setVisibleRowCount(options.length);
        bookList.setSelectedIndex(0);

        long totalKnownNum = data.getBooks().stream()
                .flatMap(book -> book.getLessons().stream())
                .flatMap(lesson -> lesson.getCharacters().stream())
                .distinct()
                .filter(ApplicationUtils::isKnown)
                .count();

        totalNumLabel.setText("总识字数：" + totalKnownNum);

        updateStatistic();
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

        addButton.addActionListener(e -> {
            lessonList.getSelectedValuesList().forEach(displayedLesson -> {
                SelectedLesson selectedLesson = new SelectedLesson(displayedLesson);
                if (!selectedLessons.contains(selectedLesson)) {
                    selectedLessons.add(0, selectedLesson);
                    selectedLesson.lesson.getLesson().getCharacters().forEach(
                            c -> {
                                if (c.getWords().size() == 0) {
                                    System.out.println(c.getText() + " 没有关联词语");
                                }
                            }
                    );
                }
            });
            updateStatistic();
        });

        removeButton.addActionListener(e -> {
            selectedList.getSelectedValuesList().forEach(selectedLesson -> {
                selectedLessons.removeElement(selectedLesson);
            });
            updateStatistic();
        });

        startButton.addActionListener(e -> {

            List<Lesson> lessons = Collections.list(selectedLessons.elements()).stream()
                    .map(SelectedLesson::getLesson)
                    .collect(toList());

            if (!lessons.isEmpty()) {
                mainFrame.remove(this);
                mainFrame.launchTest(
                        lessons,
                        randomCheckbox.isSelected(),
                        learnCheckbox.isSelected(),
                        recordCheckbox.isSelected(),
                        testKnownOnlyCheckbox.isSelected()
                );
            }
        });

        displayButton.addActionListener(e -> {

            List<Lesson> lessons = Collections.list(selectedLessons.elements()).stream()
                    .map(SelectedLesson::getLesson)
                    .collect(toList());

            if (!lessons.isEmpty()) {
                mainFrame.showCharacters(lessons, randomCheckbox.isSelected());
            }
        });
    }


    private void initGui() {

        this.setLayout(new GridBagLayout());

        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookList.setFont(new Font(FONT_NAME, Font.PLAIN, 25));

        lessonList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lessonList.setFont(new Font(FONT_NAME, Font.PLAIN, 25));

        selectedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        selectedList.setVisibleRowCount(-1);
        selectedList.setFont(new Font(FONT_NAME, Font.PLAIN, 25));

        selectInfoLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 20));

        randomCheckbox.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
        randomCheckbox.setSelected(true);

        learnCheckbox.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
        learnCheckbox.setSelected(true);

        recordCheckbox.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
        recordCheckbox.setSelected(true);

        testKnownOnlyCheckbox.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
        testKnownOnlyCheckbox.setSelected(false);

        totalNumLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 24));
        totalNumLabel.setForeground(new Color(0, 138, 0));


        GridBagConstraints c = new GridBagConstraints();
        //row 1
        c.gridy = 0;
        c.weighty = 1;

        c.gridx = 0;
        c.gridwidth = 2;
        c.weightx = 2;
        c.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane = new JScrollPane(bookList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        add(scrollPane, c);

        c.gridx = 2;
        c.gridwidth = 8;
        c.weightx = 8;
        c.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane2 = new JScrollPane(lessonList);
        scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane2.setPreferredSize(new Dimension(700, 300));
        add(scrollPane2, c);

        //row 2
        c.gridy = 1;
        c.weighty = 0.1;

        c.gridx = 0;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        JPanel tmp = new JPanel();
        tmp.add(totalNumLabel);
        add(tmp, c);

        c.gridx = 1;
        c.gridwidth = 5;
        c.weightx = 5;
        c.fill = GridBagConstraints.BOTH;
        tmp = new JPanel();

        JLabel guideLabel = new JLabel("请从上方的<书本|课文>列表中\n选择汉字到下方列表");
        guideLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
        guideLabel.setForeground(new Color(87, 81, 80, 181));
        tmp.add(guideLabel);
        add(tmp, c);


        c.gridx = 6;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        add(new JPanel(), c);


        c.gridx = 7;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        add(addButton, c);

        c.gridx = 8;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        add(removeButton, c);


        //row 3
        c.gridy = 2;
        c.weighty = 1;

        c.gridx = 0;
        c.gridwidth = 10;
        c.weightx = 0.5;
        c.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane3 = new JScrollPane(selectedList);
        scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane3.setPreferredSize(new Dimension(900, 500));
        add(scrollPane3, c);

        //row 4
        c.gridy = 3;
        c.weighty = 0.1;

        c.gridx = 0;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        add(recordCheckbox, c);

        c.gridx = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        add(randomCheckbox, c);

        c.gridx = 2;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        add(learnCheckbox, c);

        c.gridx = 3;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        add(testKnownOnlyCheckbox, c);

        c.gridx = 4;
        c.gridwidth = 3;
        c.weightx = 3;
        c.fill = GridBagConstraints.BOTH;
        add(selectInfoLabel, c);

        c.gridx = 7;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        add(displayButton, c);

        c.gridx = 8;
        c.gridwidth = 2;
        c.weightx = 2;
        c.fill = GridBagConstraints.BOTH;
        add(startButton, c);

    }

    private void updateStatistic() {

        List<Character> selected = Collections.list(selectedLessons.elements()).stream()
                .flatMap(lesson -> lesson.getLesson().getCharacters().stream())
                .distinct()
                .collect(toList());

        long knownNum = selected.stream().filter(ApplicationUtils::isKnown).count();

        selectInfoLabel.setText("<html>已选择 " + selected.size() + " 字<br/>其中已认识 " + knownNum + " 字</html>");

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
                html.append(String.format("<span style=\" color:%s; font-size:20px; \">&nbsp;%s&nbsp;</span>", cssColor, character.getText()));
            });

            return html.toString();
        }

        @Override
        public String toString() {
            return "<html>" + getHtmlContent() + "</html>";
        }
    }

}
