 package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {

    private Squad squad;
    private Agent agents[];

    @BeforeEach
    public void setUp(){
        this.squad = Squad.getInstance();
        Agent a = new Agent();
        a.setName("james bond");
        a.setSerialNumber("007");
        Agent b = new Agent();
        b.setName("alec trevelyan");
        b.setSerialNumber("006");
        this.agents= new Agent[]{a,b};
        squad.load(agents);

    }

    @Test
    public void testGetAgents() {
        LinkedList<String> serial = new LinkedList<String>();
        serial.add("007");
        serial.add("006");
        assertTrue(squad.getAgents(serial));
        serial.add("005");
        assertFalse(squad.getAgents(serial));
    }

    @Test
    public void testRelease1(){
        LinkedList<String> serial = new LinkedList<String>();
        serial.add("007");
        squad.releaseAgents(serial);
        assertTrue(squad.getAgents(serial));
    }


    @Test
    public void testRelease2(){
        LinkedList<String> serial = new LinkedList<String>();
        serial.add("007");
        serial.add("006");
        squad.releaseAgents(serial);
        assertTrue(squad.getAgents(serial));
    }

    @Test
    public void testGetAgentsNames(){
        LinkedList<String> serial = new LinkedList<String>();
        serial.add("007");
        String name="";
        for (int i= 0; i < agents.length; i++){
            if (agents[i].getSerialNumber().equals("007")){
                name = agents[i].getName();
            }
        }
        List<String> names = squad.getAgentsNames(serial);
        assertEquals(name, names.get(0));

    }

    @Test
    public void testSentAgents(){
        LinkedList<String> serial = new LinkedList<String>();
        serial.add("007");
        serial.add("006");
        assertFalse(squad.getAgents(serial));
        squad.sendAgents(serial, 10000);
        while (System.currentTimeMillis() <= System.currentTimeMillis() + 10000){}
        assertTrue(squad.getAgents(serial));

    }
}
