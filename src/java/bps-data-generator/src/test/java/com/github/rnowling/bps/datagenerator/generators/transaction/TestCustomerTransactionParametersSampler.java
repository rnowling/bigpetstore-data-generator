package com.github.rnowling.bps.datagenerator.generators.transaction;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.generators.transaction.CustomerTransactionParametersSamplerBuilder;
import com.github.rnowling.bps.datagenerator.resources.Constants;

public class TestCustomerTransactionParametersSampler
{

	@Test
	public void testSample() throws Exception
	{
		SeedFactory seedFactory = new SeedFactory(1234);
		CustomerTransactionParametersSamplerBuilder builder = new CustomerTransactionParametersSamplerBuilder(seedFactory);
		Sampler<CustomerTransactionParameters> sampler = builder.build();
		
		CustomerTransactionParameters transParams = sampler.sample();
		
		assertTrue(transParams.countPets() >= Constants.MIN_PETS);
		assertTrue(transParams.countPets() <= Constants.MAX_PETS);
		assertTrue(transParams.getAveragePurchaseTriggerTime() >= Constants.PURCHASE_TRIGGER_TIME_MIN);
		assertTrue(transParams.getAveragePurchaseTriggerTime() <= Constants.PURCHASE_TRIGGER_TIME_MAX);
		assertTrue(transParams.getAverageTransactionTriggerTime() >= Constants.TRANSACTION_TRIGGER_TIME_MIN);
		assertTrue(transParams.getAverageTransactionTriggerTime() <= Constants.TRANSACTION_TRIGGER_TIME_MAX);
	}

}
