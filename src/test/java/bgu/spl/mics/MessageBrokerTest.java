package bgu.spl.mics;

import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.example.messages.MadeUpBroadcast;
import bgu.spl.mics.example.messages.MadeUpEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Queue;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;


public class MessageBrokerTest {
    private M m;
    private MessageBroker messageBroker;
    private Broadcast b;
    private MadeUpEvent e;


    @BeforeEach
    public void setUp(){
        messageBroker = MessageBrokerImpl.getInstance();
        m = new M("M", 1, new CountDownLatch(1));
        b = new MadeUpBroadcast();
        e = new MadeUpEvent();
        messageBroker.register(m);
        messageBroker.subscribeEvent(MadeUpEvent.class, m);
        messageBroker.subscribeBroadcast(MadeUpBroadcast.class, m);
    }


    @Test
    public void testSendEvent(){
        Future f = messageBroker.sendEvent(e);
        messageBroker.complete(e, "complete");
        assertEquals("complete", f.isDone());
    }

    @Test
    public void testAwaitMessage1(){
        Subscriber s = new M("M", 1, new CountDownLatch(1));
        messageBroker.register(s);
        messageBroker.unregister(m);
        try {
            messageBroker.awaitMessage(s);
            fail("s is unregistered");
        }
        catch (Exception e){
            assert true;
        }
    }
}
