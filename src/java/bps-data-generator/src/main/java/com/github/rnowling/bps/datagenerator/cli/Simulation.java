package com.github.rnowling.bps.datagenerator.cli;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.algorithms.SeedFactory;
import com.github.rnowling.bps.datagenerator.algorithms.samplers.Sampler;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.Names;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Store;
import com.github.rnowling.bps.datagenerator.datamodels.outputs.Transaction;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.PurchasingProfile;
import com.github.rnowling.bps.datagenerator.datareaders.NameReader;
import com.github.rnowling.bps.datagenerator.datareaders.ProductsReader;
import com.github.rnowling.bps.datagenerator.datareaders.ZipcodeReader;
import com.github.rnowling.bps.datagenerator.generators.customer.CustomerSamplerBuilder;
import com.github.rnowling.bps.datagenerator.generators.purchasingprofile.PurchasingProfileSamplerBuilder;
import com.github.rnowling.bps.datagenerator.generators.store.StoreSamplerBuilder;
import com.github.rnowling.bps.datagenerator.generators.transaction.TransactionSamplerBuilder;
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
	
	private void generateStores(int nStores) throws Exception
	{
		System.out.println("Generating stores");
		StoreSamplerBuilder builder = new StoreSamplerBuilder(inputData.getZipcodeTable(), this.seedFactory);
		Sampler<Store> storeSampler = builder.build();
		
		stores = new Vector<Store>();
		for(int i = 0; i < nStores; i++)
		{
			Store store = storeSampler.sample();
			stores.add(store);
		}
		
		stores = Collections.unmodifiableList(stores);
		
		System.out.println("Generated " + stores.size() + " stores");
	}
	
	private void generateCustomers(int nCustomers) throws Exception
	{
		System.out.println("Generating customers");
		CustomerSamplerBuilder builder = new CustomerSamplerBuilder(stores, inputData, this.seedFactory);
		Sampler<Customer> sampler = builder.build();
		
		customers = new Vector<Customer>();
		for(int i = 0; i < nCustomers; i++)
		{
			Customer customer = sampler.sample();
			customers.add(customer);
		}
		
		customers = Collections.unmodifiableList(customers);
		
		System.out.println("Generated " + customers.size() + " customers");
	}
	
	public void generateTransactions(double simulationLength) throws Exception
	{
		Sampler<PurchasingProfile> ppSampler =
				new PurchasingProfileSamplerBuilder(productCategories, seedFactory).build();
		
		transactions = Lists.newArrayList();
		
		for(Customer customer : customers)
		{
			PurchasingProfile profile = ppSampler.sample();
			
			Sampler<Transaction> sampler =
					new TransactionSamplerBuilder(stores, productCategories,
							customer, profile, seedFactory).build();
			
			while(true)
			{
				Transaction transaction = sampler.sample();
				
				System.out.println("Transaction Time: " + transaction.getDateTime());
				
				if(transaction.getDateTime() > simulationLength)
					break;
				transactions.add(transaction);
			}
		}
	}
	
	public void simulate() throws Exception
	{
		loadData();
		generateStores(nStores);
		System.out.println("Generated " + stores.size() + " stores");
		generateCustomers(nCustomers);
		System.out.println("Generated " + customers.size() + " customers");
		generateTransactions(simulationTime);
		System.out.println("Generated " + transactions.size() + " transactions");
		
		
	}
}
