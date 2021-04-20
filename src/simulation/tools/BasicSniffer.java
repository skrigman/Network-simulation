package simulation.tools;

import java.util.List;
import java.math.*;
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
	private BasicProblem problem;

	public BasicSniffer (
			int id, 
			Point p, 
			int channel,
			int coverRange,
			double rxPower, 
			boolean isDirectional,
			double rxAngle,
			double rxDirection, BasicProblem problem )
	{
		this.id = id;
		this.location = p;
		this.channel = channel;
		this.coverRange = coverRange;
		this.rxPower = rxPower;
		this.isDirectional = isDirectional;
		this.rxAngle = rxAngle;	
		this.rxDirection = rxDirection;	
		this.problem = problem;
	}
	
	public BasicSniffer ( JSONObject JSONSniffer, BasicProblem problem ) {
	    this.problem = problem;
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
	private double pktPowerAtSnifferLocation ( Packet pkt) {
		double pktPowerAtSnifferLocation;
		double pwrReduction = 1.0;
		double distanceToSender = this.location.distanceTo(pkt.getLocation());
		//pktPowerAtSnifferLocation = pkt.getPower();
		double pwrReductionInDb = 0.0;
		if ( distanceToSender > 5.0 ) {
			//reduction of 6db each 10 meters
			pwrReductionInDb -= (distanceToSender / 10) * 6;
			pwrReduction = Math.pow(10.0, ( pwrReductionInDb / 10 ));
		}
		pktPowerAtSnifferLocation = pkt.getPower() * pwrReduction;
		return pktPowerAtSnifferLocation;
	}
	public void gotThesePkts ( List<Packet> listOfPktsInCurTime ) {
		//System.out.println("Sniffer=" + this.getId() + listOfPktsInCurTime.toString());
		if ( !listOfPktsInCurTime.isEmpty() ) {
			for(Packet pkt : listOfPktsInCurTime) {
				pkt.pwrAtSnifferLocation = pktPowerAtSnifferLocation(pkt);
			}
			if (listOfPktsInCurTime.size() == 1) {
				//Only one packet at this slot, check the distance
				Packet pkt = listOfPktsInCurTime.get(0);
				if (pkt.pwrAtSnifferLocation > this.rxPower) {
					gotThisPkt( pkt );
				}
			} else { //there are more than 1 pkt
				//look for the one with highest power:
				double maxPwr = 0;
				int pktIndex = 0;
				Packet pktWithMaxPwr;
				float pwrOfRestPkts = 0;
				for(Packet pkt : listOfPktsInCurTime) {
					if ( pkt.pwrAtSnifferLocation > maxPwr ) {
						pktIndex = listOfPktsInCurTime.indexOf(pkt);
						maxPwr = pkt.pwrAtSnifferLocation;
					}
				}		
				pktWithMaxPwr = listOfPktsInCurTime.get(pktIndex);
				listOfPktsInCurTime.remove(pktIndex);
				for(Packet pkt : listOfPktsInCurTime) {
					pwrOfRestPkts += pkt.pwrAtSnifferLocation;
				}
				if ((pktWithMaxPwr.pwrAtSnifferLocation > pwrOfRestPkts) &
					((pktWithMaxPwr.pwrAtSnifferLocation - pwrOfRestPkts) > this.rxPower)	) {
					gotThisPkt( pktWithMaxPwr );
				}
			}
		}
	}
	private void gotThisPkt (Packet pkt) {
  		this.problem.SniffedPktListJSONObject.put(pkt.genJSONPkt());
		
	}
}
