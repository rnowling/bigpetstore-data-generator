package com.github.rnowling.bps.datagenerator.generators.customer;

import java.util.List;
import java.util.Map;

import com.github.rnowling.bps.datagenerator.datamodels.Store;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.framework.pdfs.ProbabilityDensityFunction;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class CustomerLocationPDF implements ProbabilityDensityFunction<ZipcodeRecord>
{
	private final Map<ZipcodeRecord, Double> pdf;
	
	public CustomerLocationPDF(List<ZipcodeRecord> zipcodes, Store store, double averageDistance)
	{
		this.pdf = build(zipcodes, store, averageDistance);
	}
	
	protected ImmutableMap<ZipcodeRecord, Double> build(List<ZipcodeRecord> zipcodeTable,
			Store store, double averageDistance)
	{
		double lambda = 1.0 / averageDistance;
		
		Map<ZipcodeRecord, Double> zipcodeWeights = Maps.newHashMap();
		double totalWeight = 0.0;
		for(ZipcodeRecord record : zipcodeTable)
		{
			double dist = record.distance(store.getLocation());
			
			double weight = lambda * Math.exp(-1.0 * lambda * dist);
			totalWeight += weight;
			zipcodeWeights.put(record, weight);
		}
		
		Map<ZipcodeRecord, Double> pdf = Maps.newHashMap();
		for(ZipcodeRecord record : zipcodeTable)
		{
			pdf.put(record, zipcodeWeights.get(record) / totalWeight);
		}
		
		return ImmutableMap.copyOf(pdf);
	}
	
	public double probability(ZipcodeRecord record)
	{
		if(!this.pdf.containsKey(record))
			return 0.0;
		
		return this.pdf.get(record);
	}
}
