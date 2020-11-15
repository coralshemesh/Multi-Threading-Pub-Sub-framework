package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	private LinkedList<MissionInfo> LMissionInfo;
	private CountDownLatch latch;

	public Intelligence(MissionInfo[] missions, CountDownLatch latch) {
		super("Intelligence");
		this.latch = latch;
		LMissionInfo = new LinkedList<>();
		LMissionInfo.addAll(Arrays.asList(missions));
	}

	@Override
	protected void initialize() {

		subscribeBroadcast(TerminationBroadcast.class, call -> terminate());
		subscribeBroadcast(TickBroadcast.class,call ->{
			while (LMissionInfo.size() > 0 && LMissionInfo.peek().getTimeIssued() == call.getPass()){
				MissionInfo m = LMissionInfo.poll();
				getSimplePublisher().sendEvent(new MissionReceivedEvent(m));
				System.out.println("intelligence sent a new mission: " +m.getMissionName());
			}
		});
		latch.countDown();
	}

}