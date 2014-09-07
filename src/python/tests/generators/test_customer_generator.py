import unittest

import simulation_parameters as sim_params

from datamodels.output_models import Customer

from generators.customer_generator import CustomerGenerator
from generators.customer_generator import LocationSampler
from generators.customer_generator import NameSampler

from readers import load_names

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
    pass

class TestCustomerGenerator(unittest.TestCase):
    pass
        
