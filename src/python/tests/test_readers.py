import unittest

from collections import Sequence

import simulation_parameters as sim_params

from datamodels.inputs import ProductCategory
from datamodels.inputs import ZipcodeRecord

from readers import load_names
from readers import load_products
from readers import load_zipcode_data

class ReaderTests(unittest.TestCase):
    def test_load_zipcode_data(self):
        income_fl = sim_params.ZIPCODE_DATA_FILES["income_fl"]
        pop_fl = sim_params.ZIPCODE_DATA_FILES["population_fl"]
        coord_fl = sim_params.ZIPCODE_DATA_FILES["coordinate_fl"]
        
        records = load_zipcode_data(income_fl, pop_fl, coord_fl)

        self.assertTrue(len(records) > 0)
        self.assertIsInstance(records, dict)
        self.assertIsInstance(records.keys()[0], str)

        record = records.values()[0]

        self.assertIsInstance(record, ZipcodeRecord)
        self.assertIsInstance(record.zipcode, str)
        self.assertIsInstance(record.median_household_income, float)
        self.assertIsInstance(record.population, int)
        self.assertIsInstance(record.coords, tuple)
        self.assertTrue(len(record.coords) == 2)
        self.assertIsInstance(record.coords[0], float)
        self.assertIsInstance(record.coords[1], float)


    def test_load_names(self):
        namedb_fl = sim_params.NAMEDB_FILE
        first_names, last_names = load_names(namedb_fl)

        self.assertTrue(len(first_names) > 0)
        self.assertIsInstance(first_names[0], tuple)
        self.assertTrue(len(first_names[0]) == 2)
        self.assertIsInstance(first_names[0][0], str)
        self.assertIsInstance(first_names[0][1], float)

        self.assertTrue(len(last_names) > 0)
        self.assertIsInstance(last_names[0], tuple)
        self.assertTrue(len(last_names[0]) == 2)
        self.assertIsInstance(last_names[0][0], str)
        self.assertIsInstance(last_names[0][1], float)

    def test_load_products(self):
        products_fl = sim_params.PRODUCTS_FILE
        product_categories = load_products(products_fl)

        self.assertIsInstance(product_categories, dict)
        self.assertTrue(len(product_categories) > 0)
        
        self.assertIsInstance(product_categories.keys()[0], basestring)
        
        product_category = product_categories.values()[0]

        self.assertIsInstance(product_category, ProductCategory)
        self.assertIsInstance(product_category.category_label, basestring)
        self.assertIsInstance(product_category.fields, Sequence)
        self.assertIsInstance(product_category.fields[0], basestring)
        self.assertIsInstance(product_category.items, Sequence)
        self.assertIsInstance(product_category.items[0], dict)
        self.assertIsInstance(product_category.species, Sequence)
        self.assertIsInstance(product_category.species[0], basestring)
        self.assertIsInstance(product_category.trigger_transaction, bool)
        self.assertIsInstance(product_category.daily_usage_rate, float)
        self.assertIsInstance(product_category.base_amount_used_average, float)
        self.assertIsInstance(product_category.base_amount_used_variance, float)
        self.assertIsInstance(product_category.transaction_trigger_rate, float)
        self.assertIsInstance(product_category.transaction_purchase_rate, float)
        
