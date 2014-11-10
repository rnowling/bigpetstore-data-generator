package com.github.rnowling.bps.datagenerator.cli;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.datamodels.Pair;
import com.github.rnowling.bps.datagenerator.datamodels.Product;
import com.github.rnowling.bps.datagenerator.datamodels.Transaction;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.Names;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datareaders.NameReader;
import com.github.rnowling.bps.datagenerator.datareaders.ProductsReader;
import com.github.rnowling.bps.datagenerator.datareaders.ZipcodeReader;


public class Driver
{
	int nStores;
	int nCustomers;
	double simulationTime;
	long seed;
	File outputDir;
	File resourceDir;
	
	private void printUsage()
	{
		String usage = "BigPetStore Data Generator\n" +
				"\n" +
				"Usage: java -jar bps-data-generator-v0.2.java resourceDir outputDir nStores nCustomers simulationLength [seed]\n" +
				"\n" + 
				"resourceDir - (string) directory containing input data files\n" +
				"outputDir - (string) directory to write files\n" +
				"nStores - (int) number of stores to generate\n" +
				"nCustomers - (int) number of customers to generate\n" +
				"simulationLength - (float) number of days to simulate\n" +
				"seed - (long) seed for RNG. If not given, one is reandomly generated.\n";
		
		System.out.println(usage);
	}
	
	public void parseArgs(String[] args)
	{
		if(args.length < 5 || args.length > 6)
		{
			printUsage();
			System.exit(1);
		}
		
		resourceDir = new File(args[0]);
		if(! resourceDir.exists())
		{
			System.err.println("Given path (" + args[0] + ") does not exist.\n");
			printUsage();
			System.exit(1);
		}
		
		if(! resourceDir.isDirectory())
		{
			System.err.println("Given path (" + args[0] + ") is not a directory.\n");
			printUsage();
			System.exit(1);
		}
		
		outputDir = new File(args[1]);
		if(! outputDir.exists())
		{
			System.err.println("Given path (" + args[1] + ") does not exist.\n");
			printUsage();
			System.exit(1);
		}
		
		if(! outputDir.isDirectory())
		{
			System.err.println("Given path (" + args[1] + ") is not a directory.\n");
			printUsage();
			System.exit(1);
		}
		
		try
		{
			nStores = Integer.parseInt(args[2]);
		}
		catch(Exception e)
		{
			System.err.println("Unable to parse '" + args[2] + "' as an integer for nStores.\n");
			printUsage();
			System.exit(1);
		}
		
		try
		{
			nCustomers = Integer.parseInt(args[3]);
		}
		catch(Exception e)
		{
			System.err.println("Unable to parse '" + args[3] + "' as an integer for nCustomers.\n");
			printUsage();
			System.exit(1);
		}
		
		try
		{
			simulationTime = Double.parseDouble(args[4]);
		}
		catch(Exception e)
		{
			System.err.println("Unable to parse '" + args[4] + "' as a float for simulationLength.\n");
			printUsage();
			System.exit(1);
		}
		
		if(args.length == 6)
		{
			try
			{
				seed = Long.parseLong(args[5]);
			}
			catch(Exception e)
			{
				System.err.println("Unable to parse '" + args[5] + "' as a long for the seed.\n");
				printUsage();
				System.exit(1);
			}
		}
		else
		{
			seed = (new Random()).nextLong();
		}
	}
	
	private File getResourcePath(File filename)
	{
		File path = new File(this.resourceDir, filename.toString());
		
		if(!path.exists())
		{
			System.err.println("Given path (" + path + ") does not exist.\n");
			printUsage();
			System.exit(1);
		}
		
		return path;
	}
	
	public InputData loadData() throws Exception
	{
		
		System.out.println("Reading zipcode data");
		ZipcodeReader zipcodeReader = new ZipcodeReader();
		zipcodeReader.setCoordinatesFile(getResourcePath(Constants.COORDINATES_FILE));
		zipcodeReader.setIncomesFile(getResourcePath(Constants.INCOMES_FILE));
		zipcodeReader.setPopulationFile(getResourcePath(Constants.POPULATION_FILE));
		List<ZipcodeRecord> zipcodeTable = zipcodeReader.readData();
		System.out.println("Read " + zipcodeTable.size() + " zipcode entries");
		
		System.out.println("Reading name data");
		NameReader nameReader = new NameReader(getResourcePath(Constants.NAMEDB_FILE));
		Names names = nameReader.readData();
		System.out.println("Read " + names.getFirstNames().size() + " first names and " + names.getLastNames().size() + " last names");
		
		System.out.println("Reading product data");
		ProductsReader reader = new ProductsReader(getResourcePath(Constants.PRODUCTS_FILE));
		Collection<ProductCategory> productCategories = reader.readData();
		System.out.println("Read " + productCategories.size() + " product categories");
		
		InputData inputData = new InputData(zipcodeTable, names, productCategories);
		
		return inputData;
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
		
		InputData inputData = loadData();
		
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
