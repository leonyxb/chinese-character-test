package com.xubo.application;

import com.xubo.data.character.Character;
import com.xubo.data.character.TestStatus;
import com.xubo.data.character.CharacterTestRecord;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicationUtils {

    private static final long MILLISECONDS_PER_DAY = 1000 * 3600 * 24;

    public static String getCssColor(Color color) {
        return String.format("rgb(%s, %s, %s)", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static Colors getDisplayedColors(Character character) {

        List<CharacterTestRecord> records = oneRecordPerDay(character.getTestRecord().getRecords());

        if (records.isEmpty()) {
            return Colors.DEFAULT;
        }

        if (isArchived(records)) {
            if (isLongTimeNotTested(records, 21)) {
                return Colors.NEED_RETEST;
            } else {
                return Colors.ARCHIVED;
            }
        }

        Colors colors = Colors.DEFAULT;
        List<CharacterTestRecord> last3Records = getLastTestRecords(records, 3);
        if (!last3Records.isEmpty()) {

            long knownNum = (int) last3Records.stream()
                    .filter(r -> r.getStatus() == TestStatus.KNOWN)
                    .count();

            if (knownNum == 3) {

                int limitDaysNum;
                List<CharacterTestRecord> lastKnownRecords = getLastConsecutiveKnownTestRecords(records);
                if (lastKnownRecords.size() <= 3) {
                    limitDaysNum = 2;
                } else if (lastKnownRecords.size() <= 4) {
                    limitDaysNum = 3;
                } else if (lastKnownRecords.size() <= 5) {
                    limitDaysNum = 5;
                } else if (lastKnownRecords.size() <= 6) {
                    limitDaysNum = 8;
                } else {
                    limitDaysNum = 13;
                }

                if (isLongTimeNotTested(records, limitDaysNum)) {
                    colors = Colors.NEED_RETEST;
                } else {
                    colors = Colors.KNOWN;
                }
            } else if (knownNum == 2) {
                colors = Colors.ALMOST_KNOWN;
            } else if (knownNum == 1) {
                colors = Colors.ALMOST_UNKNOWN;
            } else {
                colors = Colors.UNKNOWN;
            }
        }

        return colors;
    }

    /**
     * 一个字，如果满足下列条件，就认为这个字已经永久记住：
     *  1 - 最近的，连续表示至少7次认识
     *  2 - 连续认识的时间跨度大于30天
     */
    private static boolean isArchived(List<CharacterTestRecord> records) {
        List<CharacterTestRecord> lastKnownRecords = getLastConsecutiveKnownTestRecords(records);

        if (lastKnownRecords.size() >= 7) {
            long firstKnownTime = lastKnownRecords.get(lastKnownRecords.size() - 1).getDate().getTime();
            long lastKnownTime = lastKnownRecords.get(0).getDate().getTime();
            if (lastKnownTime - firstKnownTime > MILLISECONDS_PER_DAY * 30) {
                return true;
            }
        }
        return false;
    }

    /**
     * 超过15天没有测试
     */
    private static boolean isLongTimeNotTested(List<CharacterTestRecord> records, int limitDaysNum) {
        CharacterTestRecord lastRecord = records.get(0);
        long duration = System.currentTimeMillis() - lastRecord.getDate().getTime();
        return duration > MILLISECONDS_PER_DAY * limitDaysNum;
    }


    public static Color getDisplayedColor(Character character, boolean isBackground) {
        Colors displayedColors = getDisplayedColors(character);
        return isBackground ? displayedColors.getBackground() : displayedColors.getForeground();
    }

    private static List<CharacterTestRecord> getLastTestRecords(List<CharacterTestRecord> records, int num) {
        if (num > 0 && records.size() >= num) {
            return records.subList(0, num);
        }
        return Collections.emptyList();
    }

    private static List<CharacterTestRecord> getLastConsecutiveKnownTestRecords(List<CharacterTestRecord> records) {
        List<CharacterTestRecord> knownRecords = new ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            CharacterTestRecord record = records.get(i);
            if (record.getStatus() == TestStatus.KNOWN) {
                knownRecords.add(record);
            } else {
                break;
            }
        }

        return knownRecords;
    }

    public static boolean isKnown(Character character) {
        Colors colors = getDisplayedColors(character);
        return colors == Colors.KNOWN || colors == Colors.ARCHIVED;
    }

    public enum Colors {
        DEFAULT(
                Color.BLACK,
                Color.LIGHT_GRAY
        ),
        NEED_RETEST(
                new Color(193, 1, 162),
                new Color(196, 125, 187)
        ),
        KNOWN(
                new Color(0, 138, 0),
                new Color(142, 196, 142)
        ),
        ALMOST_KNOWN(
                new Color(187, 209, 4),
                new Color(191, 191, 72)
        ),
        ALMOST_UNKNOWN(
                new Color(236, 130, 4),
                new Color(236, 190, 97)
        ),
        UNKNOWN(
                new Color(178, 0, 0),
                new Color(239, 166, 166)
        ),
        ARCHIVED(
                new Color(0, 33, 136),
                new Color(170, 199, 239)
        );

        private final Color foreground;
        private final Color background;

        Colors(Color foreground, Color background) {
            this.foreground = foreground;
            this.background = background;
        }

        public Color getForeground() {
            return foreground;
    }

        public Color getBackground() {
            return background;
        }
    }

    private static long getTimeMillisWithDateOnly(Date date) {
        return getDateWithoutTimes(date).getTime();
    }

    public static Date getDateWithoutTimes(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static List<CharacterTestRecord> oneRecordPerDay(List<CharacterTestRecord> records) {
        List<CharacterTestRecord> oneRecordPerDay = new ArrayList<>();

        Map<Long, List<CharacterTestRecord>> recordsPerDay = records.stream().collect(Collectors.groupingBy(r -> getTimeMillisWithDateOnly(r.getDate())));
        recordsPerDay.forEach((k, v) -> {
            oneRecordPerDay.add(new CharacterTestRecord(new Date(k), getOneTestResult(v)));
        });

        Collections.sort(oneRecordPerDay);
        return oneRecordPerDay;
    }

    private static TestStatus getOneTestResult(List<CharacterTestRecord> records) {

        long unknownCount = records.stream().filter(r -> r.getStatus() == TestStatus.UNKNOWN).count();
        if (unknownCount > 0) {
            return TestStatus.UNKNOWN;
        }

        return TestStatus.KNOWN;
    }

}
