package com.matthalstead.cmepgenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	
	public static void main(String[] args) {
		try {
			Options options = OptionsFactory.build();
			CommandLine cl = new PosixParser().parse(options, args);
			FileGenProperties fileGenProperties = OptionsFactory.getFileGenProperties(cl);
			
			File outputFile = fileGenProperties.getOutputFile();
			FileOutputStream fos;
			PrintWriter pw;
			if (outputFile == null) {
				fos = null;
				pw = new PrintWriter(System.out);
			} else {
				fos = new FileOutputStream(outputFile);
				pw = new PrintWriter(fos);
			}
			
			LocalDate usageDate = fileGenProperties.getUsageDate();
			List<InputRecord> inputRecords = InputFileParser.parseInputFile(fileGenProperties.getInputFile());
			for (InputRecord inputRecord : inputRecords) {
				OutputFileWriter.printOutput(inputRecord, pw, usageDate);
			}
			
			pw.flush();
			if (fos != null) {
				fos.close();
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error encountered in main", e);
		}
	}
	
	
}
