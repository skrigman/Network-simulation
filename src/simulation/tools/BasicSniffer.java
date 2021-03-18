package simulation.tools;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class BasicSniffer {
	private int id;
	private int xLocation;
	private int yLocation;
	private int channel;
	private int coverRange;
	private double rxPower;
	private boolean isDirectional;
	private double rxAngle;
	private double rxDirection;

	public BasicSniffer (
			int id, 
			int xLocation, 
			int yLocation, 
			int channel,
			int coverRange,
			double rxPower, 
			boolean isDirectional,
			double rxAngle,
			double rxDirection)
	{
		this.id = id;
		this.xLocation = xLocation;
		this.yLocation = yLocation;
		this.channel = channel;
		this.coverRange = coverRange;
		this.rxPower = rxPower;
		this.isDirectional = isDirectional;
		this.rxAngle = rxAngle;	
		this.rxDirection = rxDirection;	
	}
	
	public BasicSniffer ( JSONObject JSONSniffer ) {
	    try {
		    //System.out.println(JSONSniffer.toString());
	    	this.id = (int)JSONSniffer.get("sniffer_id");
	    	this.xLocation = (int)JSONSniffer.get("X_location");
	    	this.yLocation = (int)JSONSniffer.get("Y_location");
	    	this.channel = (int)JSONSniffer.get("channel");
	    	this.coverRange = (int)JSONSniffer.get("cover_range");
	    	this.rxPower = (double)JSONSniffer.get("sniffer_power");
	    	this.isDirectional = (boolean)JSONSniffer.get("directional_sniffer");
	    	this.rxAngle = (double)JSONSniffer.get("sniffer_angle");	
	    	this.rxDirection = (double)JSONSniffer.get("sniffer_direction");			
	    } catch (JSONException e) {
		    System.out.println(JSONSniffer.toString());
		}
	    //System.out.println(this.toString());
	}
		
	public void setLocation (int x, int y) {
		this.xLocation = x;
		this.yLocation = y;
	}
	
	public void setDirection (double direction) {
		this.rxDirection = direction;
	}
	
	public String toString(){
		String txString = "ID=" + id + 
		          " x=" + xLocation +
		          " y=" + yLocation +
		          " channel=" + channel +
		          " coverR=" + coverRange +
		          " power=" + rxPower;
		if (isDirectional) {
			txString = txString + " angle=" + rxAngle + " direction =" + rxDirection + "\n";
		}
		else {
			txString = txString + "\n";
		}
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
	public int getCoverRange (){
		return coverRange;
	}
	public double getPower (){
		return rxPower;
	}
	public double getAngle (){
		return rxAngle;
	}
	public double getDirection (){
		return rxDirection;
	}
	public boolean isDirection () {
		return isDirectional;
	}
	public JSONObject genJSONSniffer () {
	    //Creating a JSONObject object
	    JSONObject jsonObject = new JSONObject();
	    //Inserting key-value pairs into the json object
	    try {
	      jsonObject.put("sniffer_id", this.id);
		  jsonObject.put("X_location", this.xLocation);
		  jsonObject.put("Y_location", this.yLocation);
		  jsonObject.put("channel", channel);
		  jsonObject.put("cover range", coverRange);
		  jsonObject.put("sniffer_power", this.rxPower);
		  jsonObject.put("sniffer_angle", this.rxAngle);
		  jsonObject.put("sniffer_direction", this.rxDirection);
		  jsonObject.put("directional_sniffer", this.isDirectional);
	    } catch (JSONException e) {
	      System.out.println(jsonObject.toString());
	    }
	    return jsonObject;
	}
	public void gotThesePkts ( List<Packet> listOfPktsInCurTime ) {
		System.out.println("Sniffer=" + this.getId() + listOfPktsInCurTime.toString());
		if ( !listOfPktsInCurTime.isEmpty() ) {
			for(Packet pkt : listOfPktsInCurTime) {
				//check distance to sender
				System.out.println("pkt = " + pkt.toString());
				if ( listOfPktsInCurTime.size() == 1 ) {
				}
			}
		}
	}
}
