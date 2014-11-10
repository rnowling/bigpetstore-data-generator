package com.github.rnowling.bps.datagenerator.framework.pdfs;

public interface ProbabilityDensityFunction<T>
{
	public double probability(T datum);
}
