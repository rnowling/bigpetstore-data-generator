package com.github.rnowling.bps.datagenerator.generators.store;

import java.util.List;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.framework.pdfs.JointPDF;
import com.github.rnowling.bps.datagenerator.framework.pdfs.ProbabilityDensityFunction;
import com.github.rnowling.bps.datagenerator.framework.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.framework.samplers.SequenceSampler;

public class StoreSamplerBuilder
{
	private final List<ZipcodeRecord> zipcodeTable;
	private final SeedFactory seedFactory;
	
	public StoreSamplerBuilder(List<ZipcodeRecord> zipcodeTable, SeedFactory seedFactory)
	{
		this.zipcodeTable = zipcodeTable;
		this.seedFactory = seedFactory;
	}
	
	public Sampler<Store> build()
	{
		Sampler<Integer> idSampler = new SequenceSampler();
		
		ProbabilityDensityFunction<ZipcodeRecord> locationPopulationPDF = 
				new StoreLocationPopulationPDF(zipcodeTable);
		ProbabilityDensityFunction<ZipcodeRecord> locationIncomePDF = 
				new StoreLocationIncomePDF(zipcodeTable, Constants.INCOME_SCALING_FACTOR);
		ProbabilityDensityFunction<ZipcodeRecord> locationJointPDF = 
				new JointPDF<ZipcodeRecord>(zipcodeTable, locationPopulationPDF, locationIncomePDF);
		
		Sampler<ZipcodeRecord> locationSampler = RouletteWheelSampler.create(zipcodeTable, locationJointPDF, seedFactory);
		
		return new StoreSampler(idSampler, locationSampler);
	}
	
}
