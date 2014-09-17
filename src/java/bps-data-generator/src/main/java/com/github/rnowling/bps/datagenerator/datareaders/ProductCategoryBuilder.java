package com.github.rnowling.bps.datagenerator.datareaders;

import java.util.List;
import java.util.Set;

import com.github.rnowling.bps.datagenerator.datamodels.PetSpecies;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ProductCategoryBuilder
{
	String categoryLabel;
	Set<PetSpecies> applicableSpecies;
	Set<String> fieldNames;
	Boolean triggerTransaction;
	Double dailyUsageRate;
	Double amountUsedPerPetAverage;
	Double amountUsedPerPetVariance;
	Double triggerTransactionRate;
	Double triggerPurchaseRate;
	List<Product> products;
	
	public ProductCategoryBuilder()
	{
		applicableSpecies = Sets.newHashSet();
		fieldNames = Sets.newHashSet();
		products = Lists.newArrayList();
	}
	
	public void setCategory(String category)
	{
		this.categoryLabel = category;
	}
	
	public void setTriggerTransaction(Boolean triggerTransaction)
	{
		this.triggerTransaction = triggerTransaction;
	}
	
	public void setDailyUsageRate(Double dailyUsageRate)
	{
		this.dailyUsageRate = dailyUsageRate;
	}
	
	public void setAmountUsedPetPetAverage(Double baseAmountUsedAverage)
	{
		this.amountUsedPerPetAverage = baseAmountUsedAverage;
	}
	
	public void setAmountUsedPetPetVariance(Double baseAmountUsedVariance)
	{
		this.amountUsedPerPetVariance = baseAmountUsedVariance;
	}
	
	public void setTriggerTransactionRate(Double triggerTransactionRate)
	{
		this.triggerTransactionRate = triggerTransactionRate;
	}
	
	public void setTriggerPurchaseRate(Double triggerPurchaseRate)
	{
		this.triggerPurchaseRate = triggerPurchaseRate;
	}
	
	public void addApplicableSpecies(PetSpecies species)
	{
		this.applicableSpecies.add(species);
	}
	
	public void addFieldName(String fieldName)
	{
		this.fieldNames.add(fieldName);
	}
	
	public void addProduct(Product product)
	{
		this.products.add(product);
	}
	
	protected boolean validateProducts()
	{
		for(Product product : products)
		{
			for(String fieldName : product.getFieldNames())
			{
				if(!fieldNames.contains(fieldName))
					return false;
			}
			
			for(String fieldName : fieldNames)
			{
				if(!product.getFieldNames().contains(fieldName))
					return false;
			}
		}
		
		return true;
	}
	
	public ProductCategory build()
	{
		validateProducts();
		
		return new ProductCategory(categoryLabel, applicableSpecies, fieldNames, triggerTransaction,
				dailyUsageRate, amountUsedPerPetAverage, amountUsedPerPetVariance, triggerTransactionRate,
					triggerPurchaseRate, products);
	}
}
