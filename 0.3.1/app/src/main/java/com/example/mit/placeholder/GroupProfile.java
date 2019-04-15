package com.example.mit.placeholder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinayaka on 11/22/2016.
 */
public class GroupProfile implements Serializable, Comparable<GroupProfile> {

    private String groupName, commitNum, totalNum, uuid, image, creatorUuid;
    private List<String> members;
    private int similarity;

    public GroupProfile() {
        groupName = "";
        commitNum = "";
        totalNum = "";
        uuid = "";
        image = "";
        creatorUuid = "";
        members = new ArrayList<>();
    }

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

    public int getSimilarity() { return similarity; }

    public void setSimilarity(int similarity) { this.similarity = similarity; }

    public int compareTo(GroupProfile other) { return this.getSimilarity() - other.getSimilarity(); }

}
