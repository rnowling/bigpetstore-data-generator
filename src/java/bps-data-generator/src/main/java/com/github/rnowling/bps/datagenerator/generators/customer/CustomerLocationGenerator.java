package com.github.rnowling.bps.datagenerator.generators.customer;

import java.util.List;
import java.util.Map;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.RouletteWheelSampler;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.Store;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.generators.Generator;
import com.google.common.collect.Maps;

public class CustomerLocationGenerator implements Generator<ZipcodeRecord>
{
	List<Store> stores;
	List<ZipcodeRecord> records;
	Sampler<ZipcodeRecord> sampler;
	
	public CustomerLocationGenerator(List<ZipcodeRecord> zipcodes, List<Store> stores, double averageDistance,
			SeedFactory seedFactory)
	{
		double lambda = 1.0 / averageDistance;
		
		Map<ZipcodeRecord, Double> zipcodeWeights = Maps.newHashMap();
		for(ZipcodeRecord record : zipcodes)
		{
			Pair<Store, Double> closestStore = findClosestStore(stores, record);
			double dist = closestStore.getSecond();
			
			double weight = lambda * Math.exp(-1.0 * lambda * dist);
			zipcodeWeights.put(record, weight);
		}
		
		
		this.sampler = RouletteWheelSampler.create(zipcodeWeights, seedFactory);
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
	
	public ZipcodeRecord generate() throws Exception
	{
		return sampler.sample();
	}
}
