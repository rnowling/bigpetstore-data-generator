package com.github.rnowling.bps.datagenerator.generators.transaction;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.github.rnowling.bps.datagenerator.generators.transaction.CustomerTransactionParametersBuilder;

public class TestCustomerTransactionParametersBuilder
{

	@Test
	public void testBuild() throws Exception
	{
		CustomerTransactionParametersBuilder builder = new CustomerTransactionParametersBuilder();
		
		builder.addPet(PetSpecies.DOG);
		builder.addPet(PetSpecies.DOG);
		builder.addPet(PetSpecies.DOG);
		
		builder.setAveragePurchaseTriggerTime(1.0);
		builder.setAverageTransactionTriggerTime(2.0);
		
		CustomerTransactionParameters transParams = builder.build();
		
		assertTrue(transParams.countPetsBySpecies(PetSpecies.DOG) == 3);
		assertTrue(transParams.countPetsBySpecies(PetSpecies.CAT) == 0);
		assertTrue(transParams.getAveragePurchaseTriggerTime() == 1.0);
		assertTrue(transParams.getAverageTransactionTriggerTime() == 2.0);
	}

}
