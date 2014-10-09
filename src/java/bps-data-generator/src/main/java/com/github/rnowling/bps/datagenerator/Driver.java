package com.github.rnowling.bps.datagenerator;


public class Driver
{
	public static void main(String[] args) throws Exception
	{
		int nStores = 10;
		int nCustomers = 100;
		double simulationTime = 365.0 * 5;
		long seed = 4576;
		
		Simulation simulation = new Simulation(nStores, nCustomers, simulationTime, seed);
		
		simulation.simulate();
	}
}
