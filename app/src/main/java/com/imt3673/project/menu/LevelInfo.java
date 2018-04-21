package com.imt3673.project.menu;

public class LevelInfo {
    private String levelId;
    private String levelHeader;

    private String goldTime;
    private String silverTime;
    private String bronzeTime;

    /**
     * Level info constructor
     * @param levelHeader Name of level Displayed
     * @param levelId Level id, name of the level png in  res/raw/
     * @param goldTime time to get the gold medal
     * @param silverTime time to get the silver medal
     * @param bronzeTime time to get the bronze medal
     */
    LevelInfo(String levelHeader,String levelId, String goldTime, String silverTime, String bronzeTime) {
        this.levelHeader = levelHeader;
        this.levelId = levelId;
        this.goldTime = goldTime;
        this.silverTime = silverTime;
        this.bronzeTime = bronzeTime;
    }

    public String getLevelHeader() {
        return levelHeader;
    }

    public void setLevelHeader(String levelHeader) {
        this.levelHeader = levelHeader;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getGoldTime() {
        return goldTime;
    }

    public void setGoldTime(String goldTime) {
        this.goldTime = goldTime;
    }

    public String getSilverTime() {
        return silverTime;
    }

    public void setSilverTime(String silverTime) {
        this.silverTime = silverTime;
    }

    public String getBronzeTime() {
        return bronzeTime;
    }

    public void setBronzeTime(String bronzeTime) {
        this.bronzeTime = bronzeTime;
    }

}
