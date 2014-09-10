from collections import defaultdict

import random

import numpy as np

from algorithms.samplers import RouletteWheelSampler

import simulation_parameters as sim_param

from datamodels.output_models import Customer

class NameSampler(object):
    def __init__(self, first_names, last_names):
        normalized_first_names = self.normalize(first_names)
        normalized_last_names = self.normalize(last_names)

        self.first_name_sampler = RouletteWheelSampler(normalized_first_names)
        self.last_name_sampler = RouletteWheelSampler(normalized_last_names)

    def normalize(self, names):
        normalized_names = []

        weight_sum = 0.0
        for name, weight in names:
            weight_sum += weight

        for name, weight in names:
            normalized_names.append((name, weight / weight_sum))

        return normalized_names

    def sample(self):
        names = []
        names.append(self.first_name_sampler.sample())
        names.append(self.last_name_sampler.sample())

        return " ".join(names)        

        
class LocationSampler(object):
    def __init__(self, stores=None, zipcode_objs=None, avg_distance=None):
        lambd = 1.0 / avg_distance
        self.stores = stores
        self.zipcode_objs = zipcode_objs

        zipcode_weights = dict()
        weight_sum = 0.0
        for zipcode in zipcode_objs.itervalues():
            dist, nearest_store = self._closest_store(zipcode)
            weight = lambd * np.exp(-lambd * dist)
            weight_sum += weight
            zipcode_weights[zipcode.zipcode] = weight

        zipcode_probs = []
        for zipcode in zipcode_objs.iterkeys():
            zipcode_probs.append((self.zipcode_objs[zipcode], zipcode_weights[zipcode] / weight_sum))

        self.sampler = RouletteWheelSampler(zipcode_probs)

        
    def _closest_store(self, zipcode):
        distances = []
        for store in self.stores:
            dist = zipcode.distance(store.location)
            distances.append((dist, store))
            
        return min(distances)

    def sample(self):
        return self.sampler.sample()

class CustomerGenerator(object):
    def __init__(self, zipcode_objs=None, stores=None, first_names=None,
                 last_names=None):
        self.location_sampler = LocationSampler(stores=stores,
                                                zipcode_objs=zipcode_objs,
                                                avg_distance=sim_param.AVERAGE_CUSTOMER_STORE_DISTANCE)
        self.name_sampler = NameSampler(first_names, last_names)
        self.current_id = 0


    def generate(self):
        customer = Customer()
        customer.id = self.current_id
        self.current_id += 1
        customer.name = self.name_sampler.sample()
        customer.location = self.location_sampler.sample()

        return customer


