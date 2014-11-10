package com.github.rnowling.bps.datagenerator.framework.wfs;

public interface ConditionalWeightFunction<T, S>
{
	public double weight(T datum, S given);
	
	public WeightFunction<T> fixConditional(S given);
}
