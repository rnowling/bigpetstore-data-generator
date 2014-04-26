import numpy as np
import pandas as pd

import random

from monte_carlo import MonteCarloGenerator

class StorePDF(object):
	def __init__(self, zipcode_pop):
		total_pop = np.float64(zipcode_pop.sum(axis=0))
		self.pop_density = zipcode_pop / np.float64(total_pop)

	def probability(self, store):
		zipcode, pop = store
		return self.pop_density.loc[store]

class StoreInstanceGenerator(object):
	def __init__(self, zipcode_pop):
		self.zipcode_pop = zipcode_pop

	def generate(self):
		zipcode = random.choice(self.zipcode_pop.index)	
		pop = self.zipcode_pop.loc[zipcode]
		return (zipcode, pop)


class StoreGenerator(object):
	def __init__(self, zipcode_pop):
		self.zipcode_pop = zipcode_pop
		pdf = StorePDF(zipcode_pop)
		generator = StoreInstanceGenerator(zipcode_pop)
		self.mcmc = MonteCarloGenerator(pdf, generator)

	def generate_n(self, n):
		zipcodes = self.generate_zipcodes(n)
		stores = self.create_stores(zipcodes)
		return stores

	def generate_zipcodes(self, n):
		zipcodes = []
		for i in xrange(n):
			zipcode = self.mcmc.generate()
			zipcodes.append(zipcode)
		return zipcodes


	def create_stores(self, zipcodes):
		ids = np.arange(len(zipcodes))
		populations = self.zipcode_pop.loc[zipcodes]
		total_pop = np.float64(populations.sum(axis=0))
		salesfrequencies = populations.divide(total_pop)
		return pd.DataFrame(data={"id" : ids, "zipcode" : zipcodes, "salesfrequency" : salesfrequencies}, index=zipcodes)

