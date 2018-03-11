package com.matthalstead.cmepgenerator;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 
 * @author Matt
 *
 */
public class InputRecord {
	private String meterId;
	private String premiseNumber;
	private String servicePointNumber;
	private String meterPointNumber;
	
	private LocalDate lastReadingDate;
	private BigDecimal lastReading;
	private BigDecimal averageDailyUsage;
	public String getMeterId() {
		return meterId;
	}
	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}
	public String getPremiseNumber() {
		return premiseNumber;
	}
	public void setPremiseNumber(String premiseNumber) {
		this.premiseNumber = premiseNumber;
	}
	public String getServicePointNumber() {
		return servicePointNumber;
	}
	public void setServicePointNumber(String servicePointNumber) {
		this.servicePointNumber = servicePointNumber;
	}
	public String getMeterPointNumber() {
		return meterPointNumber;
	}
	public void setMeterPointNumber(String meterPointNumber) {
		this.meterPointNumber = meterPointNumber;
	}
	public LocalDate getLastReadingDate() {
		return lastReadingDate;
	}
	public void setLastReadingDate(LocalDate lastReadingDate) {
		this.lastReadingDate = lastReadingDate;
	}
	public BigDecimal getLastReading() {
		return lastReading;
	}
	public void setLastReading(BigDecimal lastReading) {
		this.lastReading = lastReading;
	}
	public BigDecimal getAverageDailyUsage() {
		return averageDailyUsage;
	}
	public void setAverageDailyUsage(BigDecimal averageDailyUsage) {
		this.averageDailyUsage = averageDailyUsage;
	}
	
	
}
