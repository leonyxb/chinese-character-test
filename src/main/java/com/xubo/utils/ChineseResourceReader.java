package com.xubo.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ChineseResourceReader {

    private ChineseResourceReader() {
    }

    public static List<String> readLines(String path, String encoding) {
        try {

            InputStream in = ChineseResourceReader.class.getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, encoding));

            return reader.lines().collect(toList());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
