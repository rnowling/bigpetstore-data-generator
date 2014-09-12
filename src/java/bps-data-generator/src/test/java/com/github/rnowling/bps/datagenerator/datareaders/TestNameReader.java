package com.github.rnowling.bps.datagenerator.datareaders;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.Names;

public class TestNameReader
{

	@Test
	public void testRead() throws Exception
	{
		NameReader reader = new NameReader(Constants.NAMEDB_FILE);
		
		Names names = reader.readData();
		
		assertNotNull(names.getFirstNames());
		assertNotNull(names.getLastNames());
		
		assertTrue(names.getFirstNames().size() > 0);
		assertTrue(names.getLastNames().size() > 0);
	}

}
