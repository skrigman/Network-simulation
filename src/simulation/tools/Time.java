package simulation.tools;

public class Time {

	private long currentTime;
	public String tickResolution = "1uSec";
	private int ticksInMinute = 10 * 60; // number of ticks per second * seconds in minute
	
	public String toString () {
		String string = "Current time is " + this.currentTime + tickResolution;
		return string;
	}
	public Time () {
		this.currentTime = 0;
	}
	
	public long getTime () {
		return this.currentTime;
	}
	
	public void nextTick() {
		++this.currentTime;
	}
	
	public int getTicksInMinute () {
		return this.ticksInMinute;
	}
	
	public boolean minuteStart () {
		return ( ( this.currentTime % ticksInMinute ) == 0 );
	}
	
	public long tickInMinute () {
		return this.currentTime % ticksInMinute;
	}
}
