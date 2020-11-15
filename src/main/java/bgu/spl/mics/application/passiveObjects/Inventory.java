package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

/**
 *  That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
	private static class singletonHolder {
		private static Inventory instance = new Inventory();
	}
	private List<String> gadgets;

	private Inventory(){
		gadgets = new LinkedList<String>();
	}
	/**
     * Retrieves the single instance of this class.
     */
	public static Inventory getInstance() {
		return singletonHolder.instance;
	}

	/**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (String[] inventory) {
		for (String s : inventory){
			gadgets.add(s);
		}
	}
	
	/**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     * @param gadget 		Name of the gadget to check if available
     * @return 	‘false’ if the gadget is missing, and ‘true’ otherwise
     */
	public boolean getItem(String gadget){
		if (gadgets.contains(gadget)){
			gadgets.remove(gadget);
			System.out.println(gadget + " is available");
			return true;
		}
		System.out.println(gadget + " is not available");
		return false;
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<String> which is a
	 * list of all the of the gadgets.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		PrintWriter writer = null;
		File f = new File(filename);
		String report = new Gson().toJson(gadgets);
		try{
			writer= new PrintWriter(filename, "UTF-8");
		}
		catch (IOException exp){}
		assert writer != null;
		writer.write(report);
		writer.close();
	}
}
