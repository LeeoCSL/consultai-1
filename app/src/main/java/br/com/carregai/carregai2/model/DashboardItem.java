package br.com.carregai.carregai2.model;

/**
 * Created by renan.boni on 09/08/2017.
 */

public class DashboardItem {

    private int imageID;
    private String title;

    public DashboardItem(){

    }

    public DashboardItem(int imageID, String title) {
        this.imageID = imageID;
        this.title = title;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
