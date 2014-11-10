package com.github.rnowling.bps.datagenerator.generators.customer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.datamodels.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.SequenceSampler;

public class TestCustomerSampler
{

	@Test
	public void testBuild() throws Exception
	{	
		SeedFactory factory = new SeedFactory(1234);
		
		Collection<String> nameList = Arrays.asList(new String[] {"Fred", "Gary", "George", "Fiona"});
		Collection<ZipcodeRecord> zipcodes = Arrays.asList(new ZipcodeRecord[] {				
				new ZipcodeRecord("11111", Pair.create(1.0, 1.0), "AZ", "Tempte", 30000.0, 100),
				new ZipcodeRecord("22222", Pair.create(2.0, 2.0), "AZ", "Phoenix", 45000.0, 200),
				new ZipcodeRecord("33333", Pair.create(3.0, 3.0), "AZ", "Flagstaff", 60000.0, 300)
				});
		
		Sampler<Integer> idSampler = new SequenceSampler();
		Sampler<String> nameSampler = RouletteWheelSampler.createUniform(nameList, factory);
		Sampler<ZipcodeRecord> zipcodeSampler = RouletteWheelSampler.createUniform(zipcodes, factory);
		
		Sampler<Customer> sampler = new CustomerSampler(idSampler, nameSampler, nameSampler, zipcodeSampler);
		
		Customer customer = sampler.sample();
		
		assertNotNull(customer);
		assertTrue(customer.getId() >= 0);
		assertNotNull(customer.getName());
		assertNotNull(customer.getName().getFirst());
		assertTrue(nameList.contains(customer.getName().getFirst()));
		assertNotNull(customer.getName().getSecond());
		assertTrue(nameList.contains(customer.getName().getSecond()));
		assertNotNull(customer.getLocation());
		assertTrue(zipcodes.contains(customer.getLocation()));
		
	}

}
