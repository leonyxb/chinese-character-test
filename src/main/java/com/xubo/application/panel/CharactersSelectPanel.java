package com.xubo.application.panel;

import com.xubo.application.ChineseMainFrame;
import com.xubo.data.ChineseData;
import com.xubo.data.book.Book;
import com.xubo.data.book.Lesson;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.xubo.application.ChineseMainFrame.FONT_NAME;
import static java.util.stream.Collectors.toList;


public class CharactersSelectPanel extends JPanel {

    ChineseMainFrame mainFrame;

    JButton addButton = new JButton("添加");
    JButton removeButton = new JButton("移除");
    JLabel selectInfoLabel = new JLabel();
    JButton startButton = new JButton("开始测试");
    JCheckBox randomCheckbox = new JCheckBox("打乱顺序");
    JCheckBox learnCheckbox = new JCheckBox("可以学习");

    JList<Book> bookList = new JList<>();

    JList<Lesson> lessonList = new JList<>();

    DefaultListModel<SelectedLesson> selectedLessons = new DefaultListModel<>();
    JList<SelectedLesson> selectedList = new JList<>(selectedLessons);

    public CharactersSelectPanel(ChineseData data, ChineseMainFrame mainFrame) {
        this.mainFrame = mainFrame;

        initGui();
        setActions();

        Book[] options = data.getBooks().toArray(new Book[0]);

        bookList.setListData(options);
        bookList.setVisibleRowCount(options.length);
        bookList.setSelectedIndex(0);

        updateStatistic();
    }

    private void setActions() {

        bookList.addListSelectionListener(e -> {
            JList<Book>  source = (JList<Book>) e.getSource();
            Book book = source.getSelectedValue();

            Lesson[] lessons = book.getLessons().toArray(new Lesson[0]);
            lessonList.setListData(lessons);
            lessonList.setVisibleRowCount(lessons.length);
            lessonList.setSelectedIndex(0);
        });

        addButton.addActionListener(e -> {
            lessonList.getSelectedValuesList().forEach(lesson -> {
                SelectedLesson selectedLesson = new SelectedLesson(lesson);
                if (!selectedLessons.contains(selectedLesson)) {
                    selectedLessons.add(0, selectedLesson);
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
                mainFrame.launchTest(lessons, randomCheckbox.isSelected(), learnCheckbox.isSelected());
            }
        });
    }


    private void initGui() {

        this.setLayout(new GridBagLayout());

        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookList.setFont(new Font(FONT_NAME, Font.PLAIN, 20));

        lessonList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lessonList.setFont(new Font(FONT_NAME, Font.PLAIN, 20));

        selectedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        selectedList.setVisibleRowCount(-1);
        selectedList.setFont(new Font(FONT_NAME, Font.PLAIN, 20));

        selectInfoLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
        randomCheckbox.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
        randomCheckbox.setSelected(true);
        learnCheckbox.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
        learnCheckbox.setSelected(true);

        GridBagConstraints c = new GridBagConstraints();
        //row 1
        c.gridy = 0;
        c.weighty = 1;

        c.gridx = 0;
        c.gridwidth = 3;
        c.weightx = 3;
        c.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane = new JScrollPane(bookList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        add(scrollPane, c);

        c.gridx = 3;
        c.gridwidth = 7;
        c.weightx = 7;
        c.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane2 = new JScrollPane(lessonList);
        scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane2.setPreferredSize(new Dimension(700, 300));
        add(scrollPane2, c);

        //row 2
        c.gridy = 1;
        c.weighty = 0.1;

        c.gridx = 0;
        c.gridwidth = 6;
        c.weightx = 6;
        c.fill = GridBagConstraints.BOTH;
        JPanel tmp = new JPanel();

        JLabel guideLabel = new JLabel("请从上方的<书本|课文>列表中\n选择汉字到下方列表");
        guideLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
        guideLabel.setForeground(new Color(87, 81, 80, 181));
        tmp.add(guideLabel);
        add(tmp, c);


        c.gridx = 6;
        c.gridwidth = 2;
        c.weightx = 2;
        c.fill = GridBagConstraints.BOTH;
        add(addButton, c);

        c.gridx = 8;
        c.gridwidth = 2;
        c.weightx = 2;
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
        add(new JPanel(), c);

        c.gridx = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        tmp = new JPanel();
        tmp.add(randomCheckbox);
        add(tmp, c);

        c.gridx = 2;
        c.gridwidth = 2;
        c.weightx = 2;
        c.fill = GridBagConstraints.BOTH;
        tmp = new JPanel();
        tmp.add(learnCheckbox);
        add(tmp, c);


        c.gridx = 4;
        c.gridwidth = 2;
        c.weightx = 2;
        c.fill = GridBagConstraints.BOTH;
        add(new JPanel(), c);


        c.gridx = 6;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        add(new JPanel(), c);

        c.gridx = 7;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        JPanel selectPanel = new JPanel();
        selectPanel.add(selectInfoLabel);
        add(selectPanel, c);

        c.gridx = 8;
        c.gridwidth = 2;
        c.weightx = 2;
        c.fill = GridBagConstraints.BOTH;
        add(startButton, c);

    }

    private void updateStatistic() {

        int sum = Collections.list(selectedLessons.elements()).stream()
                .mapToInt(selectedLesson -> (selectedLesson.getLesson().getCharacters().size()))
                .sum();

        selectInfoLabel.setText("总计 " + sum + " 字");
    }


    private static class SelectedLesson {

        private Lesson lesson;

        public SelectedLesson(Lesson lesson) {
            this.lesson = lesson;
        }

        public Lesson getLesson() {
            return lesson;
        }

        @Override
        public String toString() {
            return "[" + lesson.getParentBook().getTitle() + "] " +  lesson.toString();
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
}
