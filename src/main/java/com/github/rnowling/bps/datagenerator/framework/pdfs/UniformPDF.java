package com.github.rnowling.bps.datagenerator.framework.pdfs;

public class UniformPDF<T> implements ProbabilityDensityFunction<T>
{
	private final double probability;
	
	public UniformPDF(long count)
	{
		probability = 1.0 / ((double) count);
	}
	
	public UniformPDF(double probability)
	{
		this.probability = probability;
	}
	
	public double probability(T datum)
	{
		return probability;
	}
}
