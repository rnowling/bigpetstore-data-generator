package com.github.rnowling.bps.datagenerator.algorithms.pdfs;

public interface ProbabilityDensityFunction<T>
{
	public double probability(T datum);
}
