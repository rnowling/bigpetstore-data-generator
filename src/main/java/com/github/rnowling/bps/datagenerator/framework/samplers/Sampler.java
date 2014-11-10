package com.github.rnowling.bps.datagenerator.framework.samplers;

public interface Sampler<T>
{
	public T sample() throws Exception;
}
