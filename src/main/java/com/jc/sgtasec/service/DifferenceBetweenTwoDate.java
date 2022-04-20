package com.jc.sgtasec.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/*
 * Esta es una implementación basada en el código obtenido desde la web: 
 * https://www.geeksforgeeks.org/find-the-duration-of-difference-between-two-dates-in-java/
 */

@Service
public class DifferenceBetweenTwoDate {
	private Logger logger = LogManager.getLogger(getClass());

	public DifferenceBetweenTwoDate() {
		super();
	}

	// Function to print difference in
	// time start_date and end_date
	public String findDifference(String start_date, String end_date) {
		
		String differenceCalculated = "";
		
		// SimpleDateFormat converts the
		// string format to date object
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		// Try Class
		try {

			// parse method is used to parse
			// the text from a string to
			// produce the date
			Date d1 = sdf.parse(start_date);
			Date d2 = sdf.parse(end_date);

			// Calucalte time difference
			// in milliseconds
			long difference_In_Time = d2.getTime() - d1.getTime();

			// Calucalte time difference in seconds,
			// minutes, hours, years, and days
			long difference_In_Seconds = TimeUnit.MILLISECONDS.toSeconds(difference_In_Time) % 60;

			long difference_In_Minutes = TimeUnit.MILLISECONDS.toMinutes(difference_In_Time) % 60;

			long difference_In_Hours = TimeUnit.MILLISECONDS.toHours(difference_In_Time) % 24;

			long difference_In_Days = TimeUnit.MILLISECONDS.toDays(difference_In_Time) % 365;

			long difference_In_Years = TimeUnit.MILLISECONDS.toDays(difference_In_Time) / 365l;
	
			differenceCalculated += (difference_In_Years > 0)? difference_In_Years + " años, " : "";
			differenceCalculated += (difference_In_Days > 0)? difference_In_Days + " dias, " : "";
			differenceCalculated += (difference_In_Hours > 0)? difference_In_Hours + " horas, " : "";
			differenceCalculated += (difference_In_Minutes > 0)? difference_In_Minutes + " minutos, " : "";
			differenceCalculated += (difference_In_Seconds > 0)? difference_In_Seconds + " segundos, " : "";
					
		} catch (ParseException e) {			
			logger.error(e.getMessage());
		}
		
		return differenceCalculated;
	}
}
