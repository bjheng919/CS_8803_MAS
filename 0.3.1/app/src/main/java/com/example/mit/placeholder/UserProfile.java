package com.example.mit.placeholder;

import java.io.Serializable;

/**
 * Created by Vinayaka on 11/22/2016.
 */
public class UserProfile implements Serializable, Comparable<UserProfile> {

    String fname, lname, email, image, uuid;
    int similarity;
    boolean committed = false;

    public boolean isCommitted() {
        return committed;
    }

    public void setCommitted(boolean committed) {
        this.committed = committed;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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

    public int getSimilarity() { return similarity; }

    public void setSimilarity(int similarity) { this.similarity = similarity; }

    public int compareTo(UserProfile other) { return this.getSimilarity() - other.getSimilarity(); }

    public String toString() { return "User Profile: " + this.uuid; }
}
