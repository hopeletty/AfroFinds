package com.example.afrofinds;

public class Model {
    private int briefs;
    private String title;
    private String description;

    public Model(int briefs, String title, String description){
        this.briefs = briefs;
        this.title = title;
        this.description = description;
    }

    public int getImage(){
        return briefs;
    }

    public void setImage(int briefs) {
        this.briefs = briefs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
