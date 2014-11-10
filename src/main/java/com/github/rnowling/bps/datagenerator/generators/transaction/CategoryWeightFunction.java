package com.github.rnowling.bps.datagenerator.generators.transaction;

import com.github.rnowling.bps.datagenerator.framework.pdfs.ExponentialPDF;
import com.github.rnowling.bps.datagenerator.framework.pdfs.ProbabilityDensityFunction;
import com.github.rnowling.bps.datagenerator.framework.wfs.ConditionalWeightFunction;
import com.github.rnowling.bps.datagenerator.framework.wfs.WeightFunction;

public class CategoryWeightFunction implements ConditionalWeightFunction<Double, Double>
{
	private final ProbabilityDensityFunction<Double> pdf;
	
	public CategoryWeightFunction(double averagePurchaseTriggerTime)
	{
		double lambda = 1.0 / averagePurchaseTriggerTime;
		pdf = new ExponentialPDF(lambda);
	}
	
	@Override
	public double weight(Double exhaustionTime, Double transactionTime)
	{
		return fixConditional(transactionTime).weight(exhaustionTime);
	}
	
	@Override
	public WeightFunction<Double> fixConditional(final Double transactionTime)
	{
		return new WeightFunction<Double>()
			{
				public double weight(Double exhaustionTime)
				{
					double remainingTime = Math.max(0.0, exhaustionTime - transactionTime);
					return pdf.probability(remainingTime);
				}
			};
	}
}
