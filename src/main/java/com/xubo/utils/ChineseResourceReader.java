package com.xubo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ChineseResourceReader {

    private ChineseResourceReader() {
    }

    public static List<String> readLinesFromResources(String path, String encoding) {
        try {

            InputStream in = ChineseResourceReader.class.getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, encoding));

            return reader.lines().collect(toList());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static List<String> readLines(Path path, Charset charset) {
        try {

            return Files.readAllLines(path, charset);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
