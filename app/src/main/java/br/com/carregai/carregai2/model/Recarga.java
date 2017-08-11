package br.com.carregai.carregai2.model;

/**
 * Created by renan.boni on 11/08/2017.
 */

public class Recarga {

    private String date;
    private String value;

    public Recarga(String date, String value) {
        this.date = date;
        this.value = value;
    }

    public Recarga(){}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
