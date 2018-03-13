package com.matthalstead.cmepgenerator;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Matt
 *
 */
public class OutputFileWriter {

	public static void printOutput(InputRecord inputRecord, PrintWriter pw, LocalDate usageDate, ZoneId zoneId) {
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
		
		CMEPRecord registerRecord = getRegisterRecord(inputRecord, usageDate, startReading, endReading, zoneId);
		write(registerRecord, pw);
		
		//TODO finish
		
	}
	
	private static void write(CMEPRecord cmepRecord, PrintWriter pw) {
		pw.print("MEPMD01");
		pw.print(",20080101");
		pw.print("," + cmepRecord.getPod());
		pw.print("," + cmepRecord.getMeterId());
		pw.print("," + cmepRecord.getUnits());
		pw.print("," + cmepRecord.getTriplets().size());
		for (CMEPRecord.Triplet triplet : cmepRecord.getTriplets()) {
			pw.print("," + Utils.format(triplet.getTimestamp()));
			pw.print("," + triplet.getDataQualityLetter() + triplet.getDataQualityFlags());
			pw.print("," + triplet.getQuantity());
		}
		pw.println();
	}
	
	private static List<CMEPRecord> getCMEPRecords(CMEPRecord cmepRecord, int maxIntervalCount) {

		List<List<CMEPRecord.Triplet>> tripletLists = batch(cmepRecord.getTriplets(), maxIntervalCount);
		if (tripletLists.size() <= 1) {
			return Arrays.asList(cmepRecord);
		} else {
			List<CMEPRecord> result = new ArrayList<>();
			for (List<CMEPRecord.Triplet> triplets : tripletLists) {
				CMEPRecord rec = new CMEPRecord();
				rec.setMeterId(cmepRecord.getMeterId());
				rec.setPod(cmepRecord.getPod());
				rec.setUnits(cmepRecord.getUnits());
				rec.setTriplets(triplets);
				result.add(rec);
			}
			return result;
		}
	}
	
	private static CMEPRecord getCommonFields(InputRecord inputRecord) {
		CMEPRecord result = new CMEPRecord();
		result.setMeterId(inputRecord.getMeterId());
		result.setPod(inputRecord.getPremiseNumber() + "_" + inputRecord.getServicePointNumber() + "_" + inputRecord.getMeterPointNumber());
		return result;
	}
	
	private static CMEPRecord getRegisterRecord(InputRecord inputRecord, LocalDate usageDate, BigDecimal startReading, BigDecimal endReading, ZoneId zoneId) {
		CMEPRecord result = getCommonFields(inputRecord);
		result.setUnits("KWHREG");
		List<CMEPRecord.Triplet> triplets = new ArrayList<>(2);
		triplets.add(new CMEPRecord.Triplet(Utils.getStartOfDay(usageDate, zoneId), 'R', 0, startReading));
		triplets.add(new CMEPRecord.Triplet(Utils.getEndOfDay(usageDate, zoneId), 'R', 0, endReading));
		result.setTriplets(triplets);
		return result;
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

	private static <T> List<List<T>> batch(List<T> list, int size) {
		if (size < 1) {
			size = 1;
		}
		List<List<T>> result = new ArrayList<>();
		for (T t : list) {
			if (result.isEmpty()) {
				result.add(new ArrayList<>());
			}
			List<T> last = result.get(result.size() - 1);
			if (last.size() >= size) {
				last = new ArrayList<>();
				result.add(last);
			}
			last.add(t);
			
		}
		return result;
	}
	

}
