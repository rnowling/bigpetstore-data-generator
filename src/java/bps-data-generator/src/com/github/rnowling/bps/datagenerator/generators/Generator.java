package com.github.rnowling.bps.datagenerator.generators;

public interface Generator<T>
{
	public T generate() throws Exception;
}
