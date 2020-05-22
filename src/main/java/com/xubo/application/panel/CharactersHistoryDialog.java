package com.xubo.application.panel;

import com.xubo.application.ApplicationConfig;
import com.xubo.application.ApplicationUtils;
import com.xubo.data.character.CharacterTestRecords;
import com.xubo.data.character.TestStatus;
import com.xubo.data.record.TestRecordManager;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class CharactersHistoryDialog extends JDialog {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private ApplicationConfig config;

    private JList<HistoryItem> datesJList = new JList<>();

    private JTextArea correctCharactersArea = new JTextArea();
    private JTextArea incorrectCharactersArea = new JTextArea();

    private JLabel totalNumLabel = new JLabel();
    private JLabel correctNumLabel = new JLabel();
    private JLabel incorrectNumLabel = new JLabel();

    public CharactersHistoryDialog(ApplicationConfig config, JFrame owner) {
        super(owner);

        this.config = config;

        initGui();
        applyDate();
        initActions();
    }

    private void applyDate() {
        Map<Date, List<CharacterTestRecords>> historyByDate = TestRecordManager.getInstance().getHistoryByDate();

        List<HistoryItem> items = new ArrayList<>();
        historyByDate.forEach( (k, v) -> {
            Map<TestStatus, List<String>> namesByStatus = v.stream().collect(Collectors.groupingBy(
                    r -> r.getRecords().get(0).getStatus(),
                    Collectors.mapping(CharacterTestRecords::getName, Collectors.toList())));

            items.add(new HistoryItem(k, namesByStatus.get(TestStatus.KNOWN), namesByStatus.get(TestStatus.UNKNOWN)));

        });

        HistoryItem[] dates = items.stream()
                .sorted(Comparator.comparing(i -> i.date))
                .toArray(HistoryItem[]::new);

        datesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        datesJList.setListData(dates);

    }

    private void initActions() {

        datesJList.addListSelectionListener(e -> {
            JList<HistoryItem>  source = (JList<HistoryItem>) e.getSource();
            HistoryItem item = source.getSelectedValue();
            List<String> correctList = item.correct;
            List<String> incorrectList = item.incorrect;

            int correctNum = correctList.size();
            int incorrectNum = incorrectList.size();
            int total = correctNum + incorrectNum;

            totalNumLabel.setText("一共: " + total);
            correctNumLabel.setText("认识: " + correctNum );
            incorrectNumLabel.setText("不认识: " + incorrectNum);

            if (config.getLanguage() == ApplicationConfig.ApplicationLanguage.CHINESE) {
                correctCharactersArea.setText(StringUtils.join(correctList, " "));
                incorrectCharactersArea.setText(StringUtils.join(incorrectList, " "));
            } else {
                correctCharactersArea.setText(StringUtils.join(correctList, ", "));
                incorrectCharactersArea.setText(StringUtils.join(incorrectList, ", "));
            }

            correctCharactersArea.setCaretPosition(0);
            incorrectCharactersArea.setCaretPosition(0);

        });

        datesJList.setSelectedIndex(0);
    }

    private void initGui() {

        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);
        this.setTitle("历史记录");
        this.setModal(true);
        this.setResizable(false);

        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.gridheight = 1;
        c.weighty = 1;

        //col 1
        c.gridx = 0;
        c.gridwidth = 1;
        c.weightx = 1;
        add(buildCol1(), c);

        //col 2
        c.gridx = 1;
        c.gridwidth = 1;
        c.weightx = 6;
        add(buildCol2(), c);
    }

    private JScrollPane buildCol1() {
        JScrollPane jPanel = new JScrollPane(datesJList);

        datesJList.setFont(new Font(config.getFontName(), Font.PLAIN, 25));

        jPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        return jPanel;
    }
    private JPanel buildCol2() {

        correctCharactersArea.setEditable(false);
        correctCharactersArea.setFont(new Font(config.getFontName(), Font.PLAIN, 30));
        correctCharactersArea.setLineWrap(true);
        correctCharactersArea.setWrapStyleWord(true);
        correctCharactersArea.setForeground(new Color(48, 143, 4));
        correctCharactersArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        incorrectCharactersArea.setEditable(false);
        incorrectCharactersArea.setFont(new Font(config.getFontName(), Font.PLAIN, 30));
        incorrectCharactersArea.setLineWrap(true);
        incorrectCharactersArea.setWrapStyleWord(true);
        incorrectCharactersArea.setForeground(Color.RED);
        incorrectCharactersArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

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
        c.weighty = 7;
        JScrollPane scrollPanel1 = new JScrollPane(correctCharactersArea);
        scrollPanel1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPanel1.setPreferredSize(new Dimension(500, 150));
        jPanel.add(scrollPanel1, c);

        //row 2
        c.gridy = 1;
        c.gridheight = 1;
        c.weighty = 1;

        jPanel.add(buildStatisticRow(), c);

        //row 3
        c.gridy = 2;
        c.gridheight = 1;
        c.weighty = 2;

        JScrollPane scrollPanel2 = new JScrollPane(incorrectCharactersArea);
        scrollPanel2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPanel2.setPreferredSize(new Dimension(500, 150));
        jPanel.add(scrollPanel2, c);

        return jPanel;
    }

    private JPanel buildStatisticRow() {

        totalNumLabel.setFont(new Font(config.getLabelFontName(), Font.PLAIN, 25));
        totalNumLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        correctNumLabel.setFont(new Font(config.getLabelFontName(), Font.PLAIN, 25));
        correctNumLabel.setForeground(ApplicationUtils.Colors.KNOWN.getForeground());

        incorrectNumLabel.setFont(new Font(config.getLabelFontName(), Font.PLAIN, 25));
        incorrectNumLabel.setForeground(ApplicationUtils.Colors.UNKNOWN.getForeground());

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.gridheight = 1;
        c.weighty = 1;

        //col 1
        c.gridx = 0;
        c.gridwidth = 1;
        c.weightx = 1;
        jPanel.add(totalNumLabel, c);

        //col 2
        c.gridx = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        jPanel.add(correctNumLabel, c);


        //col 3
        c.gridx = 2;
        c.gridwidth = 1;
        c.weightx = 1;
        jPanel.add(incorrectNumLabel, c);

        return jPanel;
    }

    private static class HistoryItem {

        Date date;
        List<String> correct = new ArrayList<>();
        List<String> incorrect = new ArrayList<>();

        public HistoryItem(Date date, List<String> correct, List<String> incorrect) {
            this.date = date;
            if (correct != null) {
                this.correct.addAll(correct);
            }
            if (incorrect != null) {
                this.incorrect.addAll(incorrect);
            }
        }

        @Override
        public String toString() {
            return dateFormat.format(date) + "  " + (correct.size() + incorrect.size());
        }
    }

}
