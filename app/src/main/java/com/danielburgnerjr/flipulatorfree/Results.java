package com.danielburgnerjr.flipulatorfree;

import java.io.Serial;
import java.io.Serializable;

public class Results implements Serializable {
	
	@Serial
    private static final long serialVersionUID = 1L;
	
	private double dClosHoldCosts;	// closing/holding costs
	private double dProfit;			// profit
	private double dROI;         	// return on investment
	private double dCashOnCash;		// cash on cash return

	
	public double getClosHoldCosts() {
		return dClosHoldCosts;
	}

	public void setClosHoldCosts(double dRV) {
		this.dClosHoldCosts = (dRV * .1);
	}

	public double getProfit() {
		return dProfit;
	}

	public void setProfit(double dSP, double dRV, double dB) {
		this.dProfit = dRV - dSP - dB - this.dClosHoldCosts;
	}
	
	public double getROI() {
		return dROI;		// returns ROI
	}

	public void setROI(double dRV) {
		this.dROI = (this.dProfit / dRV) * 100;		
	}

	public double getCashOnCash() {
		return dCashOnCash;		// returns cash on cash
	}

	public void setCashOnCash(double dB) {
		this.dCashOnCash = (this.dProfit / (dB + this.dClosHoldCosts)) * 100;		
	}

}
