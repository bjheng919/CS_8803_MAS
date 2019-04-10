package com.example.mit.placeholder;

import android.widget.RadioGroup;

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
//    String party;
    String prefNation;
    String prefGender;
    String smoke;
    String mindSmoke;
    String pet;
    String mindPet;
    String sUseRoom;
    String cook;
    String otherCook;
    String timeFrom;
    String timeTo;
    String discription;
    int similarity;

    public String getRentLow() { return rentLow; }

    public void setRentLow(String rentLow) { this.rentLow = rentLow; }

    public String getRentHigh() { return rentHigh; }

    public void setRentHigh(String rentHigh) { this.rentHigh = rentHigh; }

    public String getGender(){return gender;}

    public void setGender(String gender){this.gender = gender;}

    public String getPrefGender() {return prefGender;}

    public void setPrefGender(String prefGender){this.prefGender = prefGender;}

    public String getPrefNation() { return prefNation; }

    public void setPrefNation(String prefNation) { this.prefNation = prefNation; }

//    public String getParty() { return party; }
//
//    public void setParty(String party) { this.party = party; }

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

    public String getMindSmoke() { return mindSmoke; }

    public void setMindSmoke(String mindSmoke) { this.mindSmoke = mindSmoke; }

    public String getPet() { return pet; }

    public void setPet(String pet) { this.pet = pet; }

    public String getMindPet() { return mindPet; }

    public void setMindPet(String mindPet) { this.mindPet = mindPet; }

    public String getUseRoom() { return sUseRoom; }

    public void setUseRoom(String sUseRoom) { this.sUseRoom = sUseRoom; }

    public String getCook() { return cook; }

    public void setCook(String cook) { this.cook = cook; }

    public String getOtherCook() { return otherCook; }

    public void setOtherCook(String otherCook) { this.otherCook = otherCook; }

    public String getTimeFrom() { return timeFrom; }

    public void setTimeFrom(String timeFrom) { this.timeFrom = timeFrom; }

    public String getTimeTo() { return timeTo; }

    public void setTimeTo(String timeTo) { this.timeTo = timeTo; }

    public String getDiscription() { return discription; }

    public void setDiscription(String discription) { this.discription = discription; }

    public int getSimilarity() { return similarity; }

    public void setSimilarity(int similarity) { this.similarity = similarity; }

    public int compareTo(UserSurvey other) { return this.similarity - other.similarity; }
}
