package bgu.spl.mics;

import java.util.*;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private static class singletonHolder{
		private  static MessageBrokerImpl instance = new MessageBrokerImpl();
	}

	private Map< Class<? extends Message>, LinkedList<Subscriber>> subscribersByTopic;
	private Map<Subscriber, ConcurrentLinkedQueue<Message>> MSGqueue;
	private Map<Message, Future> EventFutureMap;

	//constructor
	private MessageBrokerImpl() {
		subscribersByTopic = new HashMap<>();
		MSGqueue = new HashMap<>();
		EventFutureMap = new HashMap<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return singletonHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) { synchronized (subscribersByTopic){
		if (subscribersByTopic.containsKey(type)){
			LinkedList<Subscriber> subscribers = subscribersByTopic.get(type);
			if (!subscribers.contains(m)) {
				subscribers.addLast(m);
				subscribersByTopic.put(type, subscribers);
			}
		}
		else {
			LinkedList<Subscriber> newSub = new LinkedList<Subscriber>(){};
			newSub.add(m);
			subscribersByTopic.put(type,newSub);
		}
	}}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {synchronized (subscribersByTopic){
		if (subscribersByTopic.containsKey(type)){
			LinkedList<Subscriber> subscribers = subscribersByTopic.get(type);
			if (!subscribers.contains(m)) {
				subscribers.addLast(m);
				subscribersByTopic.put(type, subscribers);
			}
		}
		else {
			LinkedList<Subscriber> newSub = new LinkedList<Subscriber>(){};
			newSub.add(m);
			subscribersByTopic.put(type,newSub);
		}
	}}

	@Override
	public <T> void complete(Event<T> e, T result) { EventFutureMap.get(e).resolve(result); }

	@Override
	public synchronized void sendBroadcast(Broadcast b) {
			if (subscribersByTopic.containsKey(b.getClass())) {
				for (Subscriber s : subscribersByTopic.get(b.getClass())) {
					synchronized (MSGqueue.get(s)){
						MSGqueue.get(s).add(b);
						MSGqueue.get(s).notifyAll();
				}
			}
		}
	}

	@Override
	public synchronized <T> Future sendEvent(Event<T> e) {
			if (subscribersByTopic.containsKey(e.getClass()) && !subscribersByTopic.get(e.getClass()).isEmpty()) { //check if the topic is found and there are subscribers in his list
				Subscriber s = subscribersByTopic.get(e.getClass()).poll();
				synchronized (MSGqueue.get(s)) {
					MSGqueue.get(s).add(e);
					subscribersByTopic.get(e.getClass()).addLast(s);
					Future f = new Future();
					EventFutureMap.put(e, f);
					MSGqueue.get(s).notifyAll();
					return f;
				}
			}
		return null;
	}

	@Override
	public void register(Subscriber m) {
			if (!MSGqueue.containsKey(m)) {
				ConcurrentLinkedQueue q = new ConcurrentLinkedQueue();
				MSGqueue.put(m, q);
			}

	}

	@Override
	public synchronized void unregister(Subscriber m) {
		if (MSGqueue.containsKey(m)){
			MSGqueue.remove(m);
		}
		for (Map.Entry< Class<? extends Message>, LinkedList<Subscriber>> element : subscribersByTopic.entrySet()){
			if(element.getValue().contains(m)){
				element.getValue().remove(m);
			}
		}
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		if (!MSGqueue.containsKey(m))
			throw new IllegalStateException("the Subscriber " + m + " has not registered yet");
		synchronized (MSGqueue.get(m)) {
				while (MSGqueue.get(m).isEmpty()) {
					try{ MSGqueue.get(m).wait();}
					catch (InterruptedException e){ Thread.currentThread().interrupt();}
				}
				return MSGqueue.get(m).poll();
			}
	}

}
