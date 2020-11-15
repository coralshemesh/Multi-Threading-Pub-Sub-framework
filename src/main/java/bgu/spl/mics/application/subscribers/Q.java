package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import java.util.concurrent.CountDownLatch;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {

	private Inventory inventory;
	private int time;
	private CountDownLatch latch;


	public Q(String name, CountDownLatch latch) {
		super(name);
		inventory = Inventory.getInstance();
		time = 0;
		this.latch=latch;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TerminationBroadcast.class, call -> terminate());
		subscribeBroadcast(TickBroadcast.class, call-> time = call.getPass());

		subscribeEvent(GadgetAvailableEvent.class, call -> {
			Boolean result = inventory.getItem(call.getGadget());
			if(result){
				complete(call, time);
				System.out.println("Q took the item for the mission in time:" + time);
			}
			else { complete(call, 0);}

		});
		latch.countDown();
	}

}
