package com.xubo.application;

import com.xubo.data.character.Character;
import com.xubo.data.character.CharacterStatus;
import com.xubo.data.character.CharacterTestRecord;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class ApplicationUtils {

    public static final Color COLOR_UNKNOWN = new Color(178, 0, 0);
    public static final Color COLOR_KNOWN = new Color(0, 138, 0);

    public static String getCssColor(Color color) {
        return String.format("rgb(%s, %s, %s)", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static Color getDisplayedColor(Character character, boolean isBackground) {
        List<CharacterTestRecord> records = character.getTestRecord().getRecords();
        if (!records.isEmpty()) {

            List<CharacterTestRecord> last3Records = getLastTestRecords(character, 3);
            if (!last3Records.isEmpty()) {

                long knownNum = (int) last3Records.stream()
                        .filter(r -> r.getStatus() == CharacterStatus.KNOWN)
                        .count();

                if (knownNum == 3) {
                    return isBackground ? new Color(142, 196, 142) : COLOR_KNOWN;
                } else if (knownNum == 2) {
                    return isBackground ? new Color(191, 191, 72) : new Color(187, 209, 4);
                } else if (knownNum == 1) {
                    return isBackground ? new Color(236, 190, 97) : new Color(236, 130, 4);
                } else {
                    return isBackground ? new Color(239, 166, 166) : COLOR_UNKNOWN;
                }
            }
        }

        return isBackground ? Color.LIGHT_GRAY : Color.BLACK;
    }

    public static List<CharacterTestRecord> getLastTestRecords(Character character, int num) {
        List<CharacterTestRecord> records = character.getTestRecord().getRecords();
        if (records.size() >= num) {
            return records.subList(records.size() - num, records.size());
        }
        return Collections.emptyList();
    }

}
