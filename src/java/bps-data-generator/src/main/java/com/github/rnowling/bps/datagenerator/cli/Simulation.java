package com.github.rnowling.bps.datagenerator.cli;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.github.rnowling.bps.datagenerator.CustomerGenerator;
import com.github.rnowling.bps.datagenerator.PurchasingProfileGenerator;
import com.github.rnowling.bps.datagenerator.StoreGenerator;
import com.github.rnowling.bps.datagenerator.TransactionGenerator;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.Names;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Transaction;
import com.github.rnowling.bps.datagenerator.datareaders.NameReader;
import com.github.rnowling.bps.datagenerator.datareaders.ProductsReader;
import com.github.rnowling.bps.datagenerator.datareaders.ZipcodeReader;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.github.rnowling.bps.datagenerator.generators.purchasingprofile.PurchasingProfile;
import com.github.rnowling.bps.datagenerator.resources.Constants;
import com.google.common.collect.Lists;

public class Simulation
{
	InputData inputData;
	SeedFactory seedFactory;
	int nStores;
	int nCustomers;
	double simulationTime;
	Collection<ProductCategory> productCategories;
	
	List<Store> stores;
	List<Customer> customers;
	List<PurchasingProfile> purchasingProfiles;
	List<Transaction> transactions;
	
	public Simulation(int nStores, int nCustomers, double simulationTime, long seed)
	{
		this.nStores = nStores;
		this.nCustomers = nCustomers;
		this.simulationTime = simulationTime;
		seedFactory = new SeedFactory(seed);
	}
	
	private void loadData() throws Exception
	{
		System.out.println("Reading zipcode data");
		ZipcodeReader zipcodeReader = new ZipcodeReader();
		zipcodeReader.setCoordinatesFile(Constants.COORDINATES_FILE);
		zipcodeReader.setIncomesFile(Constants.INCOMES_FILE);
		zipcodeReader.setPopulationFile(Constants.POPULATION_FILE);
		List<ZipcodeRecord> zipcodeTable = zipcodeReader.readData();
		System.out.println("Read " + zipcodeTable.size() + " zipcode entries");
		
		System.out.println("Reading name data");
		NameReader nameReader = new NameReader(Constants.NAMEDB_FILE);
		Names names = nameReader.readData();
		System.out.println("Read " + names.getFirstNames().size() + " first names and " + names.getLastNames().size() + " last names");
		
		System.out.println("Reading product data");
		ProductsReader reader = new ProductsReader(Constants.PRODUCTS_FILE);
		productCategories = reader.readData();
		System.out.println("Read " + productCategories.size() + " product categories");
		
		inputData = new InputData(zipcodeTable, names);
	}
	
	private void generateStores() throws Exception
	{
		System.out.println("Generating stores");
		StoreGenerator storeGenerator = new StoreGenerator(inputData, seedFactory);
		
		stores = new Vector<Store>();
		for(int i = 0; i < nStores; i++)
		{
			Store store = storeGenerator.generate();
			stores.add(store);
		}
		
		stores = Collections.unmodifiableList(stores);
		
		System.out.println("Generated " + stores.size() + " stores");
	}
	
	private void generateCustomers() throws Exception
	{
		System.out.println("Generating customers");
		CustomerGenerator generator = new CustomerGenerator(inputData, stores, seedFactory);
		
		customers = new Vector<Customer>();
		for(int i = 0; i < nCustomers; i++)
		{
			Customer customer = generator.generate();
			customers.add(customer);
		}
		
		customers = Collections.unmodifiableList(customers);
		
		System.out.println("Generated " + customers.size() + " customers");
	}
	
	public void generatePurchasingProfiles() throws Exception
	{
		System.out.println("Generating purchasing profiles");
		PurchasingProfileGenerator generator = new PurchasingProfileGenerator(productCategories, seedFactory);
		
		purchasingProfiles = new Vector<PurchasingProfile>();
		for(int i = 0; i < nCustomers; i++)
		{
			PurchasingProfile profile = generator.generate();
			purchasingProfiles.add(profile);
		}
		
		System.out.println("Generated " + purchasingProfiles.size() + " purchasing profiles");
	}
	
	public void generateTransactions() throws Exception
	{
		System.out.println("Generating transactions");
		transactions = Lists.newArrayList();
		
		for(int i = 0; i < nCustomers; i++)
		{
			Customer customer = customers.get(i);
			PurchasingProfile profile = purchasingProfiles.get(i);
			
			TransactionGenerator generator = new TransactionGenerator(customer,
					profile, stores, productCategories, seedFactory);
			
			while(true)
			{
				Transaction transaction = generator.generate();
				
				if(transaction.getDateTime() > simulationTime)
					break;
				transactions.add(transaction);
			}
		}
		
		System.out.println("Generated " + transactions.size() + " transactions");
	}
	
	public void simulate() throws Exception
	{
		loadData();
		generateStores();
		generateCustomers();
		generatePurchasingProfiles();
		generateTransactions();
	}

	public List<Store> getStores()
	{
		return stores;
	}

	public List<Customer> getCustomers()
	{
		return customers;
	}

	public List<Transaction> getTransactions()
	{
		return transactions;
	}
	
	
}
