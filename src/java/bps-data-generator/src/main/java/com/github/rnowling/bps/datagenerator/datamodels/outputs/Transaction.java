package com.github.rnowling.bps.datagenerator.datamodels.outputs;

import java.util.List;

import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.google.common.collect.ImmutableList;

public class Transaction
{
	final int id;
	final Customer customer;
	final Store store;
	final Double dateTime;
	final ImmutableList<Product> products;
	
	public Transaction(int id, Customer customer, Store store, Double dateTime, List<Product> products)
	{
		this.id = id;
		this.customer = customer;
		this.store = store;
		this.dateTime = dateTime;
		this.products = ImmutableList.copyOf(products);
	}

	public int getId()
	{
		return id;
	}

	public Customer getCustomer()
	{
		return customer;
	}

	public Store getStore()
	{
		return store;
	}

	public Double getDateTime()
	{
		return dateTime;
	}

	public ImmutableList<Product> getProducts()
	{
		return products;
	}
	
	
}
