package com.github.rnowling.bps.datagenerator.generators.store;

import java.util.List;

import com.github.rnowling.bps.datagenerator.algorithms.pdfs.ProbabilityDensityFunction;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;

public class StoreLocationIncomePDF implements ProbabilityDensityFunction<ZipcodeRecord>
{
	double incomeNormalizationFactor;
	double minIncome;
	double k;
	
	public StoreLocationIncomePDF(List<ZipcodeRecord> zipcodeTable, double incomeScalingFactor)
	{
		
		double maxIncome = 0.0;
		minIncome = Double.MAX_VALUE;
		
		for(ZipcodeRecord record : zipcodeTable)
		{
			maxIncome = Math.max(maxIncome, record.getMedianHouseholdIncome());
			minIncome = Math.min(minIncome, record.getMedianHouseholdIncome());
		}
		
		k = Math.log(incomeScalingFactor) / (maxIncome - minIncome);
		
		incomeNormalizationFactor = 0.0d;
		for(ZipcodeRecord record : zipcodeTable)
		{
			double weight = incomeWeight(record);
			incomeNormalizationFactor += weight;
		}
	}
	
	private double incomeWeight(ZipcodeRecord record)
	{
		return Math.exp(k * (record.getMedianHouseholdIncome() - minIncome));
	}
	
	
	@Override
	public double probability(ZipcodeRecord datum)
	{
		double weight = incomeWeight(datum);
		
		return weight / this.incomeNormalizationFactor;
	}

}
