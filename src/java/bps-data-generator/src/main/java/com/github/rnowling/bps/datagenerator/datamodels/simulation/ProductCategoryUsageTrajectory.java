package com.github.rnowling.bps.datagenerator.datamodels.simulation;

import java.util.List;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.google.common.collect.Lists;

public class ProductCategoryUsageTrajectory
{
	final private List<Pair<Double, Double>> trajectory;
	
	public ProductCategoryUsageTrajectory(double initialTime, double initialAmount)
	{
		trajectory = Lists.newArrayList();
		this.append(initialTime, initialAmount);
	}
	
	public void append(double time, double amount)
	{
		trajectory.add(Pair.create(time, amount));
	}
	
	public double getLastAmount()
	{
		return trajectory.get(trajectory.size() - 1).getSecond();
	}
	
	public double getLastTime()
	{
		return trajectory.get(trajectory.size() - 1).getFirst();
	}
	
	public double amountAtTime(double time)
	{
		Pair<Double, Double> previous = null;
		for(Pair<Double, Double> entry : trajectory)
		{
			if(entry.getFirst() > time)
				break;
			previous = entry;
		}
		
		if(previous == null)
			return 0.0;
		
		return previous.getSecond();
	}
	
	public Pair<Double, Double> getStep(int idx)
	{
		return trajectory.get(idx);
	}
	
	public int size()
	{
		return trajectory.size();
	}
}
