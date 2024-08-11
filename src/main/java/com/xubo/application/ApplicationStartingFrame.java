package com.xubo.application;

import com.xubo.data.ChineseData;
import com.xubo.data.EnglishData;
import com.xubo.data.FrenchData;
import com.xubo.data.GermanyData;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ApplicationStartingFrame extends JFrame {

    private static final Logger logger = LogManager.getLogger(ApplicationStartingFrame.class);

    private JTextPane loggerArea = new JTextPane();

    private JButton chineseButton = buildChineseButton();

    private JButton frenchButton = buildFrenchButton();

    private JButton englishButton = buildEnglishButton();

    private JButton germanyButton = buildGermanyButton();

    private JButton chineseBookButton = buildChineseBookButton();

    public ApplicationStartingFrame()  {
        initGui();
        initActions();
    }

    private void initActions() {

        germanyButton.addActionListener(e -> {
            setEnableLanguageButtons(false);
            CompletableFuture.runAsync(() -> {
                try {
                    GermanyData data = new GermanyData();
                    ApplicationMainFrame mainFrame = new ApplicationMainFrame(data, ApplicationConfig.GERMANY_CONFIG);
                    this.setVisible(false);
                    mainFrame.setVisible(true);
                } catch (Exception ex) {
                    logger.info("载入数据异常，请尝试修复后，重新点击按钮");
                    setEnableLanguageButtons(true);
                }
                logger.info("--------------------------------------------------------------------------");
                logger.info("");
            });
        });

        englishButton.addActionListener(e -> {
            setEnableLanguageButtons(false);
            CompletableFuture.runAsync(() -> {
                try {
                    EnglishData data = new EnglishData();
                    ApplicationMainFrame mainFrame = new ApplicationMainFrame(data, ApplicationConfig.ENGLISH_CONFIG);
                    this.setVisible(false);
                    mainFrame.setVisible(true);
                } catch (Exception ex) {
                    logger.info("载入数据异常，请尝试修复后，重新点击按钮");
                    setEnableLanguageButtons(true);
                }
                logger.info("--------------------------------------------------------------------------");
                logger.info("");
            });
        });

        frenchButton.addActionListener(e -> {
            setEnableLanguageButtons(false);
            CompletableFuture.runAsync(() -> {
                try {
                    FrenchData data = new FrenchData();
                    ApplicationMainFrame mainFrame = new ApplicationMainFrame(data, ApplicationConfig.FRENCH_CONFIG);
                    this.setVisible(false);
                    mainFrame.setVisible(true);
                } catch (Exception ex) {
                    logger.info("载入数据异常，请尝试修复后，重新点击按钮");
                    setEnableLanguageButtons(true);
                }
                logger.info("--------------------------------------------------------------------------");
                logger.info("");
            });
        });

        chineseButton.addActionListener(e -> {
            setEnableLanguageButtons(false);
            CompletableFuture.runAsync(() -> {
                try {
                    ChineseData data = new ChineseData();
                    ApplicationMainFrame mainFrame = new ApplicationMainFrame(data, ApplicationConfig.CHINESE_CONFIG);
                    this.setVisible(false);
                    mainFrame.setVisible(true);
                } catch (Exception ex) {
                    logger.info("载入数据异常，请尝试修复后，重新点击按钮");
                    setEnableLanguageButtons(true);
                }
                logger.info("--------------------------------------------------------------------------");
                logger.info("");
            });
        });

        chineseBookButton.addActionListener(e -> {
            setEnableLanguageButtons(false);
            CompletableFuture.runAsync(() -> {
                try {
                    ChineseData data = new ChineseData();
                    ApplicationReadingFrame mainFrame = new ApplicationReadingFrame(data, ApplicationConfig.CHINESE_CONFIG);
                    this.setVisible(false);
                    mainFrame.setVisible(true);
                } catch (Exception ex) {
                    logger.info("载入数据异常，请尝试修复后，重新点击按钮");
                    setEnableLanguageButtons(true);
                }
                logger.info("--------------------------------------------------------------------------");
                logger.info("");
            });
        });
    }

    private void setEnableLanguageButtons(boolean enable) {
        germanyButton.setEnabled(enable);
        englishButton.setEnabled(enable);
        frenchButton.setEnabled(enable);
        chineseButton.setEnabled(enable);
        chineseBookButton.setEnabled(enable);
    }

    private void initGui() {
        setSize(800, 600);
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
        this.add(buildCol1(), c);


        c.gridx = 1;
        c.weightx = 5;
        c.gridwidth = 1;
        this.add(buildCol2(), c);
    }

    private JPanel buildCol1() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(8, 1));
        jPanel.add(chineseButton);
        jPanel.add(chineseBookButton);
        jPanel.add(frenchButton);
        jPanel.add(englishButton);
        jPanel.add(germanyButton);
        return jPanel;
    }

    private JScrollPane buildCol2() {

        List<String> welcome = Arrays.asList(
                "",
                "          ===========================================",
                "          |            欢迎使用 <名字没想好>        |",
                "          ===========================================",
                ""
        );

        loggerArea.setText(StringUtils.join(welcome, "\n"));
        loggerArea.setEditable(false);
        loggerArea.setBackground(Color.WHITE);
        loggerArea.setFont(new Font("黑体", Font.PLAIN, 16));

        TextPaneAppender.setJTextArea(loggerArea);
        JScrollPane scrollPane = new JScrollPane(loggerArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        return scrollPane;
    }

    private JButton buildFrenchButton() {
        final ApplicationConfig config = ApplicationConfig.FRENCH_CONFIG;
        JButton button = new JButton("Français");
        button.setFocusPainted(false);
        button.setFont(new Font(config.getFontName(), Font.PLAIN, 26));
        return button;
    }

    private JButton buildEnglishButton() {
        final ApplicationConfig config = ApplicationConfig.ENGLISH_CONFIG;

        JButton button = new JButton("English");
        button.setFont(new Font(config.getFontName(), Font.PLAIN, 26));
        button.setFocusPainted(false);

        return button;
    }

    private JButton buildGermanyButton() {
        final ApplicationConfig config = ApplicationConfig.GERMANY_CONFIG;

        JButton button = new JButton("Deutsche");
        button.setFont(new Font(config.getFontName(), Font.PLAIN, 26));
        button.setFocusPainted(false);

        return button;
    }

    private JButton buildChineseButton() {
        final ApplicationConfig config = ApplicationConfig.CHINESE_CONFIG;

        JButton button = new JButton("中文");
        button.setFont(new Font(config.getFontName(), Font.BOLD, 30));
        button.setFocusPainted(false);

        return button;
    }

    private JButton buildChineseBookButton() {
        final ApplicationConfig config = ApplicationConfig.CHINESE_CONFIG;

        JButton button = new JButton("阅读");
        button.setFont(new Font(config.getFontName(), Font.BOLD, 30));
        button.setFocusPainted(false);

        return button;
    }

}
