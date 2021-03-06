package simulation.tools;

import org.json.JSONException;
import org.json.JSONObject;

public class Packet {

	private int packetId;
	private int srcId;
	private int dstId;
	private int length;
	private double timestamp;
	private int pktType;
	private int channel;
	private float power;
	private int xLocation;
	private int yLocation;
	
	public Packet (int id, 
			int srcId, int dstId, int ch, 
			float po, 
			double time, 
			int type, 
			int len, 
			int x, 
			int y) {
		this.packetId = id;
		this.srcId = srcId;
		this.dstId = dstId;
		this.channel = ch;
		this.power = po;
		this.timestamp = time;
		this.pktType = type;
		this.length = len;
		this.xLocation = x;
		this.yLocation = y;
		
		writePktToJson();
	}
	
	private void writePktToJson() {
		
	}
	public JSONObject genJSONTx () {
	    //Creating a JSONObject object
	    JSONObject jsonObject = new JSONObject();
	    //Inserting key-value pairs into the json object
	    try {
	      jsonObject.put("packetId", this.packetId);
		  jsonObject.put("srcId", this.srcId);
		  jsonObject.put("dstId", this.dstId);
		  jsonObject.put("channel", channel);
		  jsonObject.put("power", this.power);
		  jsonObject.put("timestamp", this.timestamp);
		  jsonObject.put("pktType", this.pktType);
		  jsonObject.put("length", this.length);
		  jsonObject.put("xLocation", this.xLocation);
		  jsonObject.put("yLocation", this.yLocation);
	    } catch (JSONException e) {
	      System.out.println(jsonObject.toString());
	    }
	    return jsonObject;
	}
	public int getId() {
		return packetId;
	}
	public int getSrcId() {
		return srcId;
	}
	public int getDstId() {
		return dstId;
	}
	public int getChannel() {
		return channel;
	}
	public float getPower() {
		return power;
	}
	public double getTimestamp() {
		return timestamp;
	}
	public int getPktType() {
		return pktType;
	}
	public int getLength() {
		return length;
	}
	public int getXLocation() {
		return xLocation;
	}
	public int getYLocation() {
		return yLocation;
	}

}
