package youapp.services;

public interface DistanceCalculationService {
	
	public double calcDistanceFromInMiles(double lat1, double long1, double lat2,
			double long2);
	public double calcDistanceFromInKm(double lat1, double long1, double lat2,
			double long2);
	public double calcDistanceFromInM(double lat1, double long1, double lat2,
			double long2);
	public double distanceBetweenTwoPointsWithVincenty(double lat1, double lon1, double lat2, double lon2);
	

}
