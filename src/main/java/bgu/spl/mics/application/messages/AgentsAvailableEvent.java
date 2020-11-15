package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;


public class AgentsAvailableEvent implements Event {

    private List<String> agents;

    public AgentsAvailableEvent(List<String> a) {
        agents = a;
    }

    public List<String > getAgents() { return agents; }

}
