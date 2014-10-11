package com.github.rnowling.bps.datagenerator.samplers.transaction;

import com.github.rnowling.bps.datagenerator.datamodels.simulation.CustomerInventory;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;

public class ProposedPurchaseTimeSampler implements Sampler<Double>
{
	final CustomerInventory customerInventory;
	final Sampler<Double> arrivalTimeSampler;
	
	public ProposedPurchaseTimeSampler(CustomerInventory customerInventory,
			Sampler<Double> arrivalTimeSampler)
	{
		this.customerInventory = customerInventory;
		this.arrivalTimeSampler = arrivalTimeSampler;
	}
	
	protected double categoryProposedTime(double exhaustionTime) throws Exception
	{
		return Math.max(exhaustionTime - arrivalTimeSampler.sample(), 0.0);
	}
	
	public Double sample() throws Exception
	{
		double minProposedTime = Double.MAX_VALUE;
		for(Double exhaustionTime : this.customerInventory.getExhaustionTimes().values())
		{
			double proposedTime = this.categoryProposedTime(exhaustionTime);
			minProposedTime = Math.min(proposedTime, minProposedTime);
		}
		
		return minProposedTime;
	}
	
}
