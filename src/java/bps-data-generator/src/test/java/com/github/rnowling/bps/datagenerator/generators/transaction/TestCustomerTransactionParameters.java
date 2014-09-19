package com.github.rnowling.bps.datagenerator.generators.transaction;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class TestCustomerTransactionParameters
{

	@Test
	public void testCount() throws Exception
	{
		Multiset<PetSpecies> petCounts = HashMultiset.create();
		
		petCounts.add(PetSpecies.CAT);
		petCounts.add(PetSpecies.CAT);
		petCounts.add(PetSpecies.CAT);
		
		
		CustomerTransactionParameters transParams = new CustomerTransactionParameters(
				petCounts, 0.0, 0.0);
		
		
		assertTrue(transParams.countPetsBySpecies(PetSpecies.CAT) == 3);
		assertTrue(transParams.countPetsBySpecies(PetSpecies.DOG) == 0);
		assertTrue(transParams.countPets() == 3);
	}

}
