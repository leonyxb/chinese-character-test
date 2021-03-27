package com.xubo.data.character;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CharacterTestRecord implements Comparable<CharacterTestRecord> {

    private Date date;
    private TestStatus status;

    public CharacterTestRecord() {
    }

    public CharacterTestRecord(Date date, TestStatus status) {
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

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    @Override
    public int compareTo(CharacterTestRecord o) {
        return o.getDate().compareTo(this.getDate());
    }

    @Override
    public String toString() {
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return "Record{" +
                "date=" + format +
                ", status=" + status +
                '}';
    }
}
