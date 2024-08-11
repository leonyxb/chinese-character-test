package com.xubo.application;

import com.xubo.application.panel.FileTreePanel;
import com.xubo.data.DataSource;
import com.xubo.data.character.Character;
import com.xubo.data.character.CharacterFactory;
import com.xubo.data.dictionary.Dictionary;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class ApplicationReadingFrame extends JFrame {

    private static final Logger logger = LogManager.getLogger(ApplicationReadingFrame.class);

    private DataSource data;

    private ApplicationConfig config;

    private Path baseBooksDir;

    private JTextPane bookPane = new JTextPane();

    private FileTreePanel fileTree;

    private JLabel statisticLabel = new JLabel();

    private JButton coloringButton = new JButton("加颜色");

    private Map<String, List<String>> readingBooks = new HashMap<>();

    private Map<String, String> readingBooksStatistic = new HashMap<>();

    private Map<String, Color> colors = new HashMap<>();

    public ApplicationReadingFrame(DataSource data, ApplicationConfig config) {

        this.data = data;
        this.config = config;

        this.baseBooksDir = config.getResourceFolder().resolve("reading");
        this.fileTree = new FileTreePanel(this.baseBooksDir.toFile(), this::loadBook);

        initGui();
        fillData();
        initActions();
    }

    private void fillData() {

        List<Character> characters = data.getBooks().stream()
                .flatMap(book -> book.getLessons().stream())
                .flatMap(lesson -> lesson.getCharacters().stream())
                .distinct()
                .collect(Collectors.toList());

        characters.forEach(c ->
                colors.put(c.getText(), ApplicationUtils.getDisplayedColor(c, false))
        );

    }

    private void initGui() {

        setTitle("识字测试 版本：叶泉燐 豪华 VIP 99级 尊享 私人定制 1.0 版");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.gridheight = 1;
        c.weighty = 1;

        c.gridx = 0;
        c.weightx = 1;
        c.gridwidth = 1;

        fileTree.getTree().setFont(new Font(config.getFontName(), Font.PLAIN, 20));

        this.add(fileTree, c);

        c.gridx = 1;
        c.weightx = 5;
        c.gridwidth = 1;
        JPanel jPanel = buildCol2();
        jPanel.setPreferredSize(new Dimension(1000, 800));
        this.add(jPanel, c);
    }

    private JPanel buildCol2() {
        JPanel jPanel = new JPanel();

        jPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridwidth = 1;
        c.weightx = 1;

        //row 1
        c.gridy = 0;
        c.gridheight = 1;
        c.weighty = 1;
        jPanel.add(buildStatisticPanel(), c);

        //row 2
        c.gridy = 1;
        c.gridheight = 1;
        c.weighty = 30;


        bookPane.setFont(new Font(config.getFontName(), Font.PLAIN, 36));
        bookPane.setEditable(false);
        bookPane.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JScrollPane bookScrollPane = new JScrollPane(bookPane);
        bookScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        bookScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        bookScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        jPanel.add(bookScrollPane, c);

        return jPanel;
    }

    private JPanel buildStatisticPanel() {
        JPanel jPanel = new JPanel();

        jPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.gridheight = 1;
        c.weighty = 1;

        c.gridx = 0;
        c.weightx = 7;
        c.gridwidth = 1;

        statisticLabel.setFont(new Font(config.getFontName(), Font.PLAIN, 24));
        statisticLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jPanel.add(statisticLabel, c);

        c.gridx = 1;
        c.weightx = 1;
        c.gridwidth = 1;

        coloringButton.setPreferredSize(new Dimension(100, 10));
        coloringButton.setFont(new Font(config.getFontName(), Font.PLAIN, 24));
        coloringButton.setFocusPainted(false);
        coloringButton.setEnabled(false);

        jPanel.add(coloringButton, c);

        return jPanel;
    }

    private void initActions() {
        bookPane.setText("请在左边文件列表中选择要阅读的书籍");
        bookPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rightClickPosition = bookPane.viewToModel(new Point(e.getX() - 20, e.getY()));
                bookPane.setCaretPosition(rightClickPosition);
                    bookPane.select(rightClickPosition, rightClickPosition + 1);
                    String text = bookPane.getSelectedText();

                    JPopupMenu menu = ApplicationUtils.getPinyinMenu(CharacterFactory.getCharacter(text, null));
                    menu.show(bookPane, e.getX(), e.getY() + 20);

            }
        });
    }

    private void fillWithColoringText(String bookName, boolean checkSize) {

        bookPane.setEditable(true);
        List<String> characters = readingBooks.get(bookName);
        bookPane.setText("");
        appendToPane(bookPane, "", Color.black);

        if (checkSize && characters.size() > 20000) {
            bookPane.setText(StringUtils.join(characters, ""));
            coloringButton.setEnabled(true);
            for (ActionListener actionListener : coloringButton.getActionListeners()) {
                coloringButton.removeActionListener(actionListener);
            }

            coloringButton.addActionListener(e -> {
                fillWithColoringText(bookName, false);
            });

        } else {
            coloringButton.setEnabled(false);
            characters.forEach(c -> appendToPane(bookPane, c, colors.getOrDefault(c, Color.black)));
        }

        bookPane.setCaretPosition(0);
        bookPane.setEditable(false);
    }

    private void loadBook(File file) {

        if (file.isDirectory()) {
            return;
        }
        try {
            Path path = file.toPath();
            String bookName = FilenameUtils.removeExtension(path.getFileName().toString());

            if (!readingBooks.containsKey(bookName)) {

                List<String> characters = getCharacters(path);
                readingBooks.put(bookName, characters);
                readingBooksStatistic.put(bookName, analyze(bookName, characters));
                logger.info("成功载入: " + path.getFileName());
            }

            fillWithColoringText(bookName, true);
            statisticLabel.setText(readingBooksStatistic.get(bookName));

        } catch (IOException e) {
            logger.error("Error for loading file: " + file.getAbsolutePath(), e);
        }
    }

    private String analyze(String bookName, List<String> characters) {

        Dictionary dictionary = data.getDictionary();

        List<String> validCharacters = characters.stream()
                .filter(c -> dictionary.getEntries(c) != null)
                .collect(Collectors.toList());

        int validCharactersNum = validCharacters.size();

        List<String> uniqueList = validCharacters.stream().distinct().collect(Collectors.toList());

        long totalNum = uniqueList.size();

        Map<Color, List<String>> colorsListMap = uniqueList.stream()
                .collect(groupingBy(c -> colors.getOrDefault(c, Color.BLACK)));


        List<String> knownDistinct =   colorsListMap.getOrDefault(ApplicationUtils.Colors.KNOWN.getForeground(), Collections.emptyList());
        List<String> archiveDistinct = colorsListMap.getOrDefault(ApplicationUtils.Colors.ARCHIVED.getForeground(), Collections.emptyList());
        List<String> reTestDistinct =  colorsListMap.getOrDefault(ApplicationUtils.Colors.NEED_RETEST.getForeground(), Collections.emptyList());
        List<String> excludedDistinct =  colorsListMap.getOrDefault(ApplicationUtils.Colors.EXCLUDED.getForeground(), Collections.emptyList());


        long knownCharactersNum = validCharacters.stream()
                .filter(c -> knownDistinct.contains(c) || archiveDistinct.contains(c) || reTestDistinct.contains(c) || excludedDistinct.contains(c))
                .count();

        long knownPercent = knownCharactersNum * 100 / validCharactersNum;

        long knownNum = knownDistinct.size() + archiveDistinct.size() + reTestDistinct.size() + excludedDistinct.size();

        return String.format("<<%s>> 总字数 %d, 使用汉字 %d 个, 认识 %d 个 (覆盖率%d%%)", bookName, validCharactersNum, totalNum, knownNum, knownPercent);
    }

    private List<String> getCharacters(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        List<String> filteredLines = lines.stream()
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());

        String allContent = StringUtils.join(filteredLines, "\n\n") + "\n\n";

        List<String> characters = new ArrayList<>();
        char[] chars = allContent.toCharArray();
        for (char aChar : chars) {
            characters.add(String.valueOf(aChar));
        }

        return characters;
    }

    private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, config.getFontName());
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_CENTER);
        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
}
