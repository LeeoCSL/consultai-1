package br.com.carregai.carregai2.model;

/**
 * Created by renan.boni on 14/08/2017.
 */

public class User {

    private String name;
    private String email;
    private String image;
    private String phone;

    public User(){

    }

    public User(String name, String email, String image, String phone) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
