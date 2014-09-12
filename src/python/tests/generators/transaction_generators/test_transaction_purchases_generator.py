import unittest

from algorithms.markovmodel import MarkovModel
from algorithms.markovmodel import MarkovProcess
from algorithms.markovmodel import MarkovModelBuilder

from generators.transaction_generator.transaction_purchases_generator import PurchasingProcessesBuilder
from generators.transaction_generator.transaction_purchases_generator import PurchasingProcesses

from datamodels.simulation_models import PurchasingProfile

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
    pass

class TestTransactionPurchasesGenerator(unittest.TestCase):
    pass
 
