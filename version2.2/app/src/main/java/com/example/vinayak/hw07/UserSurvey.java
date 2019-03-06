package com.example.vinayak.hw07;

import java.io.Serializable;

/**
 * Created by Vinayaka on 11/22/2016.
 */
public class UserSurvey implements Serializable{

    String nation, rmType, leasingTime, rmmtNum, smoke, pet, cook;

    public String getNation() { return nation; }

    public void setNation(String nation) { this.nation = nation; }

    public String getRmType() { return rmType; }

    public void setRmType(String rmType) { this.rmType = rmType; }

    public String getLeasingTime() { return leasingTime; }

    public void setLeasingTime(String leasingTime) { this.leasingTime = leasingTime; }

    public String getRmmtNum() { return rmmtNum; }

    public void setRmmtNum(String rmmtNum) { this.rmmtNum = rmmtNum; }

    public String getSmoke() { return smoke; }

    public void setSmoke(String smoke) { this.smoke = smoke; }

    public String getPet() { return pet; }

    public void setPet(String pet) { this.pet = pet; }

    public String getCook() { return cook; }

    public void setCook(String cook) { this.cook = cook; }

}
