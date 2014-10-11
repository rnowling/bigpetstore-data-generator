package com.github.rnowling.bps.datagenerator.datamodels.simulation;

import java.util.List;

public class Purchase
{
	private final double purchaseTime;
	private final List<Product> purchasedProducts;
	
	public Purchase(double purchaseTime, List<Product> purchasedProducts)
	{
		this.purchaseTime = purchaseTime;
		this.purchasedProducts = purchasedProducts;
	}
	
	public double getPurchaseTime()
	{
		return purchaseTime;
	}

	public List<Product> getPurchasedProducts()
	{
		return purchasedProducts;
	}

}
