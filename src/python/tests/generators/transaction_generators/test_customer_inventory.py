import unittest

from generators.transaction_generator.customer_inventory import ItemCategoryUsageSimulation
from generators.transaction_generator.customer_inventory import ItemCategorySimulation
from generators.transaction_generator.customer_inventory import CustomerInventory
from generators.transaction_generator.customer_inventory import CustomerInventoryBuilder


class TestItemCategoryUsageSimulation(unittest.TestCase):
    def test_simulate(self):
        sim = ItemCategoryUsageSimulation(initial_amount=30.0,
                                          initial_time=0.0,
                                          daily_usage_rate=2.0,
                                          amount_used_average=0.5,
                                          amount_used_variance=0.2)

        sim.simulate()

    def test_exhaustion_time(self):
        sim = ItemCategoryUsageSimulation(initial_amount=30.0,
                                          initial_time=0.0,
                                          daily_usage_rate=2.0,
                                          amount_used_average=0.5,
                                          amount_used_variance=0.2)

        sim.simulate()

        exhaustion_time = sim.exhaustion_time()
        self.assertTrue(exhaustion_time > 0.0)

    def test_amount_at_time(self):
        sim = ItemCategoryUsageSimulation(initial_amount=30.0,
                                          initial_time=0.0,
                                          daily_usage_rate=2.0,
                                          amount_used_average=0.5,
                                          amount_used_variance=0.2)

        sim.simulate()

        exhaustion_time = sim.exhaustion_time()
        self.assertTrue(exhaustion_time > 0.0)
        
        last_amount = sim.amount_at_time(exhaustion_time)
        self.assertTrue(last_amount >= 0.0)
        self.assertTrue(last_amount < 0.001)

    def test_stochastic(self):
        sim1 = ItemCategoryUsageSimulation(initial_amount=30.0,
                                           initial_time=0.0,
                                           daily_usage_rate=2.0,
                                           amount_used_average=0.5,
                                           amount_used_variance=0.2)

        sim2 = ItemCategoryUsageSimulation(initial_amount=30.0,
                                           initial_time=0.0,
                                           daily_usage_rate=2.0,
                                           amount_used_average=0.5,
                                           amount_used_variance=0.2)

        sim1.simulate()
        sim2.simulate()

        exhaustion_time1 = sim1.exhaustion_time()
        exhaustion_time2 = sim2.exhaustion_time()
        self.assertNotEqual(exhaustion_time1, exhaustion_time2)

class ItemCategoryMock(object):
    def __init__(self, daily_usage_rate, base_amount_used_average, base_amount_used_variance, species=None):
        self.daily_usage_rate = daily_usage_rate
        self.base_amount_used_average = base_amount_used_average
        self.base_amount_used_variance = base_amount_used_variance
        self.species = species

class TestItemCategorySimulation(unittest.TestCase):
    def test_record_purchase(self):
        item_category = ItemCategoryMock(2.0, 0.5, 0.2)

        sim = ItemCategorySimulation(item_category, 1.0)
        
        exhaustion_time0 = sim.exhaustion_time()
        self.assertEqual(exhaustion_time0, 0.0)

        sim.record_purchase(0.0, 30.0)

        exhaustion_time1 = sim.exhaustion_time()
        self.assertTrue(exhaustion_time1 > exhaustion_time0)

        sim.record_purchase(15.0, 30.0)
        exhaustion_time2 = sim.exhaustion_time()
        self.assertTrue(exhaustion_time2 > exhaustion_time1)

    def test_get_remaining_amount(self):
        item_category = ItemCategoryMock(2.0, 0.5, 0.2)

        sim = ItemCategorySimulation(item_category, 1.0)
        
        rem_amount0 = sim.get_remaining_amount(0.0)
        self.assertEqual(rem_amount0, 0.0)

        sim.record_purchase(0.0, 30.0)

        rem_amount1 = sim.get_remaining_amount(10.0)
        self.assertTrue(rem_amount1 > rem_amount0)

        sim.record_purchase(15.0, 30.0)
        rem_amount2 = sim.get_remaining_amount(16.0)
        self.assertTrue(rem_amount2 > rem_amount1)

class TestCustomerInventory(unittest.TestCase):
    def test_usage(self):
        product_sims = {
            "dog_food" :
            ItemCategorySimulation(ItemCategoryMock(2.0, 0.5, 0.2), 1.0),
            "cat_food" :
            ItemCategorySimulation(ItemCategoryMock(2.0, 0.5, 0.2), 2.0)
            }

        customer_inventory = CustomerInventory(product_sims)

        exhaustion_times = customer_inventory.get_exhaustion_times()
        for time in exhaustion_times.values():
            self.assertEquals(time, 0.0)

        inventory_amounts = customer_inventory.get_inventory_amounts(5.0)
        for amount in inventory_amounts.values():
            self.assertEqual(amount, 0.0)

        for category in product_sims.keys():
            product = {
                "category" : category,
                "size" : 15.0
                }
            customer_inventory.record_purchase(0.0, product)

        exhaustion_times = customer_inventory.get_exhaustion_times()
        for time in exhaustion_times.values():
            self.assertTrue(time > 0.0)

        inventory_amounts = customer_inventory.get_inventory_amounts(5.0)
        for amount in inventory_amounts.values():
            self.assertTrue(amount > 0.0)


class CustomerTransactionParametersMock(object):
    def __init__(self, pet_counts):
        self.pet_counts = pet_counts

class TestCustomerInventoryBuilder(unittest.TestCase):
    def test_build(self):
        product_cats = {
            "dog_food" : ItemCategoryMock(2.0, 0.5, 0.2, species=["dog"]),
            "cat_food" : ItemCategoryMock(2.0, 0.5, 0.2, species=["cat"])
            }

        builder = CustomerInventoryBuilder(product_cats)

        pet_counts = {"dog" : 1, "cat": 1}
        trans_params = CustomerTransactionParametersMock(pet_counts)

        customer_inventory = builder.build(trans_params)

    def test_num_pets(self):
        product_cats = {
            "dog_food" : ItemCategoryMock(2.0, 0.5, 0.2, species=["dog"]),
            "cat_food" : ItemCategoryMock(2.0, 0.5, 0.2, species=["cat"])
            }

        builder = CustomerInventoryBuilder(product_cats)

        pet_counts = {"dog" : 1, "cat": 1}
        trans_params = CustomerTransactionParametersMock(pet_counts)
        customer_inventory_1 = builder.build(trans_params)

        for category in product_cats.keys():
            product = {
                "category" : category,
                "size" : 30.0
                }
            customer_inventory_1.record_purchase(0.0, product)

        exhaustion_times_1 = customer_inventory_1.get_exhaustion_times()
        
        pet_counts = {"dog" : 5, "cat": 5}
        trans_params = CustomerTransactionParametersMock(pet_counts)
        customer_inventory_2 = builder.build(trans_params)

        for category in product_cats.keys():
            product = {
                "category" : category,
                "size" : 30.0
                }
            customer_inventory_2.record_purchase(0.0, product)
        
        exhaustion_times_2 = customer_inventory_2.get_exhaustion_times()

        for category in product_cats.keys():
            self.assertTrue(exhaustion_times_1[category] > exhaustion_times_2[category])
