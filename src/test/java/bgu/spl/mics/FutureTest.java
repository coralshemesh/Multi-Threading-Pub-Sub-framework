package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {

    private String result;
    Future future;

    @BeforeEach
    public void setUp(){
        future = new Future();
    }

    @Test
    public void testResolve(){
        future.resolve("complete");
        assertTrue(future.isDone());
    }

    @Test
    public void testGet(){
        future.resolve("complete");
        assertEquals("complete", future.get());
    }

    @Test
    public void testGet2(){
        assertNotEquals("complete", future.get());
    }

    @Test
    public void testIsDone(){
        assertFalse(future.isDone());
    }

    @Test
    public void testGetParameters1(){
        assertNotEquals("complete", future.get(1000, TimeUnit.SECONDS ));
    }

    @Test
    public void testGetParameters2(){
        future.resolve("complete");
        assertEquals("complete", future.get(1000, TimeUnit.SECONDS ));
    }

}
