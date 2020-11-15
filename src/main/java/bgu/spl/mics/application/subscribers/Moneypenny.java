package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Squad;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {

	private Squad squad;
	private int ID;
	private int time;
	private CountDownLatch latch;


	public Moneypenny(String name, int id, CountDownLatch latch) {
		super(name);
		squad = Squad.getInstance();
		ID= id;
		this.latch= latch;
		this.time=0;
	}

	@Override
	protected void initialize() {

		subscribeBroadcast(TerminationBroadcast.class, call -> terminate());

		subscribeBroadcast(TickBroadcast.class, call -> time = call.getPass());

		if (ID % 2 == 0) {
			subscribeEvent(AgentsAvailableEvent.class, call -> { //some of the penny's check if the agents available
				List<String> serial = call.getAgents();
				if (squad.getAgents(serial)) complete(call, ID);
				else complete(call, 0);
			});
		}
		else{
			subscribeEvent(SendAgentsEvent.class, call->{ ////some of the penny's send the agents
				List<String> serial = call.getAgents();
				complete(call, squad.getAgentsNames(serial));
				squad.sendAgents(serial,call.getDuration());
			});
			subscribeEvent(ReleaseAgentsEvent.class, call ->{ //some of the penny's release the agents
				List<String> serial = call.getAgents();
				squad.releaseAgents(serial);
				complete(call, true);
			});
		}
		latch.countDown();
	}
}
