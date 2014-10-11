package com.github.rnowling.bps.datagenerator.statistics.pdfs;

public interface ProbabilityDensityFunction<T>
{
	public double probability(T datum);
}
