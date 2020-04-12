package com.xubo.application;

import com.xubo.data.ChineseData;
import com.xubo.data.FrenchData;

import javax.swing.*;
import java.awt.EventQueue;

public class Application {

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            ApplicationStartingFrame startingFrame = new ApplicationStartingFrame();
            startingFrame.setVisible(true);
        });

    }
}
