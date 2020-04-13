package com.xubo.application;

import com.xubo.data.ChineseData;
import com.xubo.data.FrenchData;

import javax.swing.*;
import java.awt.*;

public class ApplicationStartingFrame extends JFrame {

    public ApplicationStartingFrame()  {

        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(buildChineseButton());
        panel.add(buildFrenchButton());

        this.add(panel);
    }

    private JButton buildFrenchButton() {
        final ApplicationConfig config = ApplicationConfig.FRENCH_CONFIG;

        JButton button = new JButton("Français");
        button.setFocusPainted(false);
        button.setFont(new Font(config.getFontName(), Font.PLAIN, 26));

        button.addActionListener(e -> {
            this.setEnabled(false);
            ApplicationMainFrame mainFrame = new ApplicationMainFrame(new FrenchData(), config);
            this.setVisible(false);
            mainFrame.setVisible(true);
        });
        return button;
    }

    private JButton buildChineseButton() {
        final ApplicationConfig config = ApplicationConfig.CHINESE_CONFIG;

        JButton button = new JButton("中文");
        button.setFont(new Font(config.getFontName(), Font.BOLD, 30));
        button.setFocusPainted(false);

        button.addActionListener(e -> {
            this.setEnabled(false);
            ApplicationMainFrame mainFrame = new ApplicationMainFrame(new ChineseData(), config);
            this.setVisible(false);
            mainFrame.setVisible(true);
        });
        return button;
    }
}
