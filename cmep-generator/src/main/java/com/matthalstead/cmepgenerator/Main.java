package com.matthalstead.cmepgenerator;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	public static void main(String[] args) {
		try {
			File f = new File("data/input.txt");
			List<InputRecord> inputRecords = InputFileParser.parseInputFile(f);
			for (InputRecord inputRecord : inputRecords) {
				LOGGER.log(Level.INFO, "Input Record: " + inputRecord.getPremiseNumber());
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error encountered in main", e);
		}
	}
}
