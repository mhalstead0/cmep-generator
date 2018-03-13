package com.matthalstead.cmepgenerator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

public abstract class Utils {
	private Utils() { } // never instantiated
	
	public static final int USAGE_SCALE = 3;
	
	public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
	
	public static final MathContext MATH_CONTEXT = new MathContext(USAGE_SCALE, ROUNDING_MODE);
	
	public static final FastDateFormat TIMESTAMP_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmm", TimeZone.getTimeZone("UTC"));
	
	public static BigDecimal parseUsageValue(String str) {
		BigDecimal bd = new BigDecimal(str);
		BigDecimal result = bd.setScale(USAGE_SCALE, ROUNDING_MODE);
		return result;
	}
	
	public static Instant getStartOfDay(LocalDate date, ZoneId zoneId) {
		ZonedDateTime zdt = date.atStartOfDay(ZoneId.systemDefault());
		return zdt.toInstant();
	}
	public static Instant getEndOfDay(LocalDate date, ZoneId zoneId) {
		return getStartOfDay(date.plusDays(1), zoneId);
	}
	
	public static String format(Instant instant) {
		String result = TIMESTAMP_FORMAT.format(instant.toEpochMilli());
		return result;
	}
}
