import unittest

from algorithms.markovmodel import MarkovModel
from algorithms.markovmodel import MarkovProcess
from algorithms.markovmodel import MarkovModelBuilder

from generators.transaction_generator.transaction_purchases_generator import PurchasingProcessesBuilder
from generators.transaction_generator.transaction_purchases_generator import PurchasingProcesses
from generators.transaction_generator.transaction_purchases_generator import TransactionPurchasesHiddenMarkovModel
from generators.transaction_generator.transaction_purchases_generator import TransactionPurchasesGenerator
from generators.transaction_generator.customer_inventory import CustomerInventoryBuilder


from generators.purchasing_profile_generator import PurchasingProfileGenerator
from generators.transaction_generator.customer_transaction_parameters_generator import CustomerTransactionParametersGenerator

from datamodels.simulation_models import PurchasingProfile

from readers import load_products

import simulation_parameters as sim_params

class TestPurchasingProcessesBuilder(unittest.TestCase):
    def test_build(self):
        models = dict()
        for name in ["alpha", "beta", "gamma"]:
            builder = MarkovModelBuilder()
            builder.add_start_state("a", 1.0)
            builder.add_edge_weight("a", "b", 0.5)
            builder.add_edge_weight("a", "c", 0.5)
            model = builder.build_msm()
            models[name] = model

        profile = PurchasingProfile(models)

        builder = PurchasingProcessesBuilder()

        processes = builder.build(profile)
        self.assertIsInstance(processes, PurchasingProcesses)
        self.assertIsInstance(processes.purchasing_processes, dict)
        self.assertIn("alpha", processes.purchasing_processes)
        self.assertIn("beta", processes.purchasing_processes)
        self.assertIn("gamma", processes.purchasing_processes)

class TestPurchasingProcesses(unittest.TestCase):
    def test_simulate_purchase(self):
        processes = dict()
        for name in ["alpha", "beta", "gamma"]:
            builder = MarkovModelBuilder()
            builder.add_start_state("a", 1.0)
            builder.add_edge_weight("a", "b", 0.5)
            builder.add_edge_weight("a", "c", 0.5)
            model = builder.build_msm()
            process = MarkovProcess(model)
            processes[name] = process
        
        processes = PurchasingProcesses(processes)

        result = processes.simulate_purchase("alpha")
        self.assertEquals(result, "a")

        result = processes.simulate_purchase("alpha")
        self.assertIn(result, ["b", "c"])

        result = processes.simulate_purchase("beta")
        self.assertEquals(result, "a")

        result = processes.simulate_purchase("beta")
        self.assertIn(result, ["b", "c"])

        result = processes.simulate_purchase("gamma")
        self.assertEquals(result, "a")

        result = processes.simulate_purchase("gamma")
        self.assertIn(result, ["b", "c"])

        

        self.assertRaises(KeyError, processes.simulate_purchase, "delta")

class TestTransactionPurchasesHiddenMarkovModel(unittest.TestCase):
    def test_init(self):
        products_fl = sim_params.PRODUCTS_FILE
        product_categories = load_products(products_fl)
        
        profile_generator = PurchasingProfileGenerator(product_categories)
        purchasing_profile = profile_generator.generate()
        processes_builder = PurchasingProcessesBuilder()
        processes = processes_builder.build(purchasing_profile)

        params_generator = CustomerTransactionParametersGenerator()
        trans_params = params_generator.generate()

        inventory_builder = CustomerInventoryBuilder(product_categories)        
        customer_inventory = inventory_builder.build(trans_params)

        hmm = TransactionPurchasesHiddenMarkovModel(processes, trans_params,
                                                    customer_inventory, 0.0)
    def test_progress_state(self):
        products_fl = sim_params.PRODUCTS_FILE
        product_categories = load_products(products_fl)
        
        profile_generator = PurchasingProfileGenerator(product_categories)
        purchasing_profile = profile_generator.generate()
        processes_builder = PurchasingProcessesBuilder()
        processes = processes_builder.build(purchasing_profile)

        params_generator = CustomerTransactionParametersGenerator()
        trans_params = params_generator.generate()

        inventory_builder = CustomerInventoryBuilder(product_categories)        
        customer_inventory = inventory_builder.build(trans_params)

        hmm = TransactionPurchasesHiddenMarkovModel(processes, trans_params,
                                                    customer_inventory, 0.0)

        purchase = hmm.progress_state()
        

class TestTransactionPurchasesGenerator(unittest.TestCase):
    def test_init(self):
        products_fl = sim_params.PRODUCTS_FILE
        product_categories = load_products(products_fl)
        
        profile_generator = PurchasingProfileGenerator(product_categories)
        profile = profile_generator.generate()

        params_generator = CustomerTransactionParametersGenerator()
        trans_params = params_generator.generate()

        generator = TransactionPurchasesGenerator(profile, trans_params)

    def test_simulate(self):
        products_fl = sim_params.PRODUCTS_FILE
        product_categories = load_products(products_fl)
        
        profile_generator = PurchasingProfileGenerator(product_categories)
        profile = profile_generator.generate()

        params_generator = CustomerTransactionParametersGenerator()
        trans_params = params_generator.generate()


        inventory_builder = CustomerInventoryBuilder(product_categories)        
        customer_inventory = inventory_builder.build(trans_params)

        generator = TransactionPurchasesGenerator(profile, trans_params)
        trans_time = 100.0
        purchases = generator.simulate(customer_inventory, 100.0)

        self.assertIsInstance(purchases, list)

        # TODO test cases with only 1 pet species
        
