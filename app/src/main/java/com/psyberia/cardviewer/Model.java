package com.psyberia.cardviewer;

public class Model {
    private int img;
    private String name;

    public Model(int img, String name) {
        this.img = img;
        this.name = name;
    }

    public int getImg() {
        return this.img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
