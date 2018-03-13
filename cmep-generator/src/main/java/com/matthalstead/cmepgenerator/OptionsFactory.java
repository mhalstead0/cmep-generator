package com.matthalstead.cmepgenerator;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * 
 * @author Matt
 *
 */
public class OptionsFactory {

	public static Options build() {
		Options options = new Options();
		for (OptionDef optionDef : OptionDef.values()) {
			Option option = new Option("" + optionDef.character, optionDef.longName, optionDef.hasArg, optionDef.description);
			option.setRequired(optionDef.required);
			options.addOption(option);
		}
		return options;
	}
	
	public static FileGenProperties getFileGenProperties(CommandLine cl) {
		FileGenProperties result = new FileGenProperties();
		result.setInputFile(new File(cl.getOptionValue(OptionDef.INPUT_FILE.character)));
		if (cl.hasOption(OptionDef.OUTPUT_FILE.character)) {
			result.setOutputFile(new File(cl.getOptionValue(OptionDef.OUTPUT_FILE.character)));
		}
		result.setUsageDate(LocalDate.parse(cl.getOptionValue(OptionDef.USAGE_DATE.character)));
		if (cl.hasOption(OptionDef.TIME_ZONE.character)) {
			String zoneIdString = cl.getOptionValue(OptionDef.TIME_ZONE.character);
			result.setZoneId(ZoneId.of(zoneIdString));
		}
		return result;
	}
	
	/**
	 * 
	 * @author Matt
	 *
	 */
	public static enum OptionDef {
		INPUT_FILE('i', "input", true, true, "Input file"),
		OUTPUT_FILE('o', "output", false, true, "Output file (defaults to stdout if not specified)"),
		USAGE_DATE('u', "usageDate", true, true, "Usage date ('yyyy-MM-dd')"),
		TIME_ZONE('z', "timeZOne", false, true, "Time zone (e.g. 'America/New_York', 'America/Chicago')");
		
		private final char character;
		private final String longName;
		private final boolean required;
		private final boolean hasArg;
		private final String description;
		
		private OptionDef(char character, String longName, boolean required, boolean hasArg, String description) {
			this.character = character;
			this.longName = longName;
			this.required = required;
			this.hasArg = hasArg;
			this.description = description;
		}
		
	}
}
