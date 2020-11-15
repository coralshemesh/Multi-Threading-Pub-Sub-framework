package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    private Inventory inventory;
    private String gadgets[];

    @BeforeEach
    public void setUp(){
        inventory = Inventory.getInstance();
        this.gadgets = new String[]{"sky hook","geiger counter","x-ray glasses"};
        inventory.load(gadgets);
    }

    @Test
    public void testGetItem(){
        assertTrue(inventory.getItem("sky hook"));
        assertFalse(inventory.getItem("sky hook"));
        assertFalse(inventory.getItem("sward"));
    }
}
