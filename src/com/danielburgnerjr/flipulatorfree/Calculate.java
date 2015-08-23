package com.danielburgnerjr.flipulatorfree;

import java.io.Serializable;

public class Calculate implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String strAddress;		// address
	private String strCityStZip;	// city state ZIP code
	private int nSquareFootage;		// square footage
	private int nBedrooms;			// number of bedrooms
	private int nBathrooms;			// number of bathrooms
	private double dSalesPrice;		// sales price
	private double dFMVARV;			// FMV/ARV
	private double dBudget;         // budget

	public String getAddress() {
		return strAddress;
	}

	public void setAddress(String strAdd) {
		this.strAddress = strAdd;
	}

	public String getCityStZip() {
		return strCityStZip;
	}

	public void setCityStZip(String strCSZ) {
		this.strCityStZip = strCSZ;
	}

	public int getSquareFootage() {
		return nSquareFootage;
	}

	public void setSquareFootage(int nSF) {
		this.nSquareFootage = nSF;
	}

	public int getBedrooms() {
		return nBedrooms;
	}

	public void setBedrooms(int nBR) {
		this.nBedrooms = nBR;
	}

	public int getBathrooms() {
		return nBathrooms;
	}

	public void setBathrooms(int nBA) {
		this.nBathrooms = nBA;
	}
	
	public double getSalesPrice() {
		return dSalesPrice;
	}

	public void setSalesPrice(double dSP) {
		this.dSalesPrice = dSP;
	}

	public double getFMVARV() {
		return dFMVARV;
	}

	public void setFMVARV(double dF) {
		this.dFMVARV = dF;
	}
	
	public double getBudget() {
		return dBudget;		// returns budget
	}

	public void setBudget(double dB) {
		this.dBudget = dB;		
	}


	public void calcBudgetRehabType(String strT) {
		String strType = strT; // stores rehab type
		
		// determines budget based on type and square footage
		if (strType.equals("Low")) {
			this.dBudget = (15 * this.nSquareFootage);
		} else if (strType.equals("Medium")) {
			if (this.nSquareFootage < 1500)
				this.dBudget = (25 * this.nSquareFootage);
			else	
				this.dBudget = (20 * this.nSquareFootage);
		} else if (strType.equals("High")) {
			this.dBudget = (30 * this.nSquareFootage);
		} else if (strType.equals("Super-High")) {
			this.dBudget = (40 * this.nSquareFootage);
		} else {				
			this.dBudget = (125 * this.nSquareFootage);
		}
	}

	@Override
    public String toString() {
        return "Location\nAddress: " + strAddress + "\nCity, State ZIP: " + strCityStZip + 
        	   "\nSquare Footage: " + nSquareFootage + "\nBedrooms: " + nBedrooms + "\nBathrooms: " + nBathrooms;
    }  
}
