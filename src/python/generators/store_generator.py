from algorithms.samplers import RouletteWheelSampler

import numpy as np

from datamodels.output_models import Store

class ZipcodeSampler(object):
    def __init__(self, zipcode_objs, income_scaling_factor=None):

        pop_probs = dict()
        income_probs = dict()

        pop_sum = 0.0
        max_income = 0.0
        min_income = 100000.0
        for obj in zipcode_objs.itervalues():
            pop_sum += obj.population
            max_income = max(max_income, obj.median_household_income)
            min_income = min(min_income, obj.median_household_income)
        
        income_k = np.log(income_scaling_factor) / (max_income - min_income)
    
        income_normalization_factor = 0.0
        income_weights = dict()
        for obj in zipcode_objs.itervalues():
            w = np.exp(income_k * (obj.median_household_income - min_income))
            income_normalization_factor += w
            income_weights[obj.zipcode] = w

        income_probs = dict()
        for obj in zipcode_objs.itervalues():
            income_probs[obj.zipcode] = income_weights[obj.zipcode] / income_normalization_factor

        prob_probs = dict()
        for obj in zipcode_objs.itervalues():
            pop_probs[obj.zipcode] = obj.population / pop_sum
            
        normalization_factor = 0.0
        for z in income_probs.iterkeys():
            normalization_factor += income_probs[z] * pop_probs[z]
        
        zipcode_probs = []
        for z in income_probs.iterkeys():
            zipcode_probs.append((zipcode_objs[z], income_probs[z] * pop_probs[z] / normalization_factor))

        self.sampler = RouletteWheelSampler(zipcode_probs)

    def sample(self):
        return self.sampler.sample()

class StoreGenerator(object):
    def __init__(self, zipcode_objs=None, income_scaling_factor=None):
        self.zipcode_objs = zipcode_objs
        self.zipcode_sampler = ZipcodeSampler(zipcode_objs=zipcode_objs,
                                                income_scaling_factor=income_scaling_factor)
        self.current_id = 0
        
    def generate(self):
        store = Store()
        store.id = self.current_id
        self.current_id += 1
        store.name = "Store_" + str(self.current_id)
        store.location = self.zipcode_sampler.sample()
        return store
