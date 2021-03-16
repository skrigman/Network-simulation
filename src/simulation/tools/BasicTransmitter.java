package simulation.tools;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class BasicTransmitter
{
	private int id;
	private int xLocation;
	private int yLocation;
	private int channel;
	private float txPower;
	private int txRate; //packets per minute
	
	private BasicProblem problem;

	private int pktCount;
	private List<Long> listOfPktsTime = new ArrayList<Long>();
	private List<Packet> listOfPkts = new ArrayList<Packet>();
	
	public BasicTransmitter (int id, int xLocation, int yLocation, int channel,float txPower, int txRate, BasicProblem problem )
	{
		this.id = id;
		this.xLocation = xLocation;
		this.yLocation = yLocation;
		this.channel = channel;
		this.txPower = txPower;
		this.txRate = txRate;	
		this.pktCount = 0;
		this.problem = problem;
	}
	
	public BasicTransmitter ( JSONObject jsonTx, BasicProblem problem )
	{
		this.problem = problem;
	    try {
		      //System.out.println(jsonTx.toString());
	    	this.id = (int)jsonTx.get("tx_id");
	    	this.xLocation = (int)jsonTx.get("X_location");
	    	this.yLocation = (int)jsonTx.get("Y_location");
	    	this.channel = (int)jsonTx.get("channel");
	    	this.txPower = (int)jsonTx.get("tx_power");
	    	this.txRate = (int)jsonTx.get("tx_rate");
		    } catch (JSONException e) {
		      System.out.println(jsonTx.toString());
		    }
	}
	
	public void setLocation (int x, int y) {
		this.xLocation = x;
		this.yLocation = y;
	}
	
	public String toString(){
		String txString = "ID=" + id + 
		          " x=" + xLocation +
		          " y=" + yLocation +
		          " channel=" + channel +
		          " power=" + txPower +
		          " rate=" + txRate +
		          "\n";
		return txString;
	}
	
	public int getId (){
		return id;
	}
	public int getX (){
		return xLocation;
	}
	public int getY (){
		return yLocation;
	}
	public int getChannel (){
		return channel;
	}
	public double getPower (){
		return txPower;
	}
	public int getRate (){
		return txRate;
	}
	public JSONObject genJSONTx () {
	    //Creating a JSONObject object
	    JSONObject jsonObject = new JSONObject();
	    //Inserting key-value pairs into the json object
	    try {
	      jsonObject.put("tx_id", this.id);
		  jsonObject.put("X_location", this.xLocation);
		  jsonObject.put("Y_location", this.yLocation);
		  jsonObject.put("channel", channel);
		  jsonObject.put("tx_power", this.txPower);
		  jsonObject.put("tx_rate", this.txRate);
	    } catch (JSONException e) {
	      System.out.println(jsonObject.toString());
	    }
	    return jsonObject;
	}

	public void genPktListForNextMin(Time t) {

  		System.out.println("Test it " + this.problem.toString());
		long pktTime;
		Random num = new Random();
		listOfPktsTime.clear();
		for ( int i=0; i<this.txRate; i++) {
			do {
				pktTime = num.nextInt(t.getTicksInMinute());
			} while (listOfPktsTime.contains(pktTime));
			listOfPktsTime.add(pktTime);
		}
	}
	
	public void sendPacketIfNeeded( Time t) {
		
		//System.out.println("time="+t.tickInMinute()+" inmin="+ listOfPktsTime.contains((Long)t.tickInMinute()));
		if ( listOfPktsTime.contains( t.tickInMinute() )) {
			genAPacket(this.pktCount, t);
			this.pktCount ++;
		}
	}
	
	private Packet genAPacket( int id, Time t) {

		Random num = new Random();
		int type = num.nextInt(this.problem.getNumOfPktTypes());
		int dstId = num.nextInt(this.problem.getNumOfRx());
		int length = this.problem.getPktLength();

		Packet pkt = new Packet(
				id, 
				this.id, 
				dstId,
				this.channel, 
				this.txPower, 
				t.getTime(), 
				type, 
				length, 
				this.xLocation, 
				this.yLocation);
		this.listOfPkts.add( pkt );
		
		return pkt;
	}
	public void writePktsToJson( String inputFile ) {
		
	    JSONArray pktListJSONObject = new JSONArray();
	  	for (Packet pkt : this.listOfPkts) {
	  		//System.out.println("this pkt json" + pkt.genJSONTx().toString());
	  		pktListJSONObject.put(pkt.genJSONTx());
		}
	    try {
	        FileWriter file = new FileWriter(inputFile, true);
	        file.write(pktListJSONObject.toString());
		    System.out.println(pktListJSONObject.toString());
	        file.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
