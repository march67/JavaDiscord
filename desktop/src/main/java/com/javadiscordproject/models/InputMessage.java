package com.javadiscordproject.models;

public class InputMessage {

    private String from;
    private String text;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public InputMessage() {
    }

    public InputMessage(String from, String text) {
        super();
        this.from = from;
        this.text = text;
    }

}
