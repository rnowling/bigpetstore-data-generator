package com.github.rnowling.bps.datagenerator.statistics.pdfs;

public interface ConditionalProbabilityDensityFunction<T, S>
{
	public double probability(T datum, S conditionalDatum);
	
	public ProbabilityDensityFunction<T> fixConditional(S conditionalDatum);
}
