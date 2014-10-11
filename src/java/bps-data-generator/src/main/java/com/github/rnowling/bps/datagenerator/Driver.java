package com.github.rnowling.bps.datagenerator;

import java.util.Random;


public class Driver
{
	int nStores;
	int nCustomers;
	double simulationTime;
	long seed;
	
	private void printUsage()
	{
		String usage = "BigPetStore Data Generator\n" +
				"\n" +
				"Usage: java -jar bps-data-generator-v0.2.java nStores nCustomers simulationLength [seed]\n" +
				"\n" + 
				"nStores - (int) number of stores to generate\n" +
				"nCustomers - (int) number of customers to generate\n" +
				"simulationLength - (float) number of days to simulate\n" +
				"seed - (long) seed for RNG. If not given, one is reandomly generated.\n";
		
		System.out.println(usage);
	}
	
	private void parseArgs(String[] args)
	{
		if(args.length < 3 || args.length > 4)
		{
			printUsage();
			System.exit(1);
		}
		
		try
		{
			nStores = Integer.parseInt(args[0]);
		}
		catch(Exception e)
		{
			System.err.println("Unable to parse '" + args[0] + "' as an integer for nStores.\n");
			printUsage();
			System.exit(1);
		}
		
		try
		{
			nCustomers = Integer.parseInt(args[1]);
		}
		catch(Exception e)
		{
			System.err.println("Unable to parse '" + args[1] + "' as an integer for nCustomers.\n");
			printUsage();
			System.exit(1);
		}
		
		try
		{
			simulationTime = Double.parseDouble(args[2]);
		}
		catch(Exception e)
		{
			System.err.println("Unable to parse '" + args[2] + "' as a float for simulationLength.\n");
			printUsage();
			System.exit(1);
		}
		
		if(args.length == 4)
		{
			try
			{
				seed = Long.parseLong(args[3]);
			}
			catch(Exception e)
			{
				System.err.println("Unable to parse '" + args[3] + "' as a long for the seed.\n");
				printUsage();
				System.exit(1);
			}
		}
		else
		{
			seed = (new Random()).nextLong();
		}
	}
	
	private void run() throws Exception
	{
		Simulation simulation = new Simulation(nStores, nCustomers, simulationTime, seed);
		
		simulation.simulate();
	}
	
	public void run(String[] args) throws Exception
	{
		parseArgs(args);
		
		run();
	}
	
	public static void main(String[] args) throws Exception
	{
		Driver driver = new Driver();
		driver.run(args);		
	}
}
