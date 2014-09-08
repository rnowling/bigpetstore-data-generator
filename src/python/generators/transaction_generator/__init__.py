from collections import defaultdict

import math
import random

from algorithms.samplers import RouletteWheelSampler

from datamodels.output_models import Transaction
from generators.transaction_generator.customer_inventory import CustomerInventoryBuilder

import simulation_parameters as sim_param

class CustomerTransactionParameters(object):
    def __init__(self, pet_counts=None,
                 avg_trans_trigger_time=None,
                 avg_purch_trigger_time=None):
        self.pet_counts = pet_counts
        self.average_transaction_trigger_time=avg_trans_trigger_time
        self.average_purchase_trigger_time=avg_trans_trigger_time

class CustomerTransactionParametersGenerator(object):
    def _generate_pets(self):
        num_pets = random.randint(sim_param.MIN_PETS, sim_param.MAX_PETS)
        num_dogs = random.randint(0, num_pets)
        num_cats = num_pets - num_dogs

        pets = dict()
        pets["dog"] = num_dogs
        pets["cat"] = num_cats

        return pets

    def _generate_trigger_times(self):
        # days
        r = random.normalvariate(sim_param.TRANSACTION_TRIGGER_TIME_AVERAGE,
                                 sim_param.TRANSACTION_TRIGGER_TIME_VARIANCE)
        r = max(r, sim_param.TRANSACTION_TRIGGER_TIME_MIN)
        r = min(r, sim_param.TRANSACTION_TRIGGER_TIME_MAX)
        average_transaction_trigger_time = r
        
        r = random.normalvariate(sim_param.PURCHASE_TRIGGER_TIME_AVERAGE,
                                 sim_param.PURCHASE_TRIGGER_TIME_VARIANCE)
        r = max(r, sim_param.PURCHASE_TRIGGER_TIME_MIN)
        r = min(r, sim_param.PURCHASE_TRIGGER_TIME_MAX)        
        average_purchase_trigger_time = r

        return average_transaction_trigger_time, \
            average_purchase_trigger_time

    def generate(self):
        pet_counts = self._generate_pets()
        trans_trigger_time, purch_trigger_time = self._generate_trigger_times()

        return CustomerTransactionParameters(pet_counts=pet_counts,
                                             avg_trans_trigger_time=trans_trigger_time,
                                             avg_purch_trigger_time=purch_trigger_time)


class TransactionPurchasesGenerator(object):
    def _choose_category(self, customer_inventory, trigger_time, trans_time=None, num_purchases=None):
        category_weights = []
        for category, exhaustion_time in customer_inventory.get_exhaustion_times().iteritems():
            remaining_time = max(exhaustion_time - trans_time, 0.0)
            lambd = 1.0 / trigger_time
            weight = lambd * math.exp(-lambd * remaining_time)
            category_weights.append((category, weight))

        if num_purchases != 0:
            category_weights.append(("stop", 0.1))

        sampler = RouletteWheelSampler(category_weights)
        
        return sampler.sample()

    def _choose_product(self, purchasing_profile, category):
        msm = purchasing_profile.get_profile(category)
        product = msm.progress_state()
        return product

    def simulate(self, customer_inventory, trans_params, purchasing_profile, trans_time=None):
        trans_products = []
        purchases = 0

        while True:
            category = self._choose_category(customer_inventory,
                                             trans_params.average_purchase_trigger_time,
                                            trans_time=trans_time,
                                            num_purchases=purchases)
            
            if category == "stop":
                break
            
            product = self._choose_product(purchasing_profile, category)
            
            customer_inventory.record_purchase(trans_time, product)
            
            purchases += 1

            trans_products.append(product)
        
        return trans_products

class TransactionTimeSampler(object):
    def _propose_transaction_time(self, customer_inventory, trigger_time):
        proposed_transaction_times = []
        for category, exhaustion_time in customer_inventory.get_exhaustion_times().iteritems():
            lambd = 1.0 / trigger_time
            time_until_transaction = random.expovariate(lambd)
            transaction_time = max(exhaustion_time - time_until_transaction, 0.0)
            proposed_transaction_times.append(transaction_time)
        
        return min(proposed_transaction_times)

    def _transaction_time_probability(self, proposed_trans_time, \
                                      last_trans_time):
        if proposed_trans_time >= last_trans_time:
            return 1.0
        else:
            return 0.0

    def sample(self, customer_inventory, trans_params, last_trans_time):
        while True:
            proposed_time = self._propose_transaction_time(customer_inventory,
                                                           trans_params.average_transaction_trigger_time)
            prob = self._transaction_time_probability(proposed_time, \
                                                      last_trans_time)
            r = random.random()
            if r < prob:
                return proposed_time


class TransactionGenerator(object):
    def __init__(self, stores=None,
                 product_categories=None):

        self.product_categories = product_categories
        self.stores = stores

        self.trans_count = 0

        self.inventory_builder = CustomerInventoryBuilder(product_categories=self.product_categories)
        self.trans_time_sampler = TransactionTimeSampler()
        self.purchase_sim = TransactionPurchasesGenerator()

    def simulate(self, customer, purchasing_profile, end_time):
        params_generator = CustomerTransactionParametersGenerator()

        customer_trans_params = params_generator.generate()

        customer_inventory = self.inventory_builder.build(customer_trans_params)

        last_trans_time = 0.0
        while True:
            trans_time = self.trans_time_sampler.sample(customer_inventory, \
                                                        customer_trans_params, \
                                                        last_trans_time)

            if trans_time > end_time:
                break
            
            purchased_items = self.purchase_sim.simulate(customer_inventory,
                                                         customer_trans_params,
                                                         purchasing_profile,
                                                         trans_time=trans_time)

            store = random.choice(self.stores)

            trans = Transaction(customer=customer,
                                purchased_items=purchased_items,
                                trans_time=trans_time,
                                trans_count=self.trans_count,
                                store=store)

            self.trans_count += 1
            last_trans_time = trans_time

            yield trans

    
