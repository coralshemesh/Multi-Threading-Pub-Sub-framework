package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class GadgetAvailableEvent<AtomicInteger> implements Event {

    private String gadget;

    public GadgetAvailableEvent(String g){ gadget=g; }

    public String getGadget(){ return gadget;}
}
