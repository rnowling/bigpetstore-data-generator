package com.github.rnowling.bps.datagenerator.datamodels;

import java.util.List;

public class Transaction
{
	Long id;
	Customer customer;
	Store store;
	Double dateTime;
	List<String> products;
}
