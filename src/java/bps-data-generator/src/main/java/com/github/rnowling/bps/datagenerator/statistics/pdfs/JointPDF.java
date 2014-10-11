package com.github.rnowling.bps.datagenerator.statistics.pdfs;

import java.util.List;

public class JointPDF<T> implements ProbabilityDensityFunction<T>
{
	
	double normalizationFactor;
	ProbabilityDensityFunction<T>[] pdfs;
	
	public JointPDF(List<T> data, ProbabilityDensityFunction<T> ... pdfs)
	{
		this.pdfs = pdfs;
		
		normalizationFactor = 0.0d;
		for(T datum : data)
		{
			double prob = 1.0;
			for(ProbabilityDensityFunction<T> pdf : pdfs)
				prob *= pdf.probability(datum);
			normalizationFactor += prob;
		}
		
	}
	
	public double probability(T datum)
	{
		double weight = 1.0;
		for(ProbabilityDensityFunction<T> pdf : pdfs)
			weight *= pdf.probability(datum);
		
		return weight / normalizationFactor;
	}
}
