package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

	private MessageBroker messageBroker;
	private int time;
	private int id;
	private CountDownLatch latch;


	public M(String name, int id, CountDownLatch latch) {
		super(name);
		messageBroker = MessageBrokerImpl.getInstance();
		this.id = id;
		this.latch=latch;

	}

	@Override
	protected void initialize() {


		subscribeBroadcast(TerminationBroadcast.class, call -> terminate());

		subscribeBroadcast(TickBroadcast.class, call -> time = call.getPass());

		subscribeEvent(MissionReceivedEvent.class, call -> {
			Diary.getInstance().incrementTotal();
			System.out.println(this.getName()+ id +" starts handling " + call.getMissionName());
			List<String> serialNum = call.getSerialAgentsNumbers();
			AgentsAvailableEvent AA = new AgentsAvailableEvent(serialNum);
			Future<Integer> f1 = messageBroker.sendEvent(AA); //sent to MoneyPenny
			System.out.println(call.getMissionName() + "was sent to moneypenny, her id is: "+ f1.get().toString());
			Future<Integer> f2 = null;
			if (f1.get() != 0&&f1.get()!=null) {
				String gadget = call.getGadget();
				GadgetAvailableEvent GA = new GadgetAvailableEvent(gadget);
				f2 = messageBroker.sendEvent(GA); //sent to Q
				if(f2!=null&&f2.get() != 0){
					if(call.getExpire()-time>0) {
						SendAgentsEvent SA = new SendAgentsEvent(serialNum, call.getDuration());
						Future<List<String>> f3 = messageBroker.sendEvent(SA); //sent to MoneyPenny
						Report r = new Report(call.getMissionName(), call.getGadget(), id, f1.get(), call.getSerialAgentsNumbers(), f3.get(), call.getIssueTime(), time, f2.get());
						Diary.getInstance().addReport(r);
					}
					else{
						System.out.println("the time of : " + call.getMissionName() + "was expired");
						messageBroker.sendEvent(new ReleaseAgentsEvent(serialNum));
					}
				}
				else{
					messageBroker.sendEvent(new ReleaseAgentsEvent(serialNum));

				}
				complete(call, true);
				System.out.println(call.getMissionName() + " finished");

			}

		});
		latch.countDown();
	}

}
