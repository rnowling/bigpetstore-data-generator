package com.github.rnowling.bps.datagenerator.datareaders;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;

public class TestZipcodeReader
{

	@Test
	public void testRead() throws Exception
	{
		ZipcodeReader reader = new ZipcodeReader();
		reader.setCoordinatesFile(Constants.COORDINATES_FILE);
		reader.setIncomesFile(Constants.INCOMES_FILE);
		reader.setPopulationFile(Constants.POPULATION_FILE);
		
		List<ZipcodeRecord> zipcodes = reader.readData();
		
		assertTrue(zipcodes.size() > 0);
		
		assertNotNull(zipcodes.get(0));
		assertNotNull(zipcodes.get(0).getCoordinates());
		assertNotNull(zipcodes.get(0).getMedianHouseholdIncome());
		assertNotNull(zipcodes.get(0).getPopulation());
		assertNotNull(zipcodes.get(0).getZipcode());
	}

}
