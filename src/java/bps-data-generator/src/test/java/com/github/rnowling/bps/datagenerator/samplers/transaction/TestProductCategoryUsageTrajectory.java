package com.github.rnowling.bps.datagenerator.samplers.transaction;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.samplers.transaction.ProductCategoryUsageTrajectory;

public class TestProductCategoryUsageTrajectory
{
	
	@Test
	public void testTrajectory()
	{
		double initialAmount = 30.0;
		double initialTime = 0.0;
		
		ProductCategoryUsageTrajectory trajectory = new ProductCategoryUsageTrajectory(initialTime, initialAmount);
		
		assertEquals(trajectory.size(), 1);
		
		Pair<Double, Double> entry = trajectory.getStep(0);
		assertEquals(initialTime, entry.getFirst(), 0.0001);
		assertEquals(initialAmount, entry.getSecond(), 0.0001);
		
		trajectory.append(1.0, 25.0);
		
		assertEquals(2, trajectory.size());
		
		entry = trajectory.getStep(1);
		assertEquals(1.0, entry.getFirst(), 0.0001);
		assertEquals(25.0, entry.getSecond(), 0.0001);
		
		assertEquals(1.0, trajectory.getLastTime(), 0.0001);
		assertEquals(25.0, trajectory.getLastAmount(), 0.0001);
	}
	
	@Test
	public void testAmountAtTime()
	{
		ProductCategoryUsageTrajectory trajectory = new ProductCategoryUsageTrajectory(0.0, 30.0);
		trajectory.append(1.0, 25.0);
		trajectory.append(2.0, 20.0);
		trajectory.append(3.0, 50.0);
		trajectory.append(4.0, 40.0);
		trajectory.append(4.0, 50.0);
		trajectory.append(5.0, 30.0);
		
		assertEquals(30.0, trajectory.amountAtTime(0.5), 0.0001);
		assertEquals(50.0, trajectory.amountAtTime(4.0), 0.0001);
		assertEquals(30.0, trajectory.amountAtTime(10.0), 0.0001);
	}


}
