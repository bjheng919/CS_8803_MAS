package com.example.vinayak.hw07;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Vinayaka on 11/22/2016.
 */
public class GroupProfile implements Serializable, Comparable<GroupProfile> {

    String groupName, commitNum, totalNum, uuid, image, creatorUuid, gender;
    List<String> members;
    int similarity;

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCommitNum() {
        return commitNum;
    }

    public void setCommitNum(String commitNum) {
        this.commitNum = commitNum;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCreatorUuid() { return creatorUuid; }

    public void setCreatorUuid(String creatorUuid) { this.creatorUuid = creatorUuid; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public int getSimilarity() { return similarity; }

    public void setSimilarity(int similarity) { this.similarity = similarity; }

    public int compareTo(GroupProfile other) { return this.getSimilarity() - other.getSimilarity(); }

}
