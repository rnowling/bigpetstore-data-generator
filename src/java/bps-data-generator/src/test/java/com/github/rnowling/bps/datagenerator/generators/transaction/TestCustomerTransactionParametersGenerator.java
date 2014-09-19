package com.github.rnowling.bps.datagenerator.generators.transaction;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.SeedFactory;

public class TestCustomerTransactionParametersGenerator
{

	@Test
	public void testGenerator() throws Exception
	{
		SeedFactory seedFactory = new SeedFactory(1234);
		CustomerTransactionParametersGenerator generator = new CustomerTransactionParametersGenerator(seedFactory);
		
		CustomerTransactionParameters transParams = generator.generate();
		
		assertTrue(transParams.countPets() >= Constants.MIN_PETS);
		assertTrue(transParams.countPets() <= Constants.MAX_PETS);
		assertTrue(transParams.getAveragePurchaseTriggerTime() >= Constants.PURCHASE_TRIGGER_TIME_MIN);
		assertTrue(transParams.getAveragePurchaseTriggerTime() <= Constants.PURCHASE_TRIGGER_TIME_MAX);
		assertTrue(transParams.getAverageTransactionTriggerTime() >= Constants.TRANSACTION_TRIGGER_TIME_MIN);
		assertTrue(transParams.getAverageTransactionTriggerTime() <= Constants.TRANSACTION_TRIGGER_TIME_MAX);
	}

}
