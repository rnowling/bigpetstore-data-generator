package com.github.rnowling.bps.datagenerator;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.github.rnowling.bps.datagenerator.datamodels.Customer;
import com.github.rnowling.bps.datagenerator.datamodels.Store;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.InputData;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.Names;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ZipcodeRecord;
import com.github.rnowling.bps.datagenerator.datareaders.NameReader;
import com.github.rnowling.bps.datagenerator.datareaders.ZipcodeReader;
import com.github.rnowling.bps.datagenerator.generators.customer.CustomerGenerator;
import com.github.rnowling.bps.datagenerator.generators.store.StoreGenerator;

public class Simulation
{
	InputData inputData;
	SeedFactory seedFactory;
	int nStores;
	int nCustomers;
	
	List<Store> stores;
	List<Customer> customers;
	
	public Simulation(int nStores, int nCustomers, long seed)
	{
		this.nStores = nStores;
		this.nCustomers = nCustomers;
		seedFactory = new SeedFactory(seed);
	}
	
	private void loadData() throws FileNotFoundException
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
		
		inputData = new InputData(zipcodeTable, names);
	}
	
	private void generateStores(int nStores) throws Exception
	{
		System.out.println("Generating stores");
		StoreGenerator generator = new StoreGenerator(inputData.getZipcodeTable(), this.seedFactory);
		
		stores = new Vector<Store>();
		for(int i = 0; i < nStores; i++)
		{
			Store store = generator.generate();
			stores.add(store);
		}
		
		stores = Collections.unmodifiableList(stores);
		
		System.out.println("Generated " + stores.size() + " stores");
	}
	
	private void generateCustomers(int nCustomers) throws Exception
	{
		System.out.println("Generating customers");
		CustomerGenerator generator = new CustomerGenerator(stores, inputData, this.seedFactory);
		
		customers = new Vector<Customer>();
		for(int i = 0; i < nCustomers; i++)
		{
			Customer customer = generator.generate();
			customers.add(customer);
		}
		
		customers = Collections.unmodifiableList(customers);
		
		System.out.println("Generated " + customers.size() + " customers");
	}
	
	public void simulate() throws Exception
	{
		loadData();
		generateStores(nStores);
		generateCustomers(nCustomers);
	}
}
