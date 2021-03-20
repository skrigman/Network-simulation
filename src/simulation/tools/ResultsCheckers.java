package simulation.tools;

public class ResultsCheckers {
	
	private boolean inSniffingRange( BasicTransmitter tx, BasicSniffer sniffer) {
		//int fullTxPowerRange = 100;
		double decreaseSlop = 0.2;
		double realDistance = sniffer.getLocation().distanceTo(tx.getLocation());
		//double realDistance = txToSnifferDistance(tx, sniffer) - fullTxPowerRange - sniffer.getCoverRange();
		//System.out.println("realDistance " + realDistance);
		if ( realDistance <= 0) {
			//System.out.println("visited here");
			return true;
		} else {
			if ( sniffer.getPower() < (tx.getPower() * (1 - ( decreaseSlop * realDistance))) ) {
				//System.out.println("AND here");
				return true;
			} else {
				return false;
			}
		}
	}

	public void test( BasicProblem problem ) {
		int coveredTx = 0;
		for( BasicTransmitter tx : problem.listOfTx ) {
			for( BasicSniffer sniffer : problem.listOfSniffers ) {
				if ( inSniffingRange( tx, sniffer) ) {
					coveredTx = coveredTx + 1;
					break;
				}
			}
		}
		//target function: (number of covered transmitters) / (number of total transmitters)
		//System.out.println("coveredTx " + coveredTx);
		double coveringFunction = (double)coveredTx / (double)problem.getNumOfTx();
		System.out.println("cover function=" + coveringFunction);
	}
}
