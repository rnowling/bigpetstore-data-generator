import random

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
