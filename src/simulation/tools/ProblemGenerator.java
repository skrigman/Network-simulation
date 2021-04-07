package simulation.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import org.json.*;

public class ProblemGenerator {

	BasicProblem problem;
	
	public ProblemGenerator ( ) {	}
	
	private JSONArray generateTxs ( int numOfTx){
		int channel = 0;
		float txPower = 10;
		int txRate = 100;
		
		Random num = new Random();
		for(int i=0;i<numOfTx;i++){
			Point location = new Point(
					(double)num.nextInt(this.problem.getMaxX()),
					(double)num.nextInt(this.problem.getMaxY()) );
			this.problem.listOfTx.add(new BasicTransmitter(i, location, channel, txPower, txRate, this.problem));
		}
		//Writing Transmitters list to JSON file
	    JSONArray txListJSONObject = new JSONArray();
	  	for (BasicTransmitter tx : this.problem.listOfTx) {
	  		txListJSONObject.put(tx.genJSONTx());
		}
	 	System.out.println(this.problem.listOfTx.toString());
		return txListJSONObject;
	}
	
	private void getTxFromJSON ( JSONArray JSONTxArray ){
		this.problem.setNumOfTx( JSONTxArray.length());
		for (int i=0; i<this.problem.getNumOfTx(); i++ ) {
			try {
				JSONObject JSONTx = (JSONObject)JSONTxArray.get(i);
				this.problem.listOfTx.add(new BasicTransmitter(JSONTx, this.problem));				
			} catch (JSONException e) {
				System.out.println("Failed read " + i + " Transmitter data");
			}
		}
	 	System.out.println(this.problem.listOfTx.toString());
	}
	
	private JSONArray generateSniffers ( int numOfSniffers){
		//Random num = new Random();
		for(int i=0;i<numOfSniffers;i++){
			Point location = new Point(0,0);
			int channel = 0;
			int coverRange = 50;
			double rxPower = 0.1; 
			boolean dir = false;
			double rxAngle = 0.1;
			double rxDir = 0.1;
			this.problem.listOfSniffers.add(new BasicSniffer(i, location, channel, coverRange, rxPower, dir, rxAngle, rxDir, this.problem));
		}
		//Writing Transmitters list to JSON array
	    JSONArray sniffersListJSONObject = new JSONArray();
	  	for (BasicSniffer sniffer : this.problem.listOfSniffers) {
	  		sniffersListJSONObject.put(sniffer.genJSONSniffer());
		}
	 	//System.out.println(this.problem.listOfSniffers.toString());
		return sniffersListJSONObject;
	}

	private void getSniffersFromJSON ( JSONArray JSONSniffersArray ){
		this.problem.setNumOfSniffers( JSONSniffersArray.length());
		for (int i=0; i<this.problem.getNumOfSniffers(); i++ ) {
			try {
				JSONObject JSONSniffer = (JSONObject)JSONSniffersArray.get(i);
				this.problem.listOfSniffers.add(new BasicSniffer(JSONSniffer, this.problem));				
			} catch (JSONException e) {
				System.out.println("Failed read " + i + " sniffer data");
			}
		}
	 	//System.out.println(this.problem.listOfSniffers.toString());
	}

	private JSONObject genJSONTestParams () {
				
      //Creating a JSONObject object
      JSONObject jsonObject = new JSONObject();
      //Inserting key-value pairs into the json object
      try {
    	  jsonObject.put("test_id", this.problem.getId());
	      jsonObject.put("max_X", this.problem.getMaxX());
	      jsonObject.put("max_Y", this.problem.getMaxY());
	      jsonObject.put("fixed_channel", true);
	      jsonObject.put("num_of_transmitters", this.problem.getNumOfTx());
	      jsonObject.put("num_of_sniffers", this.problem.getNumOfSniffers());
	      jsonObject.put("numOfPktTypes", this.problem.getNumOfPktTypes());
	      jsonObject.put("numOfRx", this.problem.getNumOfRx());
	      jsonObject.put("PktLength", this.problem.getPktLength());
      } catch (JSONException e) {
    	  System.out.println(jsonObject.toString());
      }
      return jsonObject;
	}
	
	private void getParamsFromJSON (JSONObject jsonObject) {
	      try {
	    	  this.problem.setId( (int) jsonObject.get("test_id"));
	    	  this.problem.setMaxX( (int) jsonObject.get("max_X"));
	    	  this.problem.setMaxY((int) jsonObject.get("max_Y"));
	    	  //this. = (int) jsonObject.get("fixed_channel", true);
	    	  this.problem.setNumOfTx( (int) jsonObject.get("num_of_transmitters"));
	    	  this.problem.setNumOfSniffers( (int) jsonObject.get("num_of_sniffers"));
	    	  this.problem.setNumOfPktTypes( (int) jsonObject.get("numOfPktTypes"));
	    	  this.problem.setNumOfRx( (int) jsonObject.get("numOfRx"));
	    	  this.problem.setPktLength( (int) jsonObject.get("PktLength"));
	      } catch (JSONException e) {
	    	  System.out.println(jsonObject.toString());
	      }	
	      System.out.println(this.toString());
	}

	private void generateTest(String inputFile, int i) {
		JSONObject jsonParams = genJSONTestParams( );
		JSONArray jsonTx = generateTxs ( this.problem.getNumOfTx());
		JSONArray jsonSniffers = generateSniffers ( this.problem.getNumOfSniffers());
	    JSONObject jsonObject = new JSONObject();
	    //Inserting key-value pairs into the json object
	    try {
	    	jsonObject.put("test_params", jsonParams);
		    jsonObject.put("transmitters", jsonTx);
		    jsonObject.put("sniffers", jsonSniffers);
	    } catch (JSONException e) {
	    	System.out.println(jsonObject.toString());
	    }
	    try {
	        FileWriter file = new FileWriter(inputFile, true);
	        file.write(jsonObject.toString());
		    System.out.println(jsonObject.toString());
	        file.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private void getTestFromJSON (String inputFile) {      
        File file = new File(inputFile);
		try (FileReader fr = new FileReader(file)) {
            char[] chars = new char[(int) file.length()];
            fr.read(chars);
            String str = new String();  
            String fileContent = str.valueOf(chars);  
    	    try {
    	        JSONObject jsonObject = new JSONObject(fileContent); 
    	        getParamsFromJSON ( (JSONObject)jsonObject.get("test_params"));
    		    getTxFromJSON ( (JSONArray)jsonObject.get("transmitters"));
    		    getSniffersFromJSON ( (JSONArray)jsonObject.get("sniffers"));
    	    } catch (JSONException e) {
    	    	System.out.println("Failed to read JSON content");
    	    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BasicProblem genProblem( int i, String testPath) {
		BasicProblem problem= new BasicProblem();
		this.problem = problem;
		String inputFile = testPath + "/test_" + i + ".json";
        File file = new File(inputFile);
        if (file.exists()) {
            System.out.println("Test input file exist, getting inputs from it.");
            getTestFromJSON(inputFile);
        } else {
            System.out.println("No test input file, generating it.");
            generateTest(inputFile, i);
        }	
        return this.problem;
	}
}

