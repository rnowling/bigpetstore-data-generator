package com.github.rnowling.bps.datagenerator.datamodels;

import java.util.Set;

public class ProductCategory
{
	String name;
	Set<PetSpecies> applicableSpecies;
	Boolean triggerTransaction;
	Double dailyUsageRate;
	Double averageAmountUsedPerPet;
	Double varianceAmountUsedPerPet;
	Double transactionTriggerRate;
	Double purchaseTriggerRate;
	
	
	
}
