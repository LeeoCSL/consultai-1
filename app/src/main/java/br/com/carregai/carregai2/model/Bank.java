package br.com.carregai.carregai2.model;

public class Bank {

    private String name;
    private int drawable;
    private String color;
    private String textColor;

    public Bank(){

    }

    public Bank(String name, int drawable, String color, String textColor) {
        this.name = name;
        this.drawable = drawable;
        this.color = color;
        this.textColor = textColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "name='" + name + '\'' +
                ", drawable=" + drawable +
                ", color='" + color + '\'' +
                ", textColor='" + textColor + '\'' +
                '}';
    }
}
