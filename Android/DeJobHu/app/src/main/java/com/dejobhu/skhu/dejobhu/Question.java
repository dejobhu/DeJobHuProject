package com.dejobhu.skhu.dejobhu;

import android.os.Build;

import java.util.Objects;

import androidx.annotation.RequiresApi;

public class Question {
    String name;
    String body;
    String timestep;

    public Question(String ID,String body,String time){
        name=ID;
        this.body=body;
        this.timestep=time;
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(name, question.name) &&
                Objects.equals(body, question.body) &&
                Objects.equals(timestep, question.timestep);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(name, body, timestep);
    }
}
