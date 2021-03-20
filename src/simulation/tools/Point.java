package simulation.tools;

public class Point {
	private double x;
	private double y;
	public Point (double x, double y) {
		setX(x);
		setY(y);
	}
	public void setX ( double x ) {
		this.x = x; 
	}
	public void setY ( double y ) {
		this.y = y; 
	}
	public double getX () {
		return this.x;
	}
	public double getY () {
		return this.y;
	}
	private double square (double x) { return x*x; }
	
	public double distanceTo(Point p) {
		double distance = Math.sqrt( square(p.getX() - this.getX()) + square(p.getY() - this.getY()));
		return distance;			
	}

	public String toString () {
		return "(" + getX() + "," + getY() + ")";
	}
}
