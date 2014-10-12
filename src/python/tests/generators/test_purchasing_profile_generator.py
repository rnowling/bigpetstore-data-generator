import unittest

import simulation_parameters as sim_params

from algorithms.markovmodel import MarkovModel
from algorithms.markovmodel import MarkovModelBuilder

from generators.purchasing_profile_generator import PurchasingProfileBuilder
from generators.purchasing_profile_generator import PurchasingProfileGenerator
from generators.purchasing_profile_generator import ProductCategoryMarkovModelGenerator

from datamodels.simulation_models import PurchasingProfile

from readers import load_products

class TestPurchasingProfileBuilder(unittest.TestCase):
    def test_add_profile(self):
        profile_builder = PurchasingProfileBuilder()

        for name in ["alpha", "beta", "gamma"]:
            builder = MarkovModelBuilder()
            builder.add_start_state("a", 1.0)
            builder.add_edge_weight("a", "b", 0.5)
            builder.add_edge_weight("a", "c", 0.5)
            model = builder.build_msm()
            profile_builder.add_profile(name, model)

        self.assertIsInstance(profile_builder.profiles, dict)
        self.assertTrue(len(profile_builder.profiles) == 3)
        self.assertIn("alpha", profile_builder.profiles)
        self.assertIn("beta", profile_builder.profiles)
        self.assertIn("gamma", profile_builder.profiles)

    def test_build(self):
        profile_builder = PurchasingProfileBuilder()

        for name in ["alpha", "beta", "gamma"]:
            builder = MarkovModelBuilder()
            builder.add_start_state("a", 1.0)
            builder.add_edge_weight("a", "b", 0.5)
            builder.add_edge_weight("a", "c", 0.5)
            model = builder.build_msm()
            profile_builder.add_profile(name, model)

        profile = profile_builder.build()

        self.assertIsInstance(profile, PurchasingProfile)

        self.assertTrue(len(profile.get_product_categories()) == 3)

        result = profile.get_profile("alpha")
        self.assertIsInstance(result, MarkovModel)

        result = profile.get_profile("beta")
        self.assertIsInstance(result, MarkovModel)

        result = profile.get_profile("gamma")
        self.assertIsInstance(result, MarkovModel)


class TestProductCategoryMarkovModelGenerator(unittest.TestCase):
    def test_init(self):
        products_fl = sim_params.PRODUCTS_FILE
        product_categories = load_products(products_fl)

        category = product_categories.values()[0]

        generator = ProductCategoryMarkovModelGenerator(category)

    def test_generate(self):
        products_fl = sim_params.PRODUCTS_FILE
        product_categories = load_products(products_fl)

        category = product_categories.values()[0]

        generator = ProductCategoryMarkovModelGenerator(category)

        msm = generator.generate()

        self.assertIsInstance(msm, MarkovModel)
        

class TestPurchasingProfileGenerator(unittest.TestCase):
    def test_init(self):
        products_fl = sim_params.PRODUCTS_FILE
        product_categories = load_products(products_fl)
        
        generator = PurchasingProfileGenerator(product_categories)

        self.assertEqual(generator.product_categories, product_categories)

    def test_generate(self):
        products_fl = sim_params.PRODUCTS_FILE
        product_categories = load_products(products_fl)
        
        generator = PurchasingProfileGenerator(product_categories)

        profile = generator.generate()

        self.assertIsInstance(profile, PurchasingProfile)
        
