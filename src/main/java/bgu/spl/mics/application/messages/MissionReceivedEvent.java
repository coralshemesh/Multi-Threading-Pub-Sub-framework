package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;


public class MissionReceivedEvent implements Event<Boolean> {
    private MissionInfo missionInfo;

    public MissionReceivedEvent(MissionInfo m){
        missionInfo=m;
    }

    public String getMissionName(){ return missionInfo.getMissionName();}

    public List<String> getSerialAgentsNumbers(){ return missionInfo.getSerialAgentsNumbers();}

    public String getGadget() { return missionInfo.getGadget();}

    public int getExpire() { return missionInfo.getTimeExpired();}

    public int getDuration() { return missionInfo.getDuration();}

    public int getIssueTime() { return missionInfo.getTimeIssued();}

}