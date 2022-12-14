package com.wormos.nalandaadmin;

public class HighLightModel {
    String Title,link,linkName,tagline,purl;

    public HighLightModel() {
    }

    public HighLightModel(String title, String link, String linkName, String tagline, String purl) {
        Title = title;
        this.link = link;
        this.linkName = linkName;
        this.tagline = tagline;
        this.purl = purl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }
}
