package com.example.vinayak.hw07;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vinayaka on 11/22/2016.
 */
public class UserSurvey implements Serializable {

    String nation;
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

    public String getRentLow() { return rentLow; }

    public void setRentLow(String rentLow) { this.rentLow = rentLow; }

    public String getRentHigh() { return rentHigh; }

    public void setRentHigh(String rentHigh) { this.rentHigh = rentHigh; }

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

}
