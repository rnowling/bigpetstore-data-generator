package com.github.rnowling.bps.datagenerator.generators.transaction;

import com.github.rnowling.bps.datagenerator.Constants;
import com.github.rnowling.bps.datagenerator.SeedFactory;
import com.github.rnowling.bps.datagenerator.datamodels.inputs.ProductCategory;
import com.github.rnowling.bps.datagenerator.datamodels.simulation.Product;

public class ProductCategoryInventory
{	
	private ProductCategoryUsageTrajectory trajectory;
	private ProductCategoryUsageSimulator simulator;
	
	public ProductCategoryInventory(ProductCategory productCategory, CustomerTransactionParameters parameters,
			SeedFactory seedFactory)
	{
		
		double amountUsedAverage = productCategory.getBaseAmountUsedAverage() * parameters.countPetsBySpecies(productCategory.getApplicableSpecies());
		double amountUsedVariance = productCategory.getBaseAmountUsedVariance() * parameters.countPetsBySpecies(productCategory.getApplicableSpecies());		
		
		trajectory = new ProductCategoryUsageTrajectory(0.0, 0.0);
		simulator = new ProductCategoryUsageSimulator(productCategory.getDailyUsageRate(),
				amountUsedAverage, amountUsedVariance, seedFactory);
	}
	
	public void simulatePurchase(double time, Product product) throws Exception
	{
		double amountPurchased = product.getFieldValueAsDouble(Constants.PRODUCT_QUANTITY);
		
		double amountRemainingBeforePurchase = trajectory.amountAtTime(time);
		
		trajectory = simulator.simulate(time, amountRemainingBeforePurchase + amountPurchased);
	}
	
	public double findExhaustionTime()
	{
		return trajectory.getLastTime();
	}
	
	public double findRemainingAmount(double time)
	{
		return trajectory.amountAtTime(time);
	}
}
