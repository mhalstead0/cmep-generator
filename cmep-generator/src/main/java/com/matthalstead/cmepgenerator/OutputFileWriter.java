package com.matthalstead.cmepgenerator;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

/**
 * 
 * @author Matt
 *
 */
public class OutputFileWriter {

	public static void printOutput(InputRecord inputRecord, PrintWriter pw, LocalDate usageDate) {
		LocalDate lastReadingDate = inputRecord.getLastReadingDate();
		boolean goingForward = usageDate.isAfter(lastReadingDate);
		BigDecimal startReading = null;
		BigDecimal endReading = null;
		if (goingForward) {
			endReading = inputRecord.getLastReading();
			LocalDate date = lastReadingDate.plusDays(1);
			while (!date.isAfter(usageDate)) {
				startReading = endReading;
				BigDecimal dayUsage = getDailyUsage(inputRecord, date);
				endReading = startReading.add(dayUsage);
				date = date.plusDays(1);
			}
		} else {
			endReading = inputRecord.getLastReading();
			LocalDate date = lastReadingDate;
			while (!date.isBefore(usageDate)) {
				BigDecimal dayUsage = getDailyUsage(inputRecord, date);
				endReading = endReading.subtract(dayUsage);
				date = date.minusDays(1);
			}
			startReading = endReading.subtract(getDailyUsage(inputRecord, usageDate));
		}
		pw.println(startReading + "-" + endReading);
		
		//TODO finish
		
	}
	
	private static BigDecimal getDailyUsage(InputRecord inputRecord, LocalDate date) {
		int hash = date.hashCode();
		if (inputRecord.getPremiseNumber() != null) {
			hash ^= inputRecord.getPremiseNumber().hashCode();
		}
		Random r = new Random(hash);
		double adu = inputRecord.getAverageDailyUsage().doubleValue();
		double stdev = adu / 4.0;
		if (stdev < 0.5) {
			stdev = 0.5;
		}
		double x = (r.nextDouble() * stdev) + adu;
		if (x < 0.0) {
			x = 0.0;
		}
		return new BigDecimal(x).setScale(Utils.USAGE_SCALE, Utils.ROUNDING_MODE);
	}
	
}
