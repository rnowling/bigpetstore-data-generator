package com.github.rnowling.bps.datagenerator.generators.transaction;

import com.github.rnowling.bps.datagenerator.framework.wfs.ConditionalWeightFunction;
import com.github.rnowling.bps.datagenerator.framework.wfs.WeightFunction;

public class CategoryWeightFunction implements ConditionalWeightFunction<Double, Double>
{
	private final double lambda;
	
	public CategoryWeightFunction(double averagePurchaseTriggerTime)
	{
		lambda = 1.0 / averagePurchaseTriggerTime;
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
					return lambda * Math.exp(-1.0 * lambda * remainingTime);
				}
			};
	}
}
