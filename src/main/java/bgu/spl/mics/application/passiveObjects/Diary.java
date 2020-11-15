package bgu.spl.mics.application.passiveObjects;

import java.io.File;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {

	private static class singletonHolder {
		private static Diary instance = new Diary();
	}

	private List<Report> reports;
	private AtomicInteger total;

	private Diary(){
		reports = new LinkedList<Report>();
		total= new AtomicInteger();
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Diary getInstance() {
		return singletonHolder.instance;
	}

	public List<Report> getReports() {
		return reports;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd) {
		synchronized (reports) {
			reports.add(reportToAdd);
			System.out.println("a report was add to dairy");
		}
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		PrintWriter writer = null;
		File f = new File(filename);
		String report = new Gson().toJson(reports);
		try{
			writer= new PrintWriter(filename, "UTF-8");
			}
		catch (IOException exp){}
		assert writer != null;
		writer.write(report);
		writer.write(" total missions: " + this.total);
		writer.close();
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		return total.intValue();
	}

	public void incrementTotal(){
		total.incrementAndGet();
		System.out.println("the diary total now is :" + total);
	}

}
