package youapp.converters;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;

public class DateStringConverter implements Converter<Date,String> {

	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(StringDateConverter.class);
	
	@Override
	public String convert(Date date) {
		if (log.isDebugEnabled()) {
			log.debug("Converting date to string: " + date);
		}
		if (date == null) {
			if (log.isDebugEnabled()) {
				log.debug("Date is null.");
			}
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR);
	}
	
}