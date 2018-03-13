package com.matthalstead.cmepgenerator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public abstract class Utils {
	private Utils() { } // never instantiated
	
	public static final int USAGE_SCALE = 3;
	
	public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
	
	public static final MathContext MATH_CONTEXT = new MathContext(USAGE_SCALE, ROUNDING_MODE);
	
	public static BigDecimal parseUsageValue(String str) {
		BigDecimal bd = new BigDecimal(str);
		BigDecimal result = bd.setScale(USAGE_SCALE, ROUNDING_MODE);
		return result;
	}
}
