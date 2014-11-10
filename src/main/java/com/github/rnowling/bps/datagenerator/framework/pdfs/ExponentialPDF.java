package com.github.rnowling.bps.datagenerator.framework.pdfs;

public class ExponentialPDF implements ProbabilityDensityFunction<Double>
{
	private final double lambda;
	
	public ExponentialPDF(double lambda)
	{
		this.lambda = lambda;
	}
	
	public double probability(Double value)
	{
		return lambda * Math.exp(-1.0 * value * lambda);
	}
}
