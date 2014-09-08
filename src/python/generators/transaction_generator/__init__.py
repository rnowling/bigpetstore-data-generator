from collections import defaultdict

import math
import random

from algorithms.samplers import RouletteWheelSampler

from datamodels.output_models import Transaction
from generators.transaction_generator.customer_state import CustomerState

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
    def __init__(self, customer_state=None, purchasing_profile=None):
        self.customer_state = customer_state
        self.purchasing_profile = purchasing_profile

    def choose_category(self, trans_time=None, num_purchases=None):
        category_weights = self.customer_state.item_category_weights(trans_time)

        if num_purchases != 0:
            category_weights.append(("stop", 0.1))

        weight_sum = 0.0
        for category, weight in category_weights:
            weight_sum += weight

        category_probabilities = []
        for category, weight in category_weights:
            category_probabilities.append((category, weight / weight_sum))

        sampler = RouletteWheelSampler(category_probabilities)
        
        return sampler.sample()

    def choose_product(self, category):
        msm = self.purchasing_profile.get_profile(category)
        product = msm.progress_state()
        return product

    def update_usage_simulations(self, trans_time=None, product=None):
        self.customer_state.update_inventory(trans_time, product)

    def simulate(self, trans_time=None):
        trans_products = []
        purchases = 0

        while True:
            category = self.choose_category(trans_time=trans_time,
                                            num_purchases=purchases)
            
            if category == "stop":
                break
            
            product = self.choose_product(category)
            
            self.update_usage_simulations(trans_time=trans_time,
                                          product=product)
            
            purchases += 1

            trans_products.append(product)
        
        return trans_products

class TransactionTimeSampler(object):
    def __init__(self, customer_state=None):
        self.customer_state = customer_state

    def propose_transaction_time(self):
        return self.customer_state.propose_transaction_time()

    def transaction_time_probability(self, proposed_trans_time, last_trans_time):
        if proposed_trans_time >= last_trans_time:
            return 1.0
        else:
            return 0.0

    def sample(self, last_trans_time):
        while True:
            proposed_time = self.propose_transaction_time()
            prob = self.transaction_time_probability(proposed_time, last_trans_time)
            r = random.random()
            if r < prob:
                return proposed_time


class TransactionGenerator(object):
    def __init__(self, stores=None,
                 product_categories=None):

        self.product_categories = product_categories
        self.stores = stores

        self.trans_count = 0
    
    def simulate(self, customer, purchasing_profile, end_time):
        params_generator = CustomerTransactionParametersGenerator()

        customer_trans_params = params_generator.generate()

        customer_state = \
            CustomerState(item_categories=self.product_categories,
                          customer_trans_params=customer_trans_params)

        trans_time_sampler = TransactionTimeSampler(customer_state=customer_state)
        purchase_sim = TransactionPurchasesGenerator(customer_state=customer_state, purchasing_profile=purchasing_profile)

        last_trans_time = 0.0
        while True:
            trans_time = trans_time_sampler.sample(last_trans_time)

            if trans_time > end_time:
                break
            
            purchased_items = purchase_sim.simulate(trans_time=trans_time)

            trans = Transaction(customer=customer,
                                purchased_items=purchased_items,
                                trans_time=trans_time,
                                trans_count=self.trans_count,
                                store=random.choice(self.stores))

            self.trans_count += 1
            last_trans_time = trans_time

            yield trans

    
