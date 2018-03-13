package com.matthalstead.cmepgenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * 
 * @author Matt
 *
 */
public class CMEPRecord {
	private String meterId;
	private String pod;
	private String units;
	private List<Triplet> triplets;
	
	
	
	public String getMeterId() {
		return meterId;
	}



	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}



	public String getPod() {
		return pod;
	}



	public void setPod(String pod) {
		this.pod = pod;
	}



	public String getUnits() {
		return units;
	}



	public void setUnits(String units) {
		this.units = units;
	}



	public List<Triplet> getTriplets() {
		return triplets;
	}



	public void setTriplets(List<Triplet> triplets) {
		this.triplets = triplets;
	}



	/**
	 * 
	 * @author Matt
	 *
	 */
	public static class Triplet {
		private Instant timestamp;
		private char dataQualityLetter;
		private int dataQualityFlags;
		private BigDecimal quantity;
		
		
		
		public Triplet(Instant timestamp, char dataQualityLetter, int dataQualityFlags, BigDecimal quantity) {
			super();
			this.timestamp = timestamp;
			this.dataQualityLetter = dataQualityLetter;
			this.dataQualityFlags = dataQualityFlags;
			this.quantity = quantity;
		}
		public Instant getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(Instant timestamp) {
			this.timestamp = timestamp;
		}
		public char getDataQualityLetter() {
			return dataQualityLetter;
		}
		public void setDataQualityLetter(char dataQualityLetter) {
			this.dataQualityLetter = dataQualityLetter;
		}
		public int getDataQualityFlags() {
			return dataQualityFlags;
		}
		public void setDataQualityFlags(int dataQualityFlags) {
			this.dataQualityFlags = dataQualityFlags;
		}
		public BigDecimal getQuantity() {
			return quantity;
		}
		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}
		
		
	}
}
