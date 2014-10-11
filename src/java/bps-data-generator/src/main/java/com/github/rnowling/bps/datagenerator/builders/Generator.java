package com.github.rnowling.bps.datagenerator.builders;

public interface Generator<T>
{
	public T generate() throws Exception;
}
