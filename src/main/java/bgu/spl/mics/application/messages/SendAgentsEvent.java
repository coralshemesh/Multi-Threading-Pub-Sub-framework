package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class SendAgentsEvent implements Event {

    private List<String> NUMagents;
    private int duration;

    public SendAgentsEvent(List<String> a, int duration) {
        NUMagents = a;
        this.duration=duration;
    }

    public List<String > getAgents() {
        return NUMagents;
    }

    public int getDuration(){ return duration;}

}
