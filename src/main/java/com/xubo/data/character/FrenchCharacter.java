package com.xubo.data.character;

public class FrenchCharacter extends Character {

    private String category;

    private String genre;

    public FrenchCharacter(String text) {
        super(text);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
