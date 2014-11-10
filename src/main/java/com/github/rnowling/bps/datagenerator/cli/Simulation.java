package com.github.rnowling.bps.datagenerator.cli;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.github.rnowling.bps.datagenerator.CustomerGenerator;
import com.github.rnowling.bps.datagenerator.PurchasingProfileGenerator;
import com.github.rnowling.bps.datagenerator.StoreGenerator;
import com.github.rnowling.bps.datagenerator.TransactionGenerator;
import com.github.rnowling.bps.datagenerator.datamodels.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.PurchasingProfile;
import com.github.rnowling.bps.datagenerator.datamodels.Store;
import com.github.rnowling.bps.datagenerator.datamodels.Transaction;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.google.common.collect.Lists;

public class Simulation
{
	InputData inputData;
	SeedFactory seedFactory;
	int nStores;
	int nCustomers;
	double simulationTime;
	
	List<Store> stores;
	List<Customer> customers;
	List<PurchasingProfile> purchasingProfiles;
	List<Transaction> transactions;
	
	public Simulation(InputData inputData, int nStores, int nCustomers, double simulationTime, long seed)
	{
		this.inputData = inputData;
		this.nStores = nStores;
		this.nCustomers = nCustomers;
		this.simulationTime = simulationTime;
		seedFactory = new SeedFactory(seed);
	}
	
	public void generateStores() throws Exception
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
	
	public void generateCustomers() throws Exception
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
		PurchasingProfileGenerator generator = new PurchasingProfileGenerator(inputData.getProductCategories(), seedFactory);
		
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
					profile, stores, inputData.getProductCategories(), seedFactory);
			
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
	
	public List<PurchasingProfile> getPurchasingProfiles()
	{
		return purchasingProfiles;
	}
	
	public InputData getInputData()
	{
		return inputData;
	}
}
