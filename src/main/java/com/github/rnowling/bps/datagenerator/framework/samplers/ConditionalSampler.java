package com.github.rnowling.bps.datagenerator.framework.samplers;

public interface ConditionalSampler<T, S>
{
	public T sample(S conditional) throws Exception;
	
	public Sampler<T> fixConditional(S conditional) throws Exception;
}
