from collections import defaultdict

import math
import random

from markovmodel import MarkovModelBuilder
from samplers import RouletteWheelSampler

class ItemCategoryMarkovModelBuilder(object):
    def __init__(self, item_category=None, customer=None):
        self.item_category = item_category
        self.customer = customer

    def _normalize_field_weights(self):
        weight_sum = sum(self.field_weights.itervalues())

        for field, weight in list(self.field_weights.iteritems()):
            self.field_weights[field] = weight / weight_sum

    def _generate_transition_parameters(self):
        self.field_weights = dict()
        self.field_similarity_weights = dict()
        for field in self.item_category.fields:
            avg = random.choice([0.15, 0.85])
            self.field_weights[field] = min(0.95, max(0.05, random.normalvariate(avg, 0.1)))
            avg = random.choice([0.15, 0.85])
            self.field_similarity_weights[field] = min(0.95, max(0.05, random.normalvariate(avg, 0.1)))
        avg = random.choice([0.25, 0.75])
        self.loopback_weight = min(0.95, max(0.05, random.normalvariate(avg, 0.1)))

    def similarity_weight(self, rec1, rec2):
        weight = 0.0
        for field in self.item_category.fields:
            if rec1[field] == rec2[field]:
                weight += self.field_weights[field] * self.field_similarity_weights[field]
            else:
                weight += self.field_weights[field] * (1.0 - self.field_similarity_weights[field])
        return weight

    def create_markov_model(self):
        self._generate_transition_parameters()
        self._normalize_field_weights()

        builder = MarkovModelBuilder()

        for rec in self.item_category.items:
            builder.add_state(tuple(rec.items()))
            weight_sum = 0.0
            for other_rec in self.item_category.items:
                if rec != other_rec:
                    weight_sum += self.similarity_weight(rec, other_rec)

            for other_rec in self.item_category.items:
                weight = 0.0
                if rec != other_rec:
                    weight = (1.0 - self.loopback_weight) * self.similarity_weight(rec, other_rec) / weight_sum
                else:
                    weight = self.loopback_weight
                builder.add_edge_weight(tuple(rec.items()), tuple(other_rec.items()), weight)

        return builder.build_msm()


class Transaction(object):
    def __init__(self, customer=None, trans_time=None, purchased_items=None, inventory_before=None, inventory_after=None):
        self.customer = customer
        self.trans_time = trans_time
        self.purchased_items = purchased_items
        self.inventory_before = inventory_before
        self.inventory_after = inventory_after

    def __repr__(self):
        return "(%s, Time: %s, Purchased: %s\n\tInventory Before: %s\n\tInventory After: %s)" % (self.customer,
            self.trans_time, len(self.purchased_items), self.inventory_before,
                self.inventory_after)


class TransactionPurchasesSimulator(object):
    def __init__(self, customer_state=None, item_categories=None):
        self.customer_state = customer_state

        self.item_purchases_msms = dict()
        for category_label, category_data in item_categories.iteritems():
            num_pets = 0
            for species in category_data.species:
                num_pets += customer_state.customer.pets[species]

            if num_pets > 0:
                builder = ItemCategoryMarkovModelBuilder(item_category=category_data,
                                               customer=customer_state.customer)
                self.item_purchases_msms[category_label] = builder.create_markov_model()

                msm = self.item_purchases_msms[category_label]

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

    def choose_item(self, category):
        item = self.item_purchases_msms[category].progress_state()
        return item

    def update_usage_simulations(self, trans_time=None, item=None):
        self.customer_state.update_inventory(trans_time, item)

    def simulate(self, trans_time=None):
        trans_items = []
        purchases = 0

        while True:
            category = self.choose_category(trans_time=trans_time,
                                            num_purchases=purchases)
            
            if category == "stop":
                break
            
            item = self.choose_item(category)
            
            self.update_usage_simulations(trans_time=trans_time,
                                          item=item)
            
            purchases += 1

            trans_items.append(item)
        
        return trans_items

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


class TransactionSimulator(object):
    def __init__(self, customer_state=None, item_categories=None):
        self.customer_state = customer_state
        self.trans_time_sampler = TransactionTimeSampler(customer_state=customer_state)
        self.purchase_sim = TransactionPurchasesSimulator(customer_state=self.customer_state, item_categories=item_categories) 
    
    def simulate(self, end_time):
        last_trans_time = 0.0
        while True:
            trans_time = self.trans_time_sampler.sample(last_trans_time)

            if trans_time > end_time:
                break
            
            remaining_before = self.customer_state.get_inventory_amounts(trans_time)
            purchased_items = self.purchase_sim.simulate(trans_time=trans_time)
            remaining_after = self.customer_state.get_inventory_amounts(trans_time)
            trans = Transaction(customer=self.customer_state.customer,
                        purchased_items=purchased_items,
                        trans_time=trans_time,
                        inventory_before=remaining_before,
                        inventory_after=remaining_after)
            last_trans_time = trans_time
            yield trans

    
