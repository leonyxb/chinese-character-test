package com.xubo.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.EventQueue;

public class Application {

    private static final Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("Staring Application ...");

        EventQueue.invokeLater(() -> {
            ApplicationStartingFrame startingFrame = new ApplicationStartingFrame();
            startingFrame.setVisible(true);
        });

    }
}
