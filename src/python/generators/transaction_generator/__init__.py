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
    """
    The purchases are simulated using a Hidden Markov Model.

    The states of the model are purchases or stop states.
    The purchse states are composed of the

        * transaction time
        * customer inventory
        * purchased item (not in the initial state)
        * number of purchases made in this transaction

    The initial state is the customer inventory at the start
    of the simulation, 0 purchases, and no item purchased.

    For each state, the probabilities of the state transitions
    are computed from the exhaustion time of each item category
    in the customer's inventory and the probabilities of each
    product that can be purchased according to the state of the
    Markov Models in the customer purchasing profile.

    There are no cycles in the transition graph (it's a DAG) -- the
    HMM can only keep moving to previously undiscovered states. 

    Since customers can technically make an infinite number of purchases,
    the number of possible states is infinite.  Therefore, we can't
    enumerate all of the states like we normally would for a markov
    model.

    Since there are a finite number of products, however, the number of
    states reachable from the current state is simply the number of
    products plus a stop state.  This makes the problem tractable.

    The weight of a particular transition is given by:
              p(product|category) p(category)

    The probability of each category is determined from when the customer's
    inventory will be exhausted and a Poisson process.  The probability
    of purchasing an item in a given category is given by the current state
    of the category's markov model in the purchasing profile.

    We take advantage of this structure as follows:
        1. Compute the probability of each category
        2. Choose a category or stop state
        3. Choose a product to purchase by progressing the state of the
           category's markov model

   This saves us from having to enumerate all possible next states
   """

    def __init__(self, purchasing_profile, trans_params):
        self.purchasing_profile = purchasing_profile
        self.trans_params = trans_params

    def _category_weight(self, exhaustion_time, trans_time):
        trigger_time = self.trans_params.average_purchase_trigger_time
        remaining_time = max(exhaustion_time - trans_time, 0.0)
        lambd = 1.0 / trigger_time
        weight = lambd * math.exp(-lambd * remaining_time)
        return weight

    def _choose_category(self, customer_inventory, trans_time, num_purchases):
        category_weights = []

        for category, exhaustion_time in customer_inventory.get_exhaustion_times().iteritems():
            weight = self._category_weight(exhaustion_time, trans_time)
            category_weights.append((category, weight))

        if num_purchases != 0:
            category_weights.append(("stop", 0.1))

        sampler = RouletteWheelSampler(category_weights)
        
        return sampler.sample()

    def _choose_product(self, category):
        msm = self.purchasing_profile.get_profile(category)
        product = msm.progress_state()
        return product

    def simulate(self, customer_inventory, trans_time):
        trans_products = []
        purchases = 0

        while True:
            category = self._choose_category(customer_inventory,
                                            trans_time,
                                            purchases)
            
            if category == "stop":
                break
            
            product = self._choose_product(category)
            
            customer_inventory.record_purchase(trans_time, product)
            
            purchases += 1

            trans_products.append(product)
        
        return trans_products

class TransactionTimeSampler(object):
    def __init__(self, customer_trans_params):
        self.customer_trans_params = customer_trans_params

    def _category_proposed_time(self, exhaustion_time):
        trigger_time = self.customer_trans_params.average_transaction_trigger_time
        lambd = 1.0 / trigger_time
        time_until_transaction = random.expovariate(lambd)
        transaction_time = max(exhaustion_time - time_until_transaction, 0.0)
        return transaction_time
        
    def _propose_transaction_time(self, customer_inventory):
        proposed_transaction_times = []
        for exhaustion_time in customer_inventory.get_exhaustion_times().values():
            transaction_time = self._category_proposed_time(exhaustion_time)
            proposed_transaction_times.append(transaction_time)
        
        return min(proposed_transaction_times)

    def _transaction_time_probability(self, proposed_trans_time, \
                                      last_trans_time):
        if proposed_trans_time >= last_trans_time:
            return 1.0
        else:
            return 0.0

    def sample(self, customer_inventory, last_trans_time):
        while True:
            proposed_time = self._propose_transaction_time(customer_inventory)

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
        
        self.params_generator = CustomerTransactionParametersGenerator()
        self.inventory_builder = CustomerInventoryBuilder(product_categories=self.product_categories)

    def simulate(self, customer, purchasing_profile, end_time):
        customer_trans_params = self.params_generator.generate()

        trans_time_sampler = TransactionTimeSampler(customer_trans_params)
        
        purchase_sim = TransactionPurchasesGenerator(purchasing_profile,
                                                     customer_trans_params)

        customer_inventory = self.inventory_builder.build(customer_trans_params)

        last_trans_time = 0.0
        while True:
            trans_time = trans_time_sampler.sample(customer_inventory,
                                                   last_trans_time)

            if trans_time > end_time:
                break
            
            purchased_items = purchase_sim.simulate(customer_inventory,
                                                    trans_time)

            store = random.choice(self.stores)

            trans = Transaction(customer=customer,
                                purchased_items=purchased_items,
                                trans_time=trans_time,
                                trans_count=self.trans_count,
                                store=store)

            self.trans_count += 1
            last_trans_time = trans_time

            yield trans

    
