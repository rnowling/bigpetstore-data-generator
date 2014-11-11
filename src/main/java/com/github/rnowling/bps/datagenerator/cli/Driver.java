package com.github.rnowling.bps.datagenerator.cli;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Random;

import com.github.rnowling.bps.datagenerator.DataLoader;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.Product;
import com.github.rnowling.bps.datagenerator.datamodels.Transaction;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;


public class Driver
{
	int nStores;
	int nCustomers;
	double simulationTime;
	long seed;
	File outputDir;
	
	static final int NPARAMS = 5;
	
	private void printUsage()
	{
		String usage = "BigPetStore Data Generator\n" +
				"\n" +
				"Usage: java -jar bps-data-generator-v0.2.java outputDir nStores nCustomers simulationLength [seed]\n" +
				"\n" + 
				"outputDir - (string) directory to write files\n" +
				"nStores - (int) number of stores to generate\n" +
				"nCustomers - (int) number of customers to generate\n" +
				"simulationLength - (float) number of days to simulate\n" +
				"seed - (long) seed for RNG. If not given, one is reandomly generated.\n";
		
		System.out.println(usage);
	}
	
	public void parseArgs(String[] args)
	{
		if(args.length != NPARAMS && args.length != (NPARAMS - 1))
		{
			printUsage();
			System.exit(1);
		}
		
		int i = -1;
		
		outputDir = new File(args[++i]);
		if(! outputDir.exists())
		{
			System.err.println("Given path (" + args[i] + ") does not exist.\n");
			printUsage();
			System.exit(1);
		}
		
		if(! outputDir.isDirectory())
		{
			System.err.println("Given path (" + args[i] + ") is not a directory.\n");
			printUsage();
			System.exit(1);
		}
		
		try
		{
			nStores = Integer.parseInt(args[++i]);
		}
		catch(Exception e)
		{
			System.err.println("Unable to parse '" + args[i] + "' as an integer for nStores.\n");
			printUsage();
			System.exit(1);
		}
		
		try
		{
			nCustomers = Integer.parseInt(args[++i]);
		}
		catch(Exception e)
		{
			System.err.println("Unable to parse '" + args[i] + "' as an integer for nCustomers.\n");
			printUsage();
			System.exit(1);
		}
		
		try
		{
			simulationTime = Double.parseDouble(args[++i]);
		}
		catch(Exception e)
		{
			System.err.println("Unable to parse '" + args[i] + "' as a float for simulationLength.\n");
			printUsage();
			System.exit(1);
		}
		
		if(args.length == NPARAMS)
		{
			try
			{
				seed = Long.parseLong(args[++i]);
			}
			catch(Exception e)
			{
				System.err.println("Unable to parse '" + args[i] + "' as a long for the seed.\n");
				printUsage();
				System.exit(1);
			}
		}
		else
		{
			seed = (new Random()).nextLong();
		}
	}
	
	private void writeTransactions(Collection<Transaction> transactions) throws Exception
	{
		File outputFile = new File(outputDir.toString() + File.separator + "transactions.txt");
		System.out.println(outputFile.toString());
		OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
		
		for(Transaction transaction : transactions)
		{
			for(Product product : transaction.getProducts())
			{
				String record = transaction.getId() + ",";
				record += transaction.getDateTime() + ",";
				record += transaction.getStore().getId() + ",";
				record += transaction.getStore().getLocation().getZipcode() + ",";
				record += transaction.getCustomer().getId() + ",";
				Pair<String, String> name = transaction.getCustomer().getName();
				record += name.getFirst() + " " + name.getSecond() + ",";
				record += transaction.getCustomer().getLocation().getZipcode() + ",";
				record += product.toString() + "\n";
				
				outputStream.write(record.getBytes());
			}
		}
		
		outputStream.close();
	}
	
	public Simulation buildSimulation(InputData inputData)
	{
		return new Simulation(inputData, nStores, nCustomers, simulationTime, seed);
	}
	
	private void run(InputData inputData) throws Exception
	{
		Simulation simulation = buildSimulation(inputData);
		
		simulation.simulate();
		
		writeTransactions(simulation.getTransactions());
	}	
	public void run(String[] args) throws Exception
	{
		parseArgs(args);
		
		InputData inputData = (new DataLoader()).loadData();
		
		run(inputData);
	}
	
	public static void main(String[] args) throws Exception
	{
		Driver driver = new Driver();
		driver.run(args);		
	}
	
	public Double getSimulationLength()
	{
		return simulationTime;
	}
	
	public int getNCustomers()
	{
		return nCustomers;
	}
	
	public long getSeed()
	{
		return seed;
	}
	
	public int getNStores()
	{
		return nStores;
	}
	
	public File getOutputDir()
	{
		return outputDir;
	}
}
