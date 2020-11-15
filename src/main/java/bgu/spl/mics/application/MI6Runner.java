package bgu.spl.mics.application;


import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/** This is the Main class of the application. You should parse the input file, 
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) throws IOException, InterruptedException {

        Gson gson = new Gson();
        MyGsonObj myGsonObj = gson.fromJson(new FileReader(args[0]), MyGsonObj.class);

        Inventory.getInstance().load(myGsonObj.inventory); // update the inventory items

        Squad.getInstance().load(myGsonObj.squad); // update the squad

        //amount of threads = Q + num of M + num of Moneypenny + num of intelligence (we do not count the TimeService)
        int countThreads =1 + myGsonObj.services.M + myGsonObj.services.Moneypenny + myGsonObj.services.intelligence.length;
        CountDownLatch latch = new CountDownLatch(countThreads);

        ExecutorService executor1 = Executors.newCachedThreadPool();//(myGsonObj.services.M); //an executor for M
        ExecutorService executor2 = Executors.newCachedThreadPool();//(1 + myGsonObj.services.intelligence.length+1); // an executor for intelligence timeservice and Q
        ExecutorService executor3 = Executors.newCachedThreadPool();//(myGsonObj.services.Moneypenny); // an executor for moneypenny

        executor2.submit(new Q("Q", latch));

        for ( int k = 0; k< myGsonObj.services.intelligence.length; k++){
            //start new thread for each Intelligence
            executor2.submit(new Intelligence(myGsonObj.services.intelligence[k].missions, latch));
        }
        for ( int j = 1; j<= myGsonObj.services.Moneypenny; j++){
            //start new thread for each MoneyPenny
            executor3.submit(new Moneypenny("Moneypenny", j, latch));
        }
        executor3.shutdown();
        for (int i = 1; i<= myGsonObj.services.M; i++){
            //start new thread for each M
            executor1.submit(new M("M", i, latch));
        }
        executor1.shutdown();

        latch.await(); // the main waits until all threads started and then timeservice thread starts

        executor2.submit(new TimeService(myGsonObj.services.time));
        executor2.shutdown();

        executor2.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS); //wait for termination according to the amount of ticks we have
        executor3.shutdownNow();
        executor3.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        executor1.shutdownNow(); //send interrupt to M

        Inventory.getInstance().printToFile(args[1]);
        Diary.getInstance().printToFile(args[2]);
    }
}
