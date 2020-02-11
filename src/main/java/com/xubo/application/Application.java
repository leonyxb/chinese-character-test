package com.xubo.application;

import com.xubo.data.ChineseData;

import java.awt.EventQueue;

public class Application {

    public static void main(String[] args) {

        ChineseData data = new ChineseData();

        EventQueue.invokeLater(() -> {
            ChineseMainFrame chinese = new ChineseMainFrame(data);
            chinese.setVisible(true);
        });

    }
}
