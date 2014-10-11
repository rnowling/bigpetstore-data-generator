package com.github.rnowling.bps.datagenerator.statistics.samplers;

public interface Sampler<T>
{
	public T sample() throws Exception;
}
