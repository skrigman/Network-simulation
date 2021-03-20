package simulation.tools;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class BasicSniffer {
	private int id;
	private Point location;
	private int channel;
	private int coverRange;
	private double rxPower;
	private boolean isDirectional;
	private double rxAngle;
	private double rxDirection;

	public BasicSniffer (
			int id, 
			Point p, 
			int channel,
			int coverRange,
			double rxPower, 
			boolean isDirectional,
			double rxAngle,
			double rxDirection)
	{
		this.id = id;
		this.location = p;
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
	    	Point location = new Point (
	    			Double.parseDouble(JSONSniffer.optString("X_location","none") ),
	    			Double.parseDouble(JSONSniffer.optString("Y_location","none") ));
	    	this.location = location;
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
		
	public void setLocation (Point p) {
		this.location = p;
	}
	public void setLocation (double x, double y) {
		this.location.setX(x);
		this.location.setY(y);
	}
	
	public void setDirection (double direction) {
		this.rxDirection = direction;
	}
	
	public String toString(){
		String txString = "ID=" + id + 
		          " location=" + location.toString() +
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
	public Point getLocation (){
		return location;
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
		  jsonObject.put("X_location", this.location.getX());
		  jsonObject.put("Y_location", this.location.getY());
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
		float pktPoweronSnifferLocation;
		System.out.println("Sniffer=" + this.getId() + listOfPktsInCurTime.toString());
		if ( !listOfPktsInCurTime.isEmpty() ) {
			for(Packet pkt : listOfPktsInCurTime) {
				//check distance to sender
				System.out.println("pkt = " + pkt.toString());
				double distanceToSender = this.location.distanceTo(pkt.getLocation());
				pktPoweronSnifferLocation = pkt.getPower();
				if ( distanceToSender > 5.0 ) {
					//reduction of 6db each 10 meters
					pktPoweronSnifferLocation -= (distanceToSender / 10) * 6;
				}
				if (pktPoweronSnifferLocation > this.rxPower) {
					gotThisPkt( pkt );
				}
			}
		}
	}
	private void gotThisPkt (Packet pkt) {
		
	}
}
