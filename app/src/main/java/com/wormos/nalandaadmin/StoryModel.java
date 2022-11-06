package com.wormos.nalandaadmin;

public class StoryModel {
    String videoPurl,thumbnail;

    public StoryModel() {
    }

    public StoryModel(String videoPurl, String thumbnail) {
        this.videoPurl = videoPurl;
        this.thumbnail = thumbnail;
    }

    public String getVideoPurl() {
        return videoPurl;
    }

    public void setVideoPurl(String videoPurl) {
        this.videoPurl = videoPurl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
