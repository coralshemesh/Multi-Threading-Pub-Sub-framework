package bgu.spl.mics.application.passiveObjects;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {
	private static class singletonHolder {
		private static Squad instance = new Squad();
	}

	private Map<String, Agent> agents;

	private Squad(){
		agents = new HashMap<String , Agent>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		return singletonHolder.instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for (Agent a : agents){
			this.agents.put(a.getSerialNumber(),a);
		}
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		synchronized (agents) {
			for (String s : serials) {
				agents.get(s).release();
			}
			System.out.println("agents released:" + serials.toString());
			agents.notifyAll();
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public synchronized void sendAgents(List<String> serials, int time){
		 try {
			 System.out.println("agents sent: "+ serials.toString());
		 	Thread.sleep(time*100);
		 	releaseAgents(serials);
		 }
		 catch (InterruptedException e){
		 	Thread.currentThread().interrupt();
		 	releaseAgents(serials);
		 }
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials) {
		synchronized (agents) {
			for (String s : serials) {
				if (!agents.containsKey(s)) {
					System.out.println("agent not found");
					return false;
				}
			}
					try {
						while (!agentsAreReady(serials)) agents.wait();
						for (String string: serials){ agents.get(string).acquire();}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return false;
					}
		}
		return true;
	}

	private Boolean agentsAreReady(List<String> serials){
		for (String s : serials){
			if (!agents.get(s).isAvailable()) return false;
		}
		return true;
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
		List<String> names = new LinkedList<String>();
		for (String s : serials){
			names.add(agents.get(s).getName());
		}
		return names;
    }

}
