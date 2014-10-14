package com.github.rnowling.bps.datagenerator.cli;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Random;

import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Transaction;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;


public class Driver
{
	int nStores;
	int nCustomers;
	double simulationTime;
	long seed;
	File outputDir;
	
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
	
	private void parseArgs(String[] args)
	{
		if(args.length < 4 || args.length > 5)
		{
			printUsage();
			System.exit(1);
		}
		
		outputDir = new File(args[0]);
		if(! outputDir.exists())
		{
			System.err.println("Given path (" + args[0] + ") does not exist.\n");
			printUsage();
			System.exit(1);
		}
		
		if(! outputDir.isDirectory())
		{
			System.err.println("Given path (" + args[0] + ") is not a directory.\n");
			printUsage();
			System.exit(1);
		}
		
		try
		{
			nStores = Integer.parseInt(args[1]);
		}
		catch(Exception e)
		{
			System.err.println("Unable to parse '" + args[1] + "' as an integer for nStores.\n");
			printUsage();
			System.exit(1);
		}
		
		try
		{
			nCustomers = Integer.parseInt(args[2]);
		}
		catch(Exception e)
		{
			System.err.println("Unable to parse '" + args[2] + "' as an integer for nCustomers.\n");
			printUsage();
			System.exit(1);
		}
		
		try
		{
			simulationTime = Double.parseDouble(args[3]);
		}
		catch(Exception e)
		{
			System.err.println("Unable to parse '" + args[3] + "' as a float for simulationLength.\n");
			printUsage();
			System.exit(1);
		}
		
		if(args.length == 5)
		{
			try
			{
				seed = Long.parseLong(args[4]);
			}
			catch(Exception e)
			{
				System.err.println("Unable to parse '" + args[4] + "' as a long for the seed.\n");
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
	
	private void run() throws Exception
	{
		Simulation simulation = new Simulation(nStores, nCustomers, simulationTime, seed);
		
		simulation.simulate();
		
		writeTransactions(simulation.getTransactions());
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
