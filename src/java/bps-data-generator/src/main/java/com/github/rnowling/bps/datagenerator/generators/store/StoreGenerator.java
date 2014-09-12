package com.github.rnowling.bps.datagenerator.generators.store;

import java.util.List;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.pdfs.JointPDF;
import com.github.rnowling.bps.datagenerator.algorithms.pdfs.ProbabilityDensityFunction;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.generators.Generator;

public class StoreGenerator implements Generator<Store>
{

	Sampler<ZipcodeRecord> locationSampler;
	int currentId;
	
	public StoreGenerator(List<ZipcodeRecord> zipcodeTable, SeedFactory factory)
	{
		ProbabilityDensityFunction<ZipcodeRecord> locationPopulationPDF = new StoreLocationPopulationPDF(zipcodeTable);
		ProbabilityDensityFunction<ZipcodeRecord> locationIncomePDF = new StoreLocationIncomePDF(zipcodeTable, Constants.INCOME_SCALING_FACTOR);
		
		ProbabilityDensityFunction<ZipcodeRecord> locationJointPDF = 
				new JointPDF<ZipcodeRecord>(zipcodeTable, locationPopulationPDF, locationIncomePDF);
		locationSampler = RouletteWheelSampler.create(zipcodeTable, locationJointPDF, factory);
		
		currentId = 0;
	}
	
	private int generateId()
	{
		int id = currentId;
		currentId += 1;
		
		return id;
	}
	
	private ZipcodeRecord generateLocation() throws Exception
	{
		return locationSampler.sample();
	}
	
	private String generateName(int id)
	{
		return "Store_" + id;
	}
	
	public Store generate() throws Exception
	{
		int id = generateId();
		ZipcodeRecord location = generateLocation();
		String name = generateName(id);
		
		return new Store(id, name, location);
	}
	
}
