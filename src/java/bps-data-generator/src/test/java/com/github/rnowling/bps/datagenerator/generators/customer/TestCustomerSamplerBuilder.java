package com.github.rnowling.bps.datagenerator.generators.customer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.Names;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Customer;
import com.github.rnowling.bps.datagenerator.datareaders.NameReader;
import com.github.rnowling.bps.datagenerator.datareaders.ZipcodeReader;
import com.github.rnowling.bps.datagenerator.samplers.customer.CustomerSampler;
import com.github.rnowling.bps.datagenerator.statistics.SeedFactory;
import com.github.rnowling.bps.datagenerator.statistics.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.statistics.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.statistics.samplers.SequenceSampler;

public class TestCustomerSamplerBuilder
{

	@Test
	public void testSample() throws Exception
	{
		ZipcodeReader zipcodeReader = new ZipcodeReader();
		zipcodeReader.setCoordinatesFile(Constants.COORDINATES_FILE);
		zipcodeReader.setIncomesFile(Constants.INCOMES_FILE);
		zipcodeReader.setPopulationFile(Constants.POPULATION_FILE);
		List<ZipcodeRecord> zipcodes = zipcodeReader.readData();
		
		NameReader nameReader = new NameReader(Constants.NAMEDB_FILE);
		Names names = nameReader.readData();
		
		SeedFactory factory = new SeedFactory(1234);
		
		Sampler<Customer> sampler = new CustomerSampler(new SequenceSampler(),
				RouletteWheelSampler.createUniform(names.getFirstNames().keySet(), factory), 
				RouletteWheelSampler.createUniform(names.getLastNames().keySet(), factory), 
				RouletteWheelSampler.createUniform(zipcodes, factory));
		
		Customer customer = sampler.sample();
		
		assertNotNull(customer);
		assertTrue(customer.getId() >= 0);
		assertNotNull(customer.getName());
		assertNotNull(customer.getName().getFirst());
		assertNotNull(customer.getName().getSecond());
		assertNotNull(customer.getLocation());
		
	}

}
