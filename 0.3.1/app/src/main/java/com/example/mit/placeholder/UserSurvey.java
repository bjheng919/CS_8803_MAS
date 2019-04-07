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
    String party;
    String sameNation;
    String sameGender;
    String sRoommateOne;
    String sRoommateThree;
    String sRoommateFive;
    String sSmoke;
    String sOtherSmoke;
    String sPet;
    String sOtherPet;
    String sUseRoom;
    String sCook;
    String sOtherCook;
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

    public String getSameGender() {return sameGender;}

    public void setSameGender(String sameGender){this.sameGender = sameGender;}

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

//    public String getSmoke() { return smoke; }
//
//    public void setSmoke(String smoke) { this.smoke = smoke; }
//
//    public String getPet() { return pet; }
//
//    public void setPet(String pet) { this.pet = pet; }

    public String getRoommateOne() { return sRoommateOne; }

    public void setRoommateOne(String sRoommateOne) { this.sRoommateOne = sRoommateOne; }

//    public String getRoommateThree() { return sRoommateThree; }
//
//    public void setRoommateThree(String sRoommateThree) { this.sRoommateThree = sRoommateThree; }
//
//    public String getRoommateFive() { return sRoommateFive; }
//
//    public void setRoommateFive(String cook) { this.sRoommateFive = sRoommateFive; }

    public String getSmoke() { return sSmoke; }

    public void setSmoke(String sRoommateOne) { this.sSmoke = sSmoke; }

    public String getOtherSmoke() { return sOtherSmoke; }

    public void setOtherSmoke(String sOtherSmoke) { this.sOtherSmoke = sOtherSmoke; }

    public String getPet() { return sPet; }

    public void setPet(String sPet) { this.sPet = sPet; }

    public String getOtherPet() { return sOtherPet; }

    public void setOtherPet(String sOtherPet) { this.sOtherPet = sOtherPet; }

    public String getUseRoom() { return sUseRoom; }

    public void setUseRoom(String sUseRoom) { this.sUseRoom = sUseRoom; }

    public String getCook() { return sCook; }

    public void setCook(String cook) { this.sCook = sCook; }

    public String getOtherCook() { return sOtherCook; }

    public void setOtherCook(String cook) { this.sOtherCook = sOtherCook; }

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
