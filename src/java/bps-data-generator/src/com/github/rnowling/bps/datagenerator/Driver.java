package com.github.rnowling.bps.datagenerator;


public class Driver
{
	public static void main(String[] args) throws Exception
	{
		int nStores = 100;
		int nCustomers = 1000;
		long seed = 4576;
		
		Simulation simulation = new Simulation(nStores, nCustomers, seed);
		
		simulation.simulate();
	}
}
