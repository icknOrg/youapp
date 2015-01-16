package youapp.MatchMaking;

public class DistanceInterval {

	private double left;
	private Boolean leftClosed;
	private double right;
	private Boolean rightClosed;
	private double weight;
	
	public double getLeft() {		
		return left;
	}
	
	public void setLeft(double left) {
		this.left = left;
	}
	
	public Boolean isLeftClosed() {
		return leftClosed;
	}
	
	public void setLeftClosed(Boolean leftClosed) {
		
		if (leftClosed != null )		
			this.leftClosed = leftClosed;
		else 
			this.leftClosed=false;
	}
	
	public double getRight() {
		return right;
	}
	
	public void setRight(double right) {
		this.right = right;
	}
	
	public Boolean isRightClosed() {
		return rightClosed;
	}
	
	public void setRightClosed(Boolean rightClosed) {
		
		if(rightClosed !=null)
			this.rightClosed = rightClosed;
		else
			this.rightClosed = true;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}		
	
}
