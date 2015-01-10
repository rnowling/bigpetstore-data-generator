package com.github.rnowling.bps.datagenerator.generators.transaction;

import com.github.rnowling.bps.datagenerator.framework.pdfs.ConditionalProbabilityDensityFunction;

public class TransactionTimeArrowOfTimePDF implements ConditionalProbabilityDensityFunction<Double, Double>
{	
	public double probability(Double proposedTime, Double lastTransactionTime)
	{
		if(proposedTime >= lastTransactionTime)
		{
			return 1.0;
		}
		else
		{
			return 0.0;
		}
	}
}
