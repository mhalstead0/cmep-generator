package com.matthalstead.cmepgenerator;

import java.io.PrintWriter;
import java.time.LocalDate;

/**
 * 
 * @author Matt
 *
 */
public class OutputFileWriter {

	public static void printOutput(InputRecord inputRecord, PrintWriter pw, LocalDate usageDate) {
		LocalDate lastReadingDate = inputRecord.getLastReadingDate();
		int dayStepSize;
		if (usageDate.isBefore(lastReadingDate)) {
			dayStepSize = -1;
		} else {
			dayStepSize = 1;
		}
		//TODO finish
		
	}
	
}
