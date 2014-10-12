package com.github.rnowling.bps.datagenerator.generators.customer;

import java.util.List;
import java.util.Map;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.framework.pdfs.ProbabilityDensityFunction;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class CustomerLocationPDF implements ProbabilityDensityFunction<ZipcodeRecord>
{
	private final Map<ZipcodeRecord, Double> pdf;
	
	public CustomerLocationPDF(List<ZipcodeRecord> zipcodes, List<Store> stores, double averageDistance)
	{
		this.pdf = build(zipcodes, stores, averageDistance);
	}
	
	protected ImmutableMap<ZipcodeRecord, Double> build(List<ZipcodeRecord> zipcodeTable,
			List<Store> stores, double averageDistance)
	{
		double lambda = 1.0 / averageDistance;
		
		Map<ZipcodeRecord, Double> zipcodeWeights = Maps.newHashMap();
		double totalWeight = 0.0;
		for(ZipcodeRecord record : zipcodeTable)
		{
			Pair<Store, Double> closestStore = findClosestStore(stores, record);
			double dist = closestStore.getSecond();
			
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

	private Pair<Store, Double> findClosestStore(List<Store> stores, ZipcodeRecord record)
	{
		Pair<Store, Double> closestStore = new Pair<Store, Double>(null, Double.MAX_VALUE);
		
		for(Store store : stores)
		{
			double dist = record.distance(store.getLocation());
			if(dist < closestStore.getSecond())
			{
				closestStore = new Pair<Store, Double>(store, dist);
			}
		}
		
		return closestStore;
	}
	
	public double probability(ZipcodeRecord record)
	{
		if(!this.pdf.containsKey(record))
			return 0.0;
		
		return this.pdf.get(record);
	}
}
