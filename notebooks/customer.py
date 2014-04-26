import random

from monte_carlo import MonteCarloGenerator

from store import StoreSampler

import numpy as np
import pandas as pd

pets = ["dog", "cat", "fish", "rabbit", "rodent", "reptile"]
first_names = ["Larry", "Susan", "Chris", "Jay", "Marigold", "Lisa"]
last_names = ["Houdini", "Danzig", "Smith", "Doe", "Van Horn"]

class CustomerPDF(object):
	def __init__(self, stores):
		self.stores = stores

	def probability(self, customer):
		zipcode = customer["zipcode"]
		return self.stores.loc[zipcode].pop_density

class CustomerInstanceGenerator(object):
	def __init__(self, stores):
		self.stores = stores
		self.store_sampler = StoreSampler(stores)

	def generate(self):
		name = random.choice(first_names) + " " + random.choice(last_names)
		pet = random.choice(pets)
		purchase_value = self.sample_exp()
		zipcode = self.store_sampler.sample().loc["zipcode"]
		return {"name" : name, "pet_type" : pet, "purchase_value" : purchase_value, "zipcode" : zipcode}

	def sample_exp(self):
		mean = 1.0
		return random.expovariate(mean)

class CustomerGenerator(object):
	def __init__(self, stores):
		self.stores = stores
		gen = CustomerInstanceGenerator(stores)
		pdf = CustomerPDF(stores)
		self.mcmc = MonteCarloGenerator(gen, pdf)

	def generate_n(self, n):
		prototypes = self.generate_customers(n)
		finalized = self.finalize_customers(prototypes)
		return finalized

	def generate_customers(self, n):
		customers = []
		for i in xrange(n):
			customer = self.mcmc.generate()
			customers.append(customer)
		return customers

	def finalize_customers(self, customers):
		zipcodes = []
		pets = []
		names = []
		purchase_values = []
		value_total = 0.0
		ids = np.arange(len(customers))
		for c in customers:
			zipcodes.append(c["zipcode"])
			pets.append(c["pet_type"])
			names.append(c["name"])
			purchase_values.append(c["purchase_value"])
			value_total += c["purchase_value"]
		purchase_pdf = [value / value_total for value in purchase_values]
		return pd.DataFrame(data={"id" : ids, "name" : names, "zipcode" : zipcodes, "pet_type" : pets, "purchase_pdf" : purchase_pdf}, index=ids)

