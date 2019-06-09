package com.dejobhu.skhu.dejobhu.Singleton;

public class Userinfo {

    public static Userinfo shared = new Userinfo();

    private Userinfo() {

    }

    int id;
    String name;
    String email;
    String profileIMG;

    public String getProfileIMG() {
        return profileIMG;
    }

    public void setProfileIMG(String profileIMG) {
        this.profileIMG = profileIMG;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
