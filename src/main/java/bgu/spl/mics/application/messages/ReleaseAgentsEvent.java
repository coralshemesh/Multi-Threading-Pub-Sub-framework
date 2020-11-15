package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class ReleaseAgentsEvent implements Event {
    private List<String> agents;

    public ReleaseAgentsEvent (List<String> a) {
        agents = a;
    }

    public List<String > getAgents() {
        return agents;
    }
}
