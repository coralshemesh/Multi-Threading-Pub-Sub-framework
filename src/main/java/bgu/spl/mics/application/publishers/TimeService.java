package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link bgu.spl.mics.application.messages.TickBroadcast Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {

	private int duration;
	private AtomicInteger passingTime;


	public TimeService(int duration) {//set duration
		super("TimeService");
		this.duration= duration;

		passingTime= new AtomicInteger(1);
	}

	@Override
	protected void initialize() {}


	@Override
	public void run() {
		//initialize();
		System.out.println("TimeService initialized");
		while (passingTime.get()<duration && !Thread.currentThread().isInterrupted()){
				try {
					Thread.sleep(100);
				} catch (InterruptedException exp) { Thread.currentThread().interrupt();}
				if (!Thread.currentThread().isInterrupted()) {
					TickBroadcast b = new TickBroadcast(passingTime.get());
					this.getSimplePublisher().sendBroadcast(b);
					passingTime.getAndIncrement();
				}

		}
		Broadcast b = new TerminationBroadcast();
		this.getSimplePublisher().sendBroadcast(b);
		System.out.println("termination broadcast was sent");
	}

}


