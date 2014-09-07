from collections import defaultdict

import random

import math

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
        for zipcode in zipcode_objs.iterkeys():
            dist, nearest_store = self._closest_store(zipcode)
            weight = lambd * np.exp(-lambd * dist)
            weight_sum += weight
            zipcode_weights[zipcode] = weight

        zipcode_probs = []
        for zipcode in zipcode_objs.iterkeys():
            zipcode_probs.append((zipcode, zipcode_weights[zipcode] / weight_sum))

        self.sampler = RouletteWheelSampler(zipcode_probs)

    def _dist(self, lat_A, long_A, lat_B, long_B):
        """
        Computes distance between latitude-longitude
        pairs in miles.
        """
        dist = (math.sin(math.radians(lat_A)) *
                math.sin(math.radians(lat_B)) +
                math.cos(math.radians(lat_A)) *
                math.cos(math.radians(lat_B)) *
                math.cos(math.radians(long_A - long_B)))
        dist = (math.degrees(math.acos(dist))) * 69.09
        return dist
        
    def _closest_store(self, zipcode):
        distances = []
        for store in self.stores:
            if store.zipcode == zipcode:
                dist = 0.0
            else:
                latA, longA = self.zipcode_objs[store.zipcode].coords
                latB, longB = self.zipcode_objs[zipcode].coords
                dist = self._dist(latA, longA, latB, longB) 
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
        
        num_pets = random.randint(sim_param.MIN_PETS, sim_param.MAX_PETS)
        num_dogs = random.randint(0, num_pets)
        num_cats = num_pets - num_dogs
            
        # days
        r = random.normalvariate(sim_param.TRANSACTION_TRIGGER_TIME_AVERAGE,
                                 sim_param.TRANSACTION_TRIGGER_TIME_VARIANCE)
        r = max(r, sim_param.TRANSACTION_TRIGGER_TIME_MIN)
        r = min(r, sim_param.TRANSACTION_TRIGGER_TIME_MAX)
        customer.average_transaction_trigger_time = r
        
        r = random.normalvariate(sim_param.PURCHASE_TRIGGER_TIME_AVERAGE,
                                 sim_param.PURCHASE_TRIGGER_TIME_VARIANCE)
        r = max(r, sim_param.PURCHASE_TRIGGER_TIME_MIN)
        r = min(r, sim_param.PURCHASE_TRIGGER_TIME_MAX)        
        customer.average_purchase_trigger_time = r
            
        customer.pets["dog"] = num_dogs
        customer.pets["cat"] = num_cats

        return customer


