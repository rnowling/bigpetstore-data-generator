import numpy as np
import pandas as pd

import random

from monte_carlo import MonteCarloGenerator

class ZipcodePDF(object):
	def __init__(self, zipcode_pop):
		total_pop = np.float64(zipcode_pop.sum(axis=0))
		self.pop_density = zipcode_pop / np.float64(total_pop)

	def probability(self, zipcode):
		return self.pop_density.loc[zipcode]

class ZipcodeGenerator(object):
	def __init__(self, zipcode_pop):
		self.zipcode_pop = zipcode_pop

	def generate(self):
		zipcode = random.choice(self.zipcode_pop.index)	
		return zipcode


class StoreGenerator(object):
	def __init__(self, zipcode_pop):
		self.zipcode_pop = zipcode_pop
		pdf = ZipcodePDF(zipcode_pop)
		generator = ZipcodeGenerator(zipcode_pop)
		self.mcmc = MonteCarloGenerator(generator, pdf)

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
		pop_density = populations.divide(total_pop)
		return pd.DataFrame(data={"id" : ids, "zipcode" : zipcodes, "pop_density" : pop_density}, index=zipcodes)

