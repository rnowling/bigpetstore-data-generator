package com.github.rnowling.bps.datagenerator.framework.pdfs;

public class GaussianPDF implements ProbabilityDensityFunction<Double>
{
	private double mean;
	private double std;
	
	public GaussianPDF(double mean, double std)
	{
		this.mean = mean;
		this.std = std;
	}
	
	public double probability(Double value)
	{
		double diff = (mean - value) * (mean - value);
		double var = std * std;
		double exp = Math.exp(-1.0 * diff / (2.0 * var));
		
		return exp / (std * Math.sqrt(2.0 * Math.PI));
	}
}
