from collections import defaultdict

import random

import math

import numpy as np

from samplers import RouletteWheelSampler

import simulation_parameters as sim_param

class Customer(object):
    def __init__(self):
        self.id = None
        self.name = None
        self.location = None
        self.average_transaction_trigger_time = None
        self.average_purchase_trigger_time = None
        self.pets = {
                        "dog" : 0,
                        "cat" : 0
                    }

    def __repr__(self):
        return "(%s, %s, %s dogs, %s cats, %s)" % \
            (self.id, self.name, self.pets["dog"], 
             self.pets["cat"], self.location)

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
    def __init__(self, zipcode_objs=None, stores=None):
        self.location_sampler = LocationSampler(stores=stores,
                                                zipcode_objs=zipcode_objs,
                                                avg_distance=sim_param.AVERAGE_CUSTOMER_STORE_DISTANCE)
        self.current_id = 0


    def generate(self, n):
        customers = list()
        for i in xrange(n):
            customer = Customer()
            customer.id = self.current_id
            self.current_id += 1
            customer.name = "Customer_" + str(i)
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
            customers.append(customer)
        return customers

if __name__ == "__main__":
    from zipcodes import load_zipcode_data
    from stores import StoreGenerator

    print "Loading zipcode data..."
    zipcode_objs = load_zipcode_data()
    print

    print "Generating Stores..."
    generator = StoreGenerator(zipcode_objs=zipcode_objs)
    stores = generator.generate(n=100)
    print

    print "Generating customers..."
    generator = CustomerGenerator(zipcode_objs=zipcode_objs,
                                  stores=stores)
    customers = generator.generate(10)
    print

    print "Customers"
    for c in customers:
        print c

    print

