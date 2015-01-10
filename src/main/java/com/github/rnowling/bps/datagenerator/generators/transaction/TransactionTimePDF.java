package com.github.rnowling.bps.datagenerator.generators.transaction;

import com.github.rnowling.bps.datagenerator.framework.pdfs.ConditionalProbabilityDensityFunction;
import com.github.rnowling.bps.datagenerator.framework.pdfs.ProbabilityDensityFunction;

public class TransactionTimePDF implements
		ConditionalProbabilityDensityFunction<Double, Double>
{
	private final ConditionalProbabilityDensityFunction<Double, Double> arrowOfTimePDF;
	private final ProbabilityDensityFunction<Double> weatherPDF;
	
	public TransactionTimePDF(ConditionalProbabilityDensityFunction<Double, Double> arrowOfTimePDF,
			ProbabilityDensityFunction<Double> weatherPDF)
	{
		this.arrowOfTimePDF = arrowOfTimePDF;
		this.weatherPDF = weatherPDF;
	}
	
	public double probability(Double time, Double previousTime)
	{
		return arrowOfTimePDF.probability(time, previousTime) * weatherPDF.probability(time);
	}
}
