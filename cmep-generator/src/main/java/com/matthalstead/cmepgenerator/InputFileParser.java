package com.matthalstead.cmepgenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Matt
 *
 */
public class InputFileParser {

	public static List<InputRecord> parseInputFile(File f) throws IOException {
		List<InputRecord> result = new ArrayList<>();
		try (FileInputStream fis = new FileInputStream(f);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr); ) {
			String firstLine = br.readLine();
			if (firstLine != null) {
				firstLine = firstLine.replace("#", "");
				String[] firstLineSplit = firstLine.split(",");
				Map<String, Integer> fieldNameToIndex = mapToIndex(firstLineSplit);
				
				String line;
				while ((line = br.readLine()) != null) {
					InputRecord inputRecord = parseInputRecord(line, fieldNameToIndex);
					if (inputRecord != null) {
						result.add(inputRecord);
					}
				}
			}
		}
		
		return result;
	}
	
	private static InputRecord parseInputRecord(String line, Map<String, Integer> fieldNameToIndex) {
		String[] split = line.split(",");
		if (split.length < 7) {
			return null;
		}
		InputRecord result = new InputRecord();
		result.setMeterId(getFieldValue("meterId", split, fieldNameToIndex));
		result.setPremiseNumber(getFieldValue("premiseNumber", split, fieldNameToIndex));
		result.setServicePointNumber(getFieldValue("servicePointNumber", split, fieldNameToIndex));
		result.setMeterPointNumber(getFieldValue("meterPointNumber", split, fieldNameToIndex));
		
		String lastReadingDateString = getFieldValue("lastReadingDate", split, fieldNameToIndex);
		result.setLastReadingDate(LocalDate.parse(lastReadingDateString));
		
		String lastReadingString = getFieldValue("lastReading", split, fieldNameToIndex);
		result.setLastReading(Utils.parseUsageValue(lastReadingString));
		
		String averageDailyUsageString = getFieldValue("averageDailyUsage", split, fieldNameToIndex);
		result.setAverageDailyUsage(Utils.parseUsageValue(averageDailyUsageString));
		
		return result;
	}
	
	private static String getFieldValue(String fieldName, String[] fields, Map<String, Integer> fieldNameToIndex) {
		Integer index = fieldNameToIndex.get(fieldName);
		if (index == null) {
			return null;
		} else if (index < 0 || index >= fields.length) {
			return null;
		} else {
			return fields[index];
		}
	}
	
	
	private static Map<String, Integer> mapToIndex(String[] strings) {
		Map<String, Integer> result = new HashMap<>();
		for (int i=0; i<strings.length; i++) {
			result.put(strings[i], i);
		}
		return result;
	}
}
