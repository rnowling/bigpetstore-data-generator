package com.github.rnowling.bps.datagenerator.datamodels.inputs;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.github.rnowling.bps.datagenerator.datamodels.Product;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class ProductCategory implements Serializable
{
	private static final long serialVersionUID = -7638076590334497836L;
	
	String categoryLabel;
	ImmutableSet<PetSpecies> applicableSpecies;
	ImmutableSet<String> fieldNames;
	boolean triggerTransaction;
	double dailyUsageRate;
	double amountUsedPerPetAverage;
	double amountUsedPerPetVariance;
	double triggerTransactionRate;
	double triggerPurchaseRate;
	ImmutableList<Product> products;
	
	public ProductCategory(String categoryLabel, Set<PetSpecies> species, Set<String> fieldNames,
			boolean triggerTransaction, double dailyUsageRate, double amountUsedPerPetAverage,
				double amountUsedPerPetVariance, double triggerTransactionRate,
				double triggerPurchaseRate, List<Product> products)
	{
		this.categoryLabel = categoryLabel;
		this.applicableSpecies = ImmutableSet.copyOf(species);
		this.fieldNames = ImmutableSet.copyOf(fieldNames);
		this.triggerTransaction = triggerTransaction;
		this.dailyUsageRate = dailyUsageRate;
		this.amountUsedPerPetAverage = amountUsedPerPetAverage;
		this.amountUsedPerPetVariance = amountUsedPerPetVariance;
		this.triggerTransactionRate = triggerTransactionRate;
		this.triggerPurchaseRate = triggerPurchaseRate;
		this.products = ImmutableList.copyOf(products);
	}
	
	public String getCategoryLabel()
	{
		return categoryLabel;
	}
	
	public ImmutableSet<PetSpecies> getApplicableSpecies()
	{
		return applicableSpecies;
	}
	
	public ImmutableSet<String> getFieldNames()
	{
		return fieldNames;
	}
	public Boolean getTriggerTransaction()
	{
		return triggerTransaction;
	}
	
	public Double getDailyUsageRate()
	{
		return dailyUsageRate;
	}
	
	public Double getBaseAmountUsedAverage()
	{
		return amountUsedPerPetAverage;
	}
	
	public Double getBaseAmountUsedVariance()
	{
		return amountUsedPerPetVariance;
	}
	
	public Double getTransactionTriggerRate()
	{
		return triggerTransactionRate;
	}
	
	public Double getPurchaseTriggerRate()
	{
		return triggerPurchaseRate;
	}
	
	public ImmutableList<Product> getProducts()
	{
		return products;
	}
	
	
	
	
}
