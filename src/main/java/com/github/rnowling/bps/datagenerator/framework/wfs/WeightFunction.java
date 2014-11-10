package com.github.rnowling.bps.datagenerator.framework.wfs;

public interface WeightFunction<T>
{
	public double weight(T datum);
}
