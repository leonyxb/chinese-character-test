package com.xubo.data.character;

public class FrenchWord extends Character {

    private String category;

    private String genre;

    private String classe;

    private Integer echelle;

    public FrenchWord(String text) {
        super(text, "FR");
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

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public Integer getEchelle() {
        return echelle;
    }

    public void setEchelle(Integer echelle) {
        this.echelle = echelle;
    }
}
