package com.xubo.application;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ApplicationConfig {

    public static final boolean isAdmin = false;

    public static final ApplicationConfig FRENCH_CONFIG
            = new ApplicationConfig("Comic Sans MS", ApplicationLanguage.FRENCH);

    public static final ApplicationConfig CHINESE_CONFIG
            = new ApplicationConfig("楷体", ApplicationLanguage.CHINESE);

    public static final ApplicationConfig ENGLISH_CONFIG
            = new ApplicationConfig("Comic Sans MS", ApplicationLanguage.ENGLISH);

    public static final ApplicationConfig GERMANY_CONFIG
            = new ApplicationConfig("Comic Sans MS", ApplicationLanguage.GERMANY);

    private String fontName;

    private String buttonFontName = "楷体";

    private String labelFontName = "楷体";

    private ApplicationLanguage language;

    private Path resourceFolder;

    private ApplicationConfig(String fontName, ApplicationLanguage language) {
        this.fontName = fontName;
        this.language = language;
        this.resourceFolder = Paths.get("", "books", language.toString().toLowerCase());

    }

    public String getFontName() {
        return fontName;
    }

    public ApplicationLanguage getLanguage() {
        return language;
    }

    public String getButtonFontName() {
        return buttonFontName;
    }

    public String getLabelFontName() {
        return labelFontName;
    }

    public Path getResourceFolder() {
        return resourceFolder;
    }

    public enum ApplicationLanguage {
        FRENCH,
        CHINESE,
        ENGLISH,
        GERMANY
    }
}
