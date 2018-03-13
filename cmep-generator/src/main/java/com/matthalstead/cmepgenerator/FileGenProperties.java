package com.matthalstead.cmepgenerator;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;

public class FileGenProperties {

	private File inputFile;
	private File outputFile;
	private LocalDate usageDate;
	private ZoneId zoneId;
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
	public ZoneId getZoneId() {
		return zoneId;
	}
	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}
	
}
