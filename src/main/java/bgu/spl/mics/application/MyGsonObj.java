package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MyGsonObj {

    String[] inventory;
    GsonServices services;
    Agent[] squad;

    class GsonServices{
        int M;
        int Moneypenny;
        GsonIntelligence[] intelligence;
        int time;

        class GsonIntelligence{
            MissionInfo[] missions;
        }
    }

}
