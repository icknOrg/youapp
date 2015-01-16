package youapp.services.standard;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import youapp.services.DistanceCalculationService;

public class StandardDistanceCalculationService implements DistanceCalculationService {

	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(StandardDistanceCalculationService.class);
	
	
	/***
	 * Method to calculate the distance between two geographic points via
	 * latitude and longitude coordinates. This Method returns a distance
	 * measured in miles.
	 * (c) by AndreasDistler@gmx.net
	 * 
	 * @param lat1: Latitude value for starting point.
	 * @param long1: Longitude value for starting point.
	 * @param lat2: Latitude value for target point.
	 * @param long2: Longitude value for target point.
	 * @return Distance between the two coordinate points in miles. 
	 */
	public double calcDistanceFromInMiles(double lat1, double long1, double lat2,
			double long2) {
		double earthRadius = 3958.75; // miles
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(long2 - long1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
				* Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist;
	}

	/***
	 * Method to calculate the distance between two geographic points via
	 * latitude and longitude coordinates. This Method returns a distance
	 * measured in kilometers.
	 * (c) by AndreasDistler@gmx.net
	 * 
	 * @param lat1: Latitude value for starting point.
	 * @param long1: Longitude value for starting point.
	 * @param lat2: Latitude value for target point.
	 * @param long2: Longitude value for target point.
	 * @return Distance between the two coordinate points in miles. 
	 */
	public double calcDistanceFromInKm(double lat1, double long1, double lat2,
			double long2) {
		double earthRadius = 6378.14; // kilometers
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(long2 - long1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
				* Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist;
	}
	
	/***
	 * Method to calculate the distance between two geographic points via
	 * latitude and longitude coordinates. This Method returns a distance
	 * measured in meters.
	 * (c) by AndreasDistler@gmx.net
	 * 
	 * @param lat1: Latitude value for starting point.
	 * @param long1: Longitude value for starting point.
	 * @param lat2: Latitude value for target point.
	 * @param long2: Longitude value for target point.
	 * @return Distance between the two coordinate points in miles. 
	 */
	public double calcDistanceFromInM(double lat1, double long1, double lat2,
			double long2) {
		double earthRadius = 6378137.00; // meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(long2 - long1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
				* Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist;
	}
	

/***
 * Calculates geodetic distance between two points specified by latitude/longitude using Vincenty inverse formula
 * for ellipsoids
 * 
 * @param lat1
 *            first point latitude in decimal degrees
 * @param lon1
 *            first point longitude in decimal degrees
 * @param lat2
 *            second point latitude in decimal degrees
 * @param lon2
 *            second point longitude in decimal degrees
 * @returns distance in meters between points with 5.10<sup>-4</sup> precision
 * @see <a href="http://www.movable-type.co.uk/scripts/latlong-vincenty.html">Originally posted here</a>
 */
public double distanceBetweenTwoPointsWithVincenty(double lat1, double lon1, double lat2, double lon2) {
    double a = 6378137, b = 6356752.314245, f = 1 / 298.257223563; // WGS-84 ellipsoid params
    double L = Math.toRadians(lon2 - lon1);
    double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat1)));
    double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat2)));
    double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
    double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

    double sinLambda, cosLambda, sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM;
    double lambda = L, lambdaP, iterLimit = 100;
    do {
        sinLambda = Math.sin(lambda);
        cosLambda = Math.cos(lambda);
        sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda)
                + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
        if (sinSigma == 0)
            return 0; // co-incident points
        cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
        sigma = Math.atan2(sinSigma, cosSigma);
        sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
        cosSqAlpha = 1 - sinAlpha * sinAlpha;
        cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
        if (Double.isNaN(cos2SigmaM))
            cos2SigmaM = 0; // equatorial line: cosSqAlpha=0 (§6)
        double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
        lambdaP = lambda;
        lambda = L + (1 - C) * f * sinAlpha
                * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
    } while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

    if (iterLimit == 0)
        return Double.NaN; // formula failed to converge

    double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
    double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
    double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
    double deltaSigma = B
            * sinSigma
            * (cos2SigmaM + B
                    / 4
                    * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM
                            * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
    double dist = b * A * (sigma - deltaSigma);

    return dist;
}

	
	
}
