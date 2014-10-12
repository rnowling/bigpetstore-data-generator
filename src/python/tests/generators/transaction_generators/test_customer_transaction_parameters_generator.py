import unittest

import simulation_parameters as sim_param

from generators.transaction_generator.customer_transaction_parameters_generator import CustomerTransactionParameters
from generators.transaction_generator.customer_transaction_parameters_generator import CustomerTransactionParametersGenerator

class TestCustomerTransactionParametersGenerator(unittest.TestCase):
    def test_generate(self):
        generator = CustomerTransactionParametersGenerator()
        
        trans_params = generator.generate()

        self.assertIsInstance(trans_params, CustomerTransactionParameters)
        self.assertIsInstance(trans_params.pet_counts, dict)

        total_count = sum(trans_params.pet_counts.values())
        self.assertTrue(total_count >= sim_param.MIN_PETS)
        self.assertTrue(total_count <= sim_param.MAX_PETS)

        self.assertTrue(trans_params.average_transaction_trigger_time >= sim_param.TRANSACTION_TRIGGER_TIME_MIN)
        self.assertTrue(trans_params.average_transaction_trigger_time <= sim_param.TRANSACTION_TRIGGER_TIME_MAX)

        self.assertTrue(trans_params.average_purchase_trigger_time >= sim_param.PURCHASE_TRIGGER_TIME_MIN)
        self.assertTrue(trans_params.average_purchase_trigger_time <= sim_param.PURCHASE_TRIGGER_TIME_MAX)
