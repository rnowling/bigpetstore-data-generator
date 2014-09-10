import unittest

import simulation_parameters as sim_params

from datamodels.output_models import Customer
from datamodels.output_models import Store
from datamodels.input_models import ZipcodeRecord

from generators.customer_generator import CustomerGenerator
from generators.customer_generator import LocationSampler
from generators.customer_generator import NameSampler

from readers import load_names
from readers import load_zipcode_data

class TestNameSampler(unittest.TestCase):
    def test_sample(self):
        namedb_fl = sim_params.NAMEDB_FILE
        first_names, last_names = load_names(namedb_fl)

        sampler = NameSampler(first_names, last_names)

        name = sampler.sample()

        self.assertIsInstance(name, basestring)
        self.assertIn(" ", name)

        names = name.split(" ")
        self.assertTrue(len(names) == 2)

class TestLocationSampler(unittest.TestCase):
    def test_location_sampler(self):
        zipcodes = load_zipcode_data(**sim_params.ZIPCODE_DATA_FILES)

        stores = []
        for i, zipcode in enumerate(zipcodes.values()[:10]):
            store = Store()
            store.id = i
            store.name = "Store_%s" % i
            store.location = zipcode
            stores.append(store)
            
        sampler = LocationSampler(stores, zipcodes, avg_distance=5.0)
        
        location = sampler.sample()
        self.assertIsInstance(location, ZipcodeRecord)
        self.assertIn(location.zipcode, zipcodes)

    def test_distance_parameter(self):
        zipcodes = load_zipcode_data(**sim_params.ZIPCODE_DATA_FILES)

        stores = []
        for i, zipcode in enumerate(zipcodes.values()[:10]):
            store = Store()
            store.id = i
            store.name = "Store_%s" % i
            store.location = zipcode
            stores.append(store)
            
        sampler5 = LocationSampler(stores, zipcodes, avg_distance=5.0)
        sampler20 = LocationSampler(stores, zipcodes, avg_distance=20.0)

class TestCustomerGenerator(unittest.TestCase):
    pass
        
