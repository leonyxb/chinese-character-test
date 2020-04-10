package com.xubo.application.panel;

import javax.swing.*;
import java.awt.*;

import static com.xubo.application.ChineseMainFrame.FONT_NAME;

public class TestConfigDialog extends JDialog {

    private JCheckBox randomCheckbox = new JCheckBox("打乱测试顺序");
    private JCheckBox learnCheckbox = new JCheckBox("允许学习");
    private JCheckBox recordCheckbox = new JCheckBox("记录本次测试");
    private JCheckBox testUnknownOnlyCheckbox = new JCheckBox("只测试不认识的字");
    private JButton startTest = new JButton("开始测试");

    public TestConfigDialog(Frame owner) {
        super(owner);
        initGui(owner);
        setDefaultValues();
        addActions();
    }

    private void addActions() {

    }

    private void setDefaultValues() {
        randomCheckbox.setSelected(true);
        learnCheckbox.setSelected(true);
        recordCheckbox.setSelected(true);
        testUnknownOnlyCheckbox.setSelected(false);
    }

    private void initGui(Frame owner) {
        this.setSize(250, 400);
        this.setLocationRelativeTo(owner);
        this.setTitle("测试设置");
        this.setModal(true);
        this.setResizable(false);

        JPanel contextPanel = new JPanel();
        contextPanel.setLayout(new GridLayout(5, 1));

        randomCheckbox.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
        testUnknownOnlyCheckbox.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
        learnCheckbox.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
        recordCheckbox.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
        startTest.setFont(new Font(FONT_NAME, Font.PLAIN, 20));
        startTest.setFocusPainted(false);

        contextPanel.add(randomCheckbox);
        contextPanel.add(learnCheckbox);
        contextPanel.add(recordCheckbox);
        contextPanel.add(testUnknownOnlyCheckbox);
        contextPanel.add(startTest);

        this.add(contextPanel);
    }
}
