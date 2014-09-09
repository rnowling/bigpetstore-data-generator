import random

from datamodels.output_models import Transaction
from generators.transaction_generator.customer_inventory import CustomerInventoryBuilder
from generators.transaction_generator.customer_transaction_parameters_generator import CustomerTransactionParametersGenerator
from generators.transaction_generator.transaction_purchases_generator import TransactionPurchasesGenerator

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

    
