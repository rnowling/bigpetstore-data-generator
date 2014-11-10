package com.github.rnowling.bps.datagenerator.framework;

import java.util.Random;

public class SeedFactory
{
	Random rng;
	
	public SeedFactory()
	{
		rng = new Random();
	}
	
	public SeedFactory(long seed)
	{
		rng = new Random(seed);
	}
	
	public long getNextSeed()
	{
		return rng.nextLong();
	}
}
