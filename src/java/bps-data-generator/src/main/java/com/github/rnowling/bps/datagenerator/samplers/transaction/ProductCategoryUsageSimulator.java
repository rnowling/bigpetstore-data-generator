package com.github.rnowling.bps.datagenerator.samplers.transaction;

import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;
import com.github.rnowling.bps.datagenerator.statistics.samplers.ExponentialSampler;
import com.github.rnowling.bps.datagenerator.statistics.samplers.GaussianSampler;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;

public class ProductCategoryUsageSimulator
{
	final private double amountUsedAverage;
	final private double amountUsedVariance;
	
	final private Sampler<Double> timestepSampler;
	final private Sampler<Double> R;
	
	public ProductCategoryUsageSimulator(double dailyUsageRate, double amountUsedAverage,
			double amountUsedVariance, SeedFactory seedFactory)
	{
		this.amountUsedAverage = amountUsedAverage;
		this.amountUsedVariance = amountUsedVariance;
		
		timestepSampler = new ExponentialSampler(dailyUsageRate, seedFactory);
		R = new GaussianSampler(0.0, 1.0, seedFactory);
	}
	
	private void step(ProductCategoryUsageTrajectory trajectory) throws Exception
	{
		// given in days since last usage
		double timestep = timestepSampler.sample();
		
		double r = R.sample();
		
		// given in units per day
		double usageAmount = this.amountUsedAverage * timestep + 
				Math.sqrt(this.amountUsedVariance * timestep) * r;
		
		// can't use a negative amount
		usageAmount = Math.max(usageAmount, 0.0);
		
		double remainingAmount = Math.max(0.0, trajectory.getLastAmount() - usageAmount);
		double time = trajectory.getLastTime() + timestep;
		
		trajectory.append(time, remainingAmount);
	}
	
	public ProductCategoryUsageTrajectory simulate(double initialTime, double initialAmount) throws Exception
	{
		ProductCategoryUsageTrajectory trajectory = new ProductCategoryUsageTrajectory(initialTime, initialAmount);
		
		while(trajectory.getLastAmount() > 0.0)
		{
			step(trajectory);
		}
		
		return trajectory;
	}
}
