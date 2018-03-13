package com.matthalstead.cmepgenerator;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
		
		CMEPRecord singleIntervalRecord = getIntervalRecord(inputRecord, usageDate, startReading, endReading, zoneId);
		List<CMEPRecord> splitIntervalRecords = getCMEPRecords(singleIntervalRecord, 48);
		for (CMEPRecord intervalRecord : splitIntervalRecords) {
			write(intervalRecord, pw);
		}
		//TODO finish
		
	}
	
	private static CMEPRecord getIntervalRecord(InputRecord inputRecord, LocalDate usageDate, BigDecimal startReading, BigDecimal endReading, ZoneId zoneId) {
		List<CMEPRecord.Triplet> triplets = new ArrayList<>();
		BigDecimal remainingUsage = endReading.subtract(startReading);
		BigDecimal approxIntervalUsage = remainingUsage.divide(new BigDecimal("24.000"), Utils.ROUNDING_MODE).setScale(Utils.USAGE_SCALE, Utils.ROUNDING_MODE);
		Instant intervalStart = Utils.getStartOfDay(usageDate, zoneId);
		while (intervalStart.isBefore(Utils.getEndOfDay(usageDate, zoneId))) {
			Instant intervalEnd = intervalStart.plus(1, ChronoUnit.HOURS);
			
			BigDecimal intervalUsage = approxIntervalUsage;
			if (intervalUsage.compareTo(remainingUsage) > 0) {
				intervalUsage = remainingUsage;
			}
			remainingUsage = remainingUsage.subtract(intervalUsage);
			
			CMEPRecord.Triplet triplet = new CMEPRecord.Triplet(intervalEnd, 'R', 0, intervalUsage);
			triplets.add(triplet);
			intervalStart = intervalEnd;
		}
		
		Random r = new Random(1);
		for (int i=0; i<100; i++) {
			averageRandomValues(triplets, r);
		}
		
		CMEPRecord result = getCommonFields(inputRecord);
		result.setUnits("KWH");
		result.setTriplets(triplets);
		return result;
	}
	
	private static void averageRandomValues(List<CMEPRecord.Triplet> triplets, Random r) {
		int index1 = r.nextInt(triplets.size());
		int index2 = r.nextInt(triplets.size());
		if (index1 != index2) {
			double percentToStealFrom1 = r.nextDouble();
			double percentToStealFrom2 = r.nextDouble();
			CMEPRecord.Triplet t1 = triplets.get(index1);
			CMEPRecord.Triplet t2 = triplets.get(index2);
			BigDecimal toStealFrom1 = t1.getQuantity().multiply(new BigDecimal("" + percentToStealFrom1)).setScale(Utils.USAGE_SCALE, Utils.ROUNDING_MODE);
			BigDecimal toStealFrom2 = t2.getQuantity().multiply(new BigDecimal("" + percentToStealFrom2)).setScale(Utils.USAGE_SCALE, Utils.ROUNDING_MODE);
			t1.setQuantity(t1.getQuantity().subtract(toStealFrom1));
			t2.setQuantity(t2.getQuantity().subtract(toStealFrom2));
			CMEPRecord.Triplet tMiddle = triplets.get((index1 + index2) / 2);
			t2.setQuantity(tMiddle.getQuantity().add(toStealFrom1).add(toStealFrom2));
		}
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
