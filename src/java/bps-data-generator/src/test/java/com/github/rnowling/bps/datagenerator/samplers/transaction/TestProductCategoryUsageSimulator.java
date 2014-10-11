package com.github.rnowling.bps.datagenerator.samplers.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.samplers.transaction.ProductCategoryUsageSimulator;
import com.github.rnowling.bps.datagenerator.samplers.transaction.ProductCategoryUsageTrajectory;
import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;

public class TestProductCategoryUsageSimulator
{
	
	@Test
	public void testSimulate() throws Exception
	{
		SeedFactory seedFactory = new SeedFactory(1234);
		
		ProductCategoryUsageSimulator simulator = new ProductCategoryUsageSimulator(2.0, 1.0, 1.0, seedFactory);
		
		ProductCategoryUsageTrajectory trajectory = simulator.simulate(0.0, 30.0);
		
		assertEquals(0.0, trajectory.getLastAmount(), 0.0001);
		
		Pair<Double, Double> previousEntry = trajectory.getStep(0);
		for(int i = 1; i < trajectory.size(); i++)
		{
			Pair<Double, Double> entry = trajectory.getStep(i);
			// time should move forward
			assertTrue(previousEntry.getFirst() <= entry.getFirst());
			// remaining amounts should go down
			assertTrue(previousEntry.getSecond() >= entry.getSecond());
			previousEntry = entry;
		}
	}

}
