package com.github.rnowling.bps.datagenerator.datamodels.outputs;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class Transaction
{
	final Long id;
	final Customer customer;
	final Store store;
	final Double dateTime;
	final ImmutableList<String> products;
	
	public Transaction(Long id, Customer customer, Store store, Double dateTime, List<String> products)
	{
		this.id = id;
		this.customer = customer;
		this.store = store;
		this.dateTime = dateTime;
		this.products = ImmutableList.copyOf(products);
	}

	public Long getId()
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

	public ImmutableList<String> getProducts()
	{
		return products;
	}
	
	
}
