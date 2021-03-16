package simulation.tools;

import java.util.ArrayList;
import java.util.List;

public class BasicProblem {
	//test parameters
	private int testId = 0;
	private int numOfTx = 10;
	private int numOfSniffers = 5;
	private int maxX = 500;
	private int maxY = 500;
	private int numOfPktTypes = 10;
	private int numOfRx = 100;
	private int PktLength = 10;

	public List<BasicTransmitter> listOfTx = new ArrayList<BasicTransmitter>();
	public List<BasicSniffer> listOfSniffers = new ArrayList<BasicSniffer>();

	public String toString () {
		return "testId=" + this.testId + 
			   " maxX=" + maxX + 
			   " maxY=" + maxY + 
			   " numOfTx=" + numOfTx + 
			   " numOfSniffers=" + numOfSniffers + 
			   "\nTtrnasmitters:\n" + listOfTx.toString() + 
			   "\nSniffers:\n" + listOfSniffers.toString();
	}
	public int getId () {
		return testId;
	}
	public int getNumOfTx () {
		return numOfTx;
	}
	public int getNumOfSniffers () {
		return numOfSniffers;
	}
	public int getMaxX () {
		return maxX;
	}
	public int getMaxY () {
		return maxY;
	}
	public int getNumOfPktTypes () {
		return numOfPktTypes;
	}
	public int getNumOfRx () {
		return numOfRx;
	}
	public int getPktLength () {
		return PktLength;
	}
	public void setId ( int id) {
		this.testId = id;
	}
	public void setMaxX (int maxX) {
		this.maxX = maxX;
	}
	public void setMaxY (int maxY) {
		this.maxY = maxY;
	}
	public void setNumOfTx (int numOfTx) {
		this.numOfTx = numOfTx;
	}
	public void setNumOfSniffers (int numOfSniffers) {
		this.numOfSniffers = numOfSniffers;
	}
	public void setNumOfPktTypes ( int numOfPktTypes ) {
		this.numOfPktTypes =numOfPktTypes;
	}
	public void setNumOfRx ( int numOfRx ) {
		this.numOfRx = numOfRx;
	}
	public void setPktLength ( int PktLength ) {
		this.PktLength = PktLength;
	}

}
