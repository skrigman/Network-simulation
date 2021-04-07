package simulation.tools;

import java.util.*;

//import org.json.*;

import java.io.*;
//import java.io.file.Files;

public class BuildingSim {

	public static BasicProblem preProccessing (int i, String filePath) {
	    File file = new File(filePath);
	    if (file.mkdirs()) {
	    	System.out.println("Directory " + filePath + " is created!");
	    }
		ProblemGenerator problemGenerator = new ProblemGenerator();
		return problemGenerator.genProblem(i, filePath);
	}
	
	public static void runTime(double  testDuration, BasicProblem problem) {
		Time time = new Time();
		SolutionGenerator solution = new SolutionGenerator();
		solution.run( problem );
		List<Packet> listOfPktsInCurTime = new ArrayList<Packet>();
		while ( time.getTime()<testDuration ) {
			listOfPktsInCurTime.clear();
			for (BasicTransmitter tx : problem.listOfTx) {
				if( time.minuteStart() ) {
					System.out.println("minute starting. time is " + time.getTime());
					tx.genPktListForNextMin( time );
				}
				tx.sendPacketIfNeeded( time, listOfPktsInCurTime );
			}
			for (BasicSniffer sniffer : problem.listOfSniffers) {
				sniffer.gotThesePkts(listOfPktsInCurTime);
			}
			time.nextTick();
		}
	}
	
	public static void postProccessing ( BasicProblem problem, int i, String filePath ) {
	    File dir = new File(filePath);
	    if (dir.mkdirs()) {
	    	System.out.println("Directory " + filePath + " is created!");
	    }
	    String txPacketsFile = filePath + "/pkts_" + i + ".json";
	    try {
	        FileWriter file = new FileWriter(txPacketsFile, true);
	        file.write(problem.pktListJSONObject.toString());
		    //System.out.println(problem.pktListJSONObject.toString());
	        file.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    String sniffedPacketsFile = filePath + "/sniffed_pkts_" + i + ".json";
	    try {
	        FileWriter file = new FileWriter(sniffedPacketsFile, true);
	        file.write(problem.SniffedPktListJSONObject.toString());
		    //System.out.println(problem.pktListJSONObject.toString());
	        file.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		//ResultsCheckers checker = new ResultsCheckers();
		//checker.test( problem );		
	}
	
	public static void runAproblem(int i, String testName, double testDuration) {
		BasicProblem problem = new BasicProblem();
		
		problem = preProccessing(i, testName + "/inputs");
		runTime( testDuration, problem );
		postProccessing( problem, i, testName + "/outputs");
	}
	
	public static void main(String[] args) {

		int numOfIterations = 2;
		double testDuration = 10*60*10;
		String directory = "./tests";
		String testName = "/first_test";
		
		String testPath = directory + testName;
	    File file = new File(testPath);
	    if (file.mkdirs()) {
	    	System.out.println("Directory " + testPath + " is created!");
	    } else {
	        System.out.println("Failed to create directory!");
	    }    
		for (int i=0;i<numOfIterations;i++) {
			System.out.println("Generating " + i + " problem");
			runAproblem ( i, testPath, testDuration );
		}
	}
}
