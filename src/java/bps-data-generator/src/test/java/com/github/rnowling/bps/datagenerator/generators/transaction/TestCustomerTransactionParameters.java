package com.github.rnowling.bps.datagenerator.generators.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class TestCustomerTransactionParameters
{

	@Test
	public void testCountPetsBySpecies() throws Exception
	{
		Multiset<PetSpecies> petCounts = HashMultiset.create();
		
		petCounts.add(PetSpecies.CAT);
		petCounts.add(PetSpecies.CAT);
		petCounts.add(PetSpecies.CAT);
		
		
		CustomerTransactionParameters transParams = new CustomerTransactionParameters(
				petCounts, 0.0, 0.0);
		
		
		assertEquals(transParams.countPetsBySpecies(PetSpecies.CAT), 3);
		assertEquals(transParams.countPetsBySpecies(PetSpecies.DOG), 0);
		assertEquals(transParams.countPets(), 3);
	}
	
	@Test
	public void testCountPetsByMultipleSpecies() throws Exception
	{
		Multiset<PetSpecies> petCounts = HashMultiset.create();
		
		petCounts.add(PetSpecies.CAT);
		petCounts.add(PetSpecies.CAT);
		petCounts.add(PetSpecies.DOG);
		
		
		CustomerTransactionParameters transParams = new CustomerTransactionParameters(
				petCounts, 0.0, 0.0);
		
		
		assertEquals(transParams.countPetsBySpecies(Arrays.asList(PetSpecies.values())), 3);
		assertEquals(transParams.countPets(), 3);
	}

}
