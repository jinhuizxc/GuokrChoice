package com.example.jh.guokrchoice.bean;

public class Article {
    private String title;
    private String headline_img;
    private String summary;
    private int inFavor;

    public int getInFavor() {
        return inFavor;
    }

    public void setInFavor(int inFavor) {
        this.inFavor = inFavor;
    }

    private int id;
    private int date_picked;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeadline_img() {
        return headline_img;
    }

    public void setHeadline_img(String headline_img) {
        this.headline_img = headline_img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDate_picked() {
        return date_picked;
    }

    public void setDate_picked(int date_picked) {
        this.date_picked = date_picked;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
