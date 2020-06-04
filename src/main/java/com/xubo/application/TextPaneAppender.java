package com.xubo.application;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

@Plugin(
        name = "TextPaneAppender",
        category = "Core",
        elementType = "Appender")
public class TextPaneAppender extends AbstractAppender {

    public static volatile JTextPane jTextArea;

    public static void setJTextArea(JTextPane textArea) {
        jTextArea = textArea;
    }

    protected TextPaneAppender(String name, Filter filter) {
        super(name, filter, null);
    }

    @PluginFactory
    public static TextPaneAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter) {
        return new TextPaneAppender(name, filter);
    }

    @Override
    public void append(LogEvent event) {

        String formattedMessage = event.getMessage().getFormattedMessage();

        SwingUtilities.invokeLater(() -> {
            if (jTextArea != null) {
                try {
                    StyledDocument styledDocument = jTextArea.getStyledDocument();
                    styledDocument.insertString(styledDocument.getLength(), "\n" + formattedMessage, null);
                } catch (BadLocationException e) {


                }
            }
        });

    }
}
