package youapp.MatchMaking;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class DistanceIntervalsWeights{

	@XmlElement(name = "distanceInterval", type = DistanceInterval.class)
	List<DistanceInterval> distanceInterval;

	public DistanceIntervalsWeights() {
		super();
		this.distanceInterval = new ArrayList<DistanceInterval>();

		
	}
	
	public List<DistanceInterval> getIntervals() {
		return distanceInterval;
	}

	public void setIntervals(List<DistanceInterval> distanceInterval) {
		this.distanceInterval = distanceInterval;
	}
	
	

}
