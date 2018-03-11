package com.matthalstead.cmepgenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Utils {
	private Utils() { } // never instantiated
	
	public static final int USAGE_SCALE = 3;
	
	public static BigDecimal parseUsageValue(String str) {
		BigDecimal bd = new BigDecimal(str);
		BigDecimal result = bd.setScale(USAGE_SCALE, RoundingMode.HALF_EVEN);
		return result;
	}
}
