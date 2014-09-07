import unittest

from generators.store_generator import ZipcodeSampler
from generators.store_generator import StoreGenerator
from datamodels.output_models import Store

from readers import load_zipcode_data

import simulation_parameters as sim_params

class TestZipcodeSampler(unittest.TestCase):    
    def test_zipcode_sampler(self):
        income_fl = sim_params.ZIPCODE_DATA_FILES["income_fl"]
        pop_fl = sim_params.ZIPCODE_DATA_FILES["population_fl"]
        coord_fl = sim_params.ZIPCODE_DATA_FILES["coordinate_fl"]
        
        zipcode_records = load_zipcode_data(income_fl, pop_fl, coord_fl)

        sampler = ZipcodeSampler(zipcode_records, 
                                 sim_params.STORE_INCOME_SCALING_FACTOR)
        
        record = sampler.sample()

        self.assertIsInstance(record, str)

class TestStoreGenerator(unittest.TestCase):
    def test_store_generator(self):
        income_fl = sim_params.ZIPCODE_DATA_FILES["income_fl"]
        pop_fl = sim_params.ZIPCODE_DATA_FILES["population_fl"]
        coord_fl = sim_params.ZIPCODE_DATA_FILES["coordinate_fl"]
        
        zipcode_records = load_zipcode_data(income_fl, pop_fl, coord_fl)

        sampler = StoreGenerator(zipcode_records,
                                 sim_params.STORE_INCOME_SCALING_FACTOR)

        store1 = sampler.generate()

        self.assertIsInstance(store1, Store)
        self.assertIsInstance(store1.id, int)
        self.assertIsInstance(store1.name, str)
        self.assertIsInstance(store1.zipcode, str)
        self.assertIsInstance(store1.coords, tuple)

        store2 = sampler.generate()
        self.assertTrue(store2.id != store1.id)
        
        
