package com.github.rnowling.bps.datagenerator.generators.store;

import java.util.List;

import com.github.rnowling.bps.datagenerator.algorithms.pdfs.ProbabilityDensityFunction;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;

public class StoreLocationPopulationPDF implements ProbabilityDensityFunction<ZipcodeRecord>
{
	double populationSum = 0.0;
	
	public StoreLocationPopulationPDF(List<ZipcodeRecord> zipcodeTable)
	{
		long populationSum = 0L;
		for(ZipcodeRecord record : zipcodeTable)
		{
			populationSum += record.getPopulation();
		}
		
		this.populationSum = ((double) populationSum);
	}
	
	public double probability(ZipcodeRecord record)
	{
		return ((double) record.getPopulation()) / populationSum;
	}

}
