package com.dejobhu.skhu.dejobhu.models;

public class ItemImage {
    int postNum;
    String url;

    public ItemImage(int postNum, String url) {
        this.postNum = postNum;
        this.url = url;
    }

    public int getPostNum() {
        return postNum;
    }

    public void setPostNum(int postNum) {
        this.postNum = postNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
