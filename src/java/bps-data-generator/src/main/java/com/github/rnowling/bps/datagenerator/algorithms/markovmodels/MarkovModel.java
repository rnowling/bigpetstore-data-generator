package com.github.rnowling.bps.datagenerator.algorithms.markovmodels;

import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.google.common.collect.ImmutableMap;

public class MarkovModel<T> implements Sampler<T>
{
	ImmutableMap<T, Sampler<T>> transitionSamplers;
	T currentState;
	Sampler<T> startStateSampler;
	
	
	public MarkovModel(ImmutableMap<T, Sampler<T>> transitionSamplers, Sampler<T> startStateSampler)
	{
		this.transitionSamplers = transitionSamplers;
		this.startStateSampler = startStateSampler;
		
		initialize();
	}
	
	public T sample()
	{
		T previousState = currentState;
		currentState = transitionSamplers.get(currentState).sample();
		return previousState;
	}
	
	public void initialize()
	{
		currentState = startStateSampler.sample();
	}
	
	public static void main(String[] args)
	{
		SeedFactory seedFactory = new SeedFactory(12345);
		MarkovModelBuilder<Integer> builder = new MarkovModelBuilder<Integer>(seedFactory);
		
		System.out.println("Adding transitions");
		for(int i = 0; i < 6; i++)
			for(int j = 0; j < 6; j++)
				builder.addTransition(i, j, 0.01);
		
		builder.addTransition(0, 1, 1.0);
		builder.addTransition(1, 2, 1.0);
		builder.addTransition(2, 3, 1.0);
		builder.addTransition(3, 4, 1.0);
		builder.addTransition(4, 5, 1.0);
		builder.addTransition(5, 0, 1.0);
		
		System.out.println("Added transitions");
		
		System.out.println("Building");
		MarkovModel<Integer> msm = builder.build();
		System.out.println("Built");
		
		for(int i = 0; i < 100; i++)
			System.out.println(msm.sample());
	}
}
