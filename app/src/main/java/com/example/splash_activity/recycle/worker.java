package com.example.splash_activity.recycle;

public class worker {
    private String name;
    private String profession;
    private String  description;
    private String profilePic;
    private String url;

    public worker() {
    }

    public worker(String name, String profession, String description, String profilePic, String url) {
        this.name = name;
        this.profession = profession;
        this.description = description;
        this.profilePic = profilePic;
        this.url=url;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setUrl(String url){this.url=url; }
    public  String getUrl(String url){return url;}
}
