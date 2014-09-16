package com.github.rnowling.bps.datagenerator.generators.customer;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.Names;
import com.github.rnowling.bps.datagenerator.datareaders.NameReader;

public class TestNameGenerator
{

	@Test
	public void testGenerate() throws Exception
	{
		NameReader nameReader = new NameReader(Constants.NAMEDB_FILE);
		Names names = nameReader.readData();
	
		SeedFactory factory = new SeedFactory(1234);
		
		NameGenerator generator = new NameGenerator(names, factory);
		
		Pair<String, String> name = generator.generate();
		
		assertNotNull(name);
		assertNotNull(name.getFirst());
		assertNotNull(name.getSecond());		
	}

}
