package simulation.tools;

import java.util.Random;

public class SolutionGenerator {

	BasicProblem problem;

	public void run( BasicProblem problem ) {
		this.problem = problem;
		
	  	for (BasicSniffer sniffer : this.problem.listOfSniffers) {
			Random num = new Random();
			double x = num.nextInt(this.problem.getMaxX());
			double y = num.nextInt(this.problem.getMaxY());
			sniffer.setLocation(x, y);
		}		
		//return this.problem;
	}
}
