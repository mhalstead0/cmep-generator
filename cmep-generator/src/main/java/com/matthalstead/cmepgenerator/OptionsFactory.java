package com.matthalstead.cmepgenerator;

import java.io.File;
import java.time.LocalDate;

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
		USAGE_DATE('u', "usageDate", true, true, "Usage date ('yyyy-MM-dd')");
		
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
