package br.com.carregai.carregai2.model;

/**
 * Created by renan.boni on 09/08/2017.
 */

public class DashItem {

    private int imageID;


    public DashItem(){

    }

    public DashItem(int imageID) {
        this.imageID = imageID;

    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

}
