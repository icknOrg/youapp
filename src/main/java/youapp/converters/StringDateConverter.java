package youapp.converters;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;

public class StringDateConverter implements Converter<String,Date> {

	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(StringDateConverter.class);
	
	@Override
	public Date convert(String date) {
		if (log.isDebugEnabled()) {
			log.debug("Converting string to date: " + date);
		}
		if (date == null) {
			if (log.isDebugEnabled()) {
				log.debug("Date is null.");
			}
			return null;
		}
		try {
			String[] parts = date.split("/");
			if (parts.length != 3) {
				throw new IllegalArgumentException("Parts of given date string do not have the right format: Length does not equal 3.");
			}
			Integer month = Integer.parseInt(parts[0]);
			Integer day = Integer.parseInt(parts[1]);
			Integer year = Integer.parseInt(parts[2]);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DATE, day);
			cal.set(Calendar.MONTH, (month - 1));
			cal.set(Calendar.YEAR, year);
			return cal.getTime();
		}
		catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("Exception occurred: " + e.getMessage());
			}
			throw new IllegalArgumentException(e);
		}
	}

}
