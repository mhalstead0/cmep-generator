package com.matthalstead.cmepgenerator;

import java.io.File;
import java.time.LocalDate;

public class FileGenProperties {

	private File inputFile;
	private File outputFile;
	private LocalDate usageDate;
	public File getInputFile() {
		return inputFile;
	}
	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}
	public File getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}
	public LocalDate getUsageDate() {
		return usageDate;
	}
	public void setUsageDate(LocalDate usageDate) {
		this.usageDate = usageDate;
	}
	
	
	
}
