package com.xubo.data.character;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CharacterTestRecord {

    private Date date;
    private CharacterStatus status;

    public CharacterTestRecord() {
    }

    public CharacterTestRecord(Date date, CharacterStatus status) {
        this.date = date;
        this.status = status;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CharacterStatus getStatus() {
        return status;
    }

    public void setStatus(CharacterStatus status) {
        this.status = status;
    }
}
