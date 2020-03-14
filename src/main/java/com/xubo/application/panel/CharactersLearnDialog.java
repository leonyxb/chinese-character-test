package com.xubo.application.panel;

import com.xubo.data.book.Lesson;
import com.xubo.data.character.Character;
import com.xubo.data.dictionary.DictionaryEntry;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CharactersLearnDialog extends JDialog {

    public CharactersLearnDialog(Character character, JFrame owner) {
        super(owner);

        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
        this.setTitle(character.getText());
        this.setModal(true);

        JEditorPane mainPanel = new JEditorPane();

        mainPanel.setContentType("text/html");
        mainPanel.setText(buildHTMLText(character));
        mainPanel.setSelectionStart(0);
        mainPanel.setSelectionEnd(0);
        mainPanel.setMargin(new Insets(0, 10, 8, 10));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane);
    }

    private String buildHTMLText(Character character) {
        List<String> lines = new ArrayList<>();
        lines.add("<html>");

        lines.add("<p style=\" font-weight:bold; color:blue; font-size:16px; font-family: 楷体\">出现位置：</p>");

        character.getLessons()
                .stream()
                .filter(lesson -> lesson.getParentBook().display())
                .forEach(lesson -> {
            lines.add(buildLessonHTML(lesson));
        });
        lines.add("<p><hr /></p>");

        character.getDictionaryEntries().forEach(entry -> {

            lines.add(buildTitle(entry));
            lines.addAll(buildDescription(entry));

            entry.getWords().forEach(word -> {
                lines.add(buildWordLine(word));
            });

            lines.add("<p><hr /></p>");

        });

        lines.remove(lines.size()-1);

        lines.add("</html>");

        return String.join("\n", lines);
    }

    private String buildLessonHTML(Lesson lesson) {
        StringBuilder html = new StringBuilder();

        html.append("<p>");
        html.append("<span style=\" font-weight:bold; font-size:14px; font-family: 宋体\">");
        html.append("[" + StringEscapeUtils.escapeHtml4(lesson.getParentBook().toString()) + "]");
        html.append("</span>");
        html.append("<span style=\"  font-size:14px; font-family: 宋体\">");
        html.append("  " + StringEscapeUtils.escapeHtml4(lesson.toString()));
        html.append("</span>");

        html.append("</p>");

        return html.toString();
    }

    private List<String> buildDescription(DictionaryEntry entry) {

        List<String> lines = new ArrayList<>();

        String indexRef = "①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⑯⑰⑱⑲";
        StringTokenizer multiTokenizer = new StringTokenizer(entry.getDescription(), indexRef);

        int index = 0;
        while(multiTokenizer.hasMoreTokens()) {
            String token = multiTokenizer.nextToken();
            lines.add("<p  style=\" font-size:16px; \">" + indexRef.charAt(index++) + token + "</p>");
        }

        return lines;
    }

    private String buildTitle(DictionaryEntry entry) {
        StringBuilder title = new StringBuilder();

        title.append("<p>");
        title.append("<span style=\" font-weight:bold; color:blue; font-size:30px; font-family: 楷体\">");
        title.append("  " + entry.getText());
        title.append("</span>");
        if (entry.getTextAlias() != null) {
            title.append("<span style=\" color:blue; font-size:25px; font-family: 楷体\">");
            title.append(entry.getTextAlias());
            title.append("</span>");
        }
        title.append("<span style=\" color:blue; font-size:20px; font-family: Arial\">");
        title.append("  " + entry.getPinyin());
        title.append("</span>");
        title.append("</p>");


        return title.toString();
    }

    private String buildWordLine(DictionaryEntry word) {

        StringBuilder title = new StringBuilder();

        title.append("<p style=\" font-size:16px; \">");

        title.append("<span style=\" color:blue; \">");
        title.append(word.getText());
        title.append("</span>");

        title.append("<span>");
        title.append("  " + word.getPinyin() + "  ");
        title.append("</span>");

        title.append("<span>");
        title.append(word.getDescription());
        title.append("</span>");

        title.append("</p>");

        return title.toString();
    }
}
