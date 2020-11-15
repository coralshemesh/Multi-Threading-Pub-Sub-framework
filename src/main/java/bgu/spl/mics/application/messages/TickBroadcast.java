package bgu.spl.mics.application.messages;
import bgu.spl.mics.Broadcast;
//notify every subscribe who is interested of how much clock ticks passed since the TimeService initial execution
public class TickBroadcast implements Broadcast {
    private int pass;

    public TickBroadcast(int pass){
        this.pass=pass;
    }

    public int getPass(){ return pass;}
}