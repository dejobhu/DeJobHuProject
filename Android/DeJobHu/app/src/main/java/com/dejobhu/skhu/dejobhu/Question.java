package com.dejobhu.skhu.dejobhu;

import android.graphics.Bitmap;
import android.os.Build;

import java.util.Objects;


public class Question {
    String name;
    String body;
    String timestep;
    Bitmap image;

    public Question(String ID,String body,String time,Bitmap bitmap){
        name=ID;
        this.body=body;
        this.timestep=time;
        this.image=bitmap;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTimestep() {
        return timestep;
    }

    public void setTimestep(String timestep) {
        this.timestep = timestep;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(name, question.name) &&
                Objects.equals(body, question.body) &&
                Objects.equals(timestep, question.timestep);
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, body, timestep);
    }
}
