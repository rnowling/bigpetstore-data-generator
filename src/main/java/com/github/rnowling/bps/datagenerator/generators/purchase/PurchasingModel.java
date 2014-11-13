package com.github.rnowling.bps.datagenerator.generators.purchase;

import java.io.Serializable;

import com.github.rnowling.bps.datagenerator.framework.SeedFactory;
import com.google.common.collect.ImmutableSet;

public interface PurchasingModel extends Serializable
{
	public ImmutableSet<String> getProductCategories();
	
	public PurchasingProcesses buildProcesses(SeedFactory seedFactory);
}
