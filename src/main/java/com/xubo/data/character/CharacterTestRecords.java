package com.xubo.data.character;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class CharacterTestRecords {

    private String name;

    private List<CharacterTestRecord> records = new ArrayList<>();

    public CharacterTestRecords(String name) {
        this.name = name;
    }

    public CharacterTestRecords() {
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public String getDisplayName() {
        return name.replace("_", " ");
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CharacterTestRecord> getRecords() {
        return records;
    }

    public void setRecords(List<CharacterTestRecord> records) {
        this.records = records;
    }

}
