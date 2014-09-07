from collections import defaultdict

import math
import random

from algorithms.samplers import RouletteWheelSampler

from datamodels.output_models import Transaction

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
                 customer_state=None,
                 purchasing_profile=None):
        self.stores = stores
        self.customer_state = customer_state
        self.trans_time_sampler = TransactionTimeSampler(customer_state=customer_state)
        self.purchase_sim = TransactionPurchasesGenerator(customer_state=self.customer_state, purchasing_profile=purchasing_profile) 
        self.trans_count = 0
    
    def simulate(self, end_time):
        last_trans_time = 0.0
        while True:
            trans_time = self.trans_time_sampler.sample(last_trans_time)

            if trans_time > end_time:
                break
            
            purchased_items = self.purchase_sim.simulate(trans_time=trans_time)
            trans = Transaction(customer=self.customer_state.customer,
                                purchased_items=purchased_items,
                                trans_time=trans_time,
                                trans_count=self.trans_count,
                                store=random.choice(self.stores))
            self.trans_count += 1
            last_trans_time = trans_time
            yield trans

    
