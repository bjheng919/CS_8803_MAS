package com.example.mit.placeholder;

import java.io.Serializable;

public class UserSurvey implements Serializable, Comparable<UserSurvey> {

    String nation;
    String gender;
    String rmType;
    String rmmtNum;
    String lsStartTime;
    String lsEndTime;
    String rentLow;
    String rentHigh;
    String smoke;
    String pet;
    String cook;
    String party;
    String sameNation;
    String sameGender;
    int similarity;

    public String getRentLow() { return rentLow; }

    public void setRentLow(String rentLow) { this.rentLow = rentLow; }

    public String getRentHigh() { return rentHigh; }

    public void setRentHigh(String rentHigh) { this.rentHigh = rentHigh; }

    public String getGender() {return gender;}

    public void setGender(String gender) {this.gender = gender;}

    public String getSameGender() {return sameGender;}

    public void setSameGender(String sameGender) {this.sameGender = sameGender;}

    public String getSameNation() { return sameNation; }

    public void setSameNation(String sameNation) { this.sameNation = sameNation; }

    public String getParty() { return party; }

    public void setParty(String party) { this.party = party; }

    public String getNation() { return nation; }

    public void setNation(String nation) { this.nation = nation; }

    public String getRmType() { return rmType; }

    public void setRmType(String rmType) { this.rmType = rmType; }

    public String getLsStartTime() { return lsStartTime; }

    public void setLsStartTime(String lsStartTime) { this.lsStartTime = lsStartTime; }

    public String getLsEndTime() { return lsEndTime; }

    public void setLsEndTime(String lsEndTime) { this.lsEndTime = lsEndTime; }

    public String getRmmtNum() { return rmmtNum; }

    public void setRmmtNum(String rmmtNum) { this.rmmtNum = rmmtNum; }

    public String getSmoke() { return smoke; }

    public void setSmoke(String smoke) { this.smoke = smoke; }

    public String getPet() { return pet; }

    public void setPet(String pet) { this.pet = pet; }

    public String getCook() { return cook; }

    public void setCook(String cook) { this.cook = cook; }

    public int getSimilarity() { return similarity; }

    public void setSimilarity(int similarity) { this.similarity = similarity; }

    public int compareTo(UserSurvey other) { return this.similarity - other.similarity; }
}
