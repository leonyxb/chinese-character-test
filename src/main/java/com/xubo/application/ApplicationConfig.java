package com.xubo.application;


public class ApplicationConfig {

    public static final ApplicationConfig FRENCH_CONFIG
            = new ApplicationConfig("Comic Sans MS", ApplicationLanguage.FRENCH);

    public static final ApplicationConfig CHINESE_CONFIG
            = new ApplicationConfig("楷体", ApplicationLanguage.CHINESE);

    private String fontName;

    private String buttonFontName = "楷体";

    private String labelFontName = "楷体";

    private ApplicationLanguage language;

    private ApplicationConfig(String fontName, ApplicationLanguage language) {
        this.fontName = fontName;
        this.language = language;
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

    public enum ApplicationLanguage {
        FRENCH,
        CHINESE
    }
}
