from algorithms.samplers import RouletteWheelSampler

import math

class TransactionPurchasesHiddenMarkovModel(object):
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
    def __init__(self, purchasing_processes, trans_params, customer_inventory, trans_time):
        self.purchasing_processes = purchasing_processes
        self.trans_params = trans_params
        self.num_purchases = 0
        self.trans_time = trans_time
        self.customer_inventory = customer_inventory

    def _category_weight(self, exhaustion_time):
        """
        TODO: Discussion of Poisson process
        """
        trigger_time = self.trans_params.average_purchase_trigger_time
        remaining_time = max(exhaustion_time - self.trans_time, 0.0)
        lambd = 1.0 / trigger_time
        weight = lambd * math.exp(-lambd * remaining_time)
        return weight

    def _choose_category(self):
        category_weights = []

        exhaustion_times = self.customer_inventory.get_exhaustion_times()
        for category, exhaustion_time in exhaustion_times.iteritems():
            weight = self._category_weight(exhaustion_time)
            category_weights.append((category, weight))

        if self.num_purchases != 0:
            category_weights.append(("stop", 0.1))

        sampler = RouletteWheelSampler(category_weights)
        
        return sampler.sample()

    def _choose_product(self, category):
        return self.purchasing_processes.simulate_purchase(category)

    def progress_state(self):
        category = self._choose_category()

        if category == "stop":
            return None
            
        product = self._choose_product(category)
        self.customer_inventory.record_purchase(self.trans_time, product)
        self.num_purchases += 1

        return product

class TransactionPurchasesGenerator(object):
    def __init__(self, purchasing_profile, trans_params):
        self.purchasing_processes = purchasing_profile.build_processes()
        self.trans_params = trans_params
        
    def simulate(self, customer_inventory, trans_time):
        hmm = TransactionPurchasesHiddenMarkovModel(self.purchasing_processes,
                                                    self.trans_params,
                                                    customer_inventory,
                                                    trans_time)
        
        trans_products = []

        while True:
            product = hmm.progress_state()
            if product is None:
                return trans_products
            trans_products.append(product)
