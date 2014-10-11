package com.github.rnowling.bps.datagenerator.generators.transaction;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.github.rnowling.bps.datagenerator.algorithms.SeedFactory;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CustomerInventoryBuilder
{
	final private List<ProductCategory> productCategories;
	final private SeedFactory seedFactory;
	final private CustomerTransactionParameters parameters;
	
	public CustomerInventoryBuilder(CustomerTransactionParameters parameters,
			SeedFactory seedFactory)
	{
		productCategories = Lists.newArrayList();
		this.seedFactory = seedFactory;
		this.parameters = parameters;
	}
	
	public void addProductCategory(ProductCategory productCategory)
	{
		this.productCategories.add(productCategory);
	}
	
	public void addAllProductCategories(Collection<ProductCategory> productCategories)
	{
		this.productCategories.addAll(productCategories);
	}
	
	public CustomerInventory build()
	{
		Map<String, ProductCategoryInventory> inventories = Maps.newHashMap();
		for(ProductCategory productCategory : productCategories)
		{
			if(parameters.countPetsBySpecies(productCategory.getApplicableSpecies()) > 0)
			{
				ProductCategoryInventory inventory = new ProductCategoryInventory(productCategory,
					parameters, seedFactory);
				inventories.put(productCategory.getCategoryLabel(), inventory);
			}
		}
		
		return new CustomerInventory(inventories);
	}
}


